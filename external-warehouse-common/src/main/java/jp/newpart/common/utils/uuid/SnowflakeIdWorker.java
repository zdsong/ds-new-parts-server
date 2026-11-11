package jp.newpart.common.utils.uuid;

import jp.newpart.common.utils.ValueUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicLong;

/**
 * SnowflakeIdWorkerクラスは、TwitterのSnowflakeアルゴリズムに類似した分散ユニークIDを生成します。
 * 時間戳、マシンID、シーケンス番号を組み合わせてユニークIDを生成し、分散システム環境に適しています。
 *
 * @author nextosd
 */
@Slf4j
public class SnowflakeIdWorker {
    /**
     * 時間戳の起点（2016-11-27 00:00:00）
     */
    private final static long START_STMP = 1480166465631L;
    /**
     * シーケンス番号が占めるビット数
     */
    private final static long SEQUENCE_BIT = 12;
    /**
     * マシンIDが占めるビット数
     */
    private final static long MACHINE_BIT = 10;
    /**
     * 時間戳が占めるビット数
     */
    private final static long TIMESTAMP_BIT = 41;
    /**
     * 最大マシンID、MACHINE_BITによって決定される
     */
    private final static long MAX_MACHINE_NUM = (1L << MACHINE_BIT) - 1;
    /**
     * 最大シーケンス番号、SEQUENCE_BITによって決定される
     */
    private final static long MAX_SEQUENCE = (1L << SEQUENCE_BIT) - 1;
    /**
     * マシンIDの左シフトビット数、シーケンス番号が占めるビット数と等しい
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    /**
     * 時間戳の左シフトビット数、シーケンス番号とマシンIDが占める合計ビット数と等しい
     */
    private final static long TIMESTAMP_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    /**
     * シーケンス番号、AtomicLongを使用してスレッドセーフを保証する
     */
    private final AtomicLong sequence = new AtomicLong(0L);
    /**
     * 最終時間戳、AtomicLongを使用してスレッドセーフを保証する
     */
    private final AtomicLong lastStmp = new AtomicLong(-1L);
    /**
     * マシンID
     */
    private final long machineId;

    /**
     * コンストラクタ、マシンIDを初期化します。
     * マシンIDが最大マシンIDを超えるか0未満の場合、IllegalArgumentException例外をスローします。
     */
    public SnowflakeIdWorker() {
        this.machineId = getMachineId();
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
    }

    /**
     * 次のIDを生成します。
     * このメソッドは同期化されており、マルチスレッド環境下で安全にIDを生成します。
     *
     * @return 生成されたユニークID。
     */
    public synchronized long nextId() {
        long currStmp = getNewStmp();
        if (currStmp < lastStmp.get()) {
            // 時間戳が正常になるまで自転待機する
            while (currStmp <= lastStmp.get()) {
                currStmp = getNewStmp();
            }
        }

        if (currStmp == lastStmp.get()) {
            long seq = (sequence.incrementAndGet() & MAX_SEQUENCE);
            if (seq == 0L) {
                currStmp = getNextMill();
                sequence.set(0L);
            }
        } else {
            sequence.set(0L);
        }

        lastStmp.set(currStmp);

        return ((currStmp - START_STMP) << TIMESTAMP_LEFT)
                | (machineId << MACHINE_LEFT)
                | sequence.get();
    }

    /**
     * 次のミリ秒時間戳を取得します。
     * 指定时间内により大きな時間戳を取得できない場合、RuntimeException例外をスローします。
     *
     * @return 次のミリ秒時間戳。
     */
    private long getNextMill() {
        long mill = getNewStmp();
        // 1秒のタイムアウトを設定
        long timeout = System.currentTimeMillis() + 1000;
        while (mill <= lastStmp.get() && System.currentTimeMillis() < timeout) {
            mill = getNewStmp();
        }
        if (mill <= lastStmp.get()) {
            throw new RuntimeException("Failed to get next millisecond timestamp within timeout");
        }
        return mill;
    }

    /**
     * 現在の時間戳を取得します。
     *
     * @return 現在の時間戳。
     */
    private long getNewStmp() {
        return System.currentTimeMillis();
    }

    /**
     * マシンIDを取得します。
     * 最初にネットワークインターフェースのMACアドレスに基づいてマシンIDを生成を試みます。失敗した場合は、ホスト名とプロセスIDに基づいて生成します。
     *
     * @return マシンID。
     */
    private long getMachineId() {
        try {
            byte[] mac = null;

            InetAddress ip = getLocalHostLanAddress();
            mac = NetworkInterface.getByInetAddress(ip).getHardwareAddress();

            if(ValueUtils.isEmpty(mac)) {
                // mac address
                NetworkInterface network = NetworkInterface.getByInetAddress(
                        InetAddress.getLocalHost());

                mac = network.getHardwareAddress();
            }

            if(ValueUtils.isEmpty(mac)){
                return 1;
            }

            long machineId = 0;

            for (int i = 0; i < mac.length; i++) {
                machineId += ((0xFF & mac[i]) << (8 * i));
            }

            return Math.abs(machineId % (MAX_MACHINE_NUM + 1));
        } catch (Exception e) {
            log.error("MACアドレスを取得できない場合、ホスト名とPIDを使用してマシン識別子を生成する");
            // MACアドレスを取得できない場合、ホスト名とPIDを使用してマシン識別子を生成する
            String hostName = System.getenv("HOSTNAME") != null ? System.getenv("HOSTNAME") : "default";
            int pid = ManagementFactory.getRuntimeMXBean().getName().hashCode();
            return Math.abs((hostName.hashCode() + pid) % (MAX_MACHINE_NUM + 1));
        }
    }

    /**
     * ローカルホストの局域網アドレスを取得します。
     * 全てのネットワークインターフェースとアドレスを列挙し、優先的に局域網アドレスを返します。
     * 局域網アドレスを決定できない場合、UnknownHostException例外をスローします。
     *
     * @return 局域網アドレス。
     * @throws UnknownHostException 局域網アドレスを決定できない場合。
     * @throws SocketException      ネットワークエラーが発生した場合。
     */
    private InetAddress getLocalHostLanAddress() throws UnknownHostException, SocketException {
        try {
            InetAddress candidateAddress = null;
            for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = ifaces.nextElement();
                for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (Exception e) {
            log.error("Failed to determine LAN address", e);
            throw new UnknownHostException("Failed to determine LAN address: " + e.getMessage());
        }
    }
}
