package jp.newpart.common.utils.uuid;

import java.util.concurrent.atomic.AtomicInteger;
import jp.newpart.common.utils.DateUtils;
import jp.newpart.common.utils.StringUtils;

/**
 * シーケンス生成クラス
 *
 * @author nextosd
 */
public class Seq
{
    // 共通シーケンスタイプ
    public static final String commSeqType = "COMMON";

    // アップロードシーケンスタイプ
    public static final String uploadSeqType = "UPLOAD";

    // 汎用インタフェースシーケンス数
    private static AtomicInteger commSeq = new AtomicInteger(1);

    // アップロードインタフェースのシーケンス数
    private static AtomicInteger uploadSeq = new AtomicInteger(1);

    // マシンID
    private static final String machineCode = "A";

    /**
     * 共通シリアル番号の取得
     * 
     * @return シーケンス値
     */
    public static String getId()
    {
        return getId(commSeqType);
    }
    
    /**
     * デフォルトの16ビットシリアル番号yMMddHHmmss+1ビットマシンID+3長さサイクル増分文字列
     * 
     * @return シーケンス値
     */
    public static String getId(String type)
    {
        AtomicInteger atomicInt = commSeq;
        if (uploadSeqType.equals(type))
        {
            atomicInt = uploadSeq;
        }
        return getId(atomicInt, 3);
    }

    /**
     * 汎用インタフェースシリアル番号yMMddHHmmss+1ビットマシンID+length長さサイクル増分文字列
     * 
     * @param atomicInt シーケンス数
     * @param length 数値の長さ
     * @return シーケンス値
     */
    public static String getId(AtomicInteger atomicInt, int length)
    {
        String result = DateUtils.dateTimeNow();
        result += machineCode;
        result += getSeq(atomicInt, length);
        return result;
    }

    /**
     * シーケンス巡回増分文字列［1，10の（length）べき乗の次）、lengthビット数を0で左揃え
     * 
     * @return シーケンス値
     */
    private synchronized static String getSeq(AtomicInteger atomicInt, int length)
    {
        // 値を先に取ってから+1
        int value = atomicInt.getAndIncrement();

        // 更新後の値>=10の（length）べき乗の次の値を1にリセットする場合
        int maxSeq = (int) Math.pow(10, length);
        if (atomicInt.get() >= maxSeq)
        {
            atomicInt.set(1);
        }
        // 文字列を回転し、0で左揃え
        return StringUtils.padl(value, length);
    }
}
