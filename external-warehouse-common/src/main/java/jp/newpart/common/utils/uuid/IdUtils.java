package jp.newpart.common.utils.uuid;

import jp.newpart.common.constant.IdPrefixConstants;
import jp.newpart.common.utils.StringUtils;

/**
 * ID生成器ユーティリティクラス
 *
 * @author nextosd
 */
public class IdUtils {
    /**
     * ランダムなUUIDを取得するためのSnowflakeIdWorkerインスタンス
     */
    private static final SnowflakeIdWorker SNOWFLAKE_ID_WORKER = new SnowflakeIdWorker();

    /**
     * Snowflakeアルゴリズムを使用してユニークなIDを取得
     *
     * @return Snowflakeアルゴリズムを使用して生成されたユニークなID
     */
    public static long nextId() {
        return SNOWFLAKE_ID_WORKER.nextId();
    }

    /**
     * Snowflakeアルゴリズムを使用してユニークなIDを取得
     *
     * @return Snowflakeアルゴリズムを使用して生成されたユニークなID
     */
    public static String nextIdStr() {
        return String.valueOf(SNOWFLAKE_ID_WORKER.nextId());
    }

    /**
     * Snowflakeアルゴリズムを使用してユニークなIDを取得
     *
     * @return Snowflakeアルゴリズムを使用して生成されたユニークなID
     */
    public static String nextIdStr(String prefix) {
        if (StringUtils.isEmpty(prefix)) {
            return IdPrefixConstants.COMMON_ID.concat(String.valueOf(SNOWFLAKE_ID_WORKER.nextId()));
        } else {
            return prefix.concat(String.valueOf(SNOWFLAKE_ID_WORKER.nextId()));
        }
    }
}
