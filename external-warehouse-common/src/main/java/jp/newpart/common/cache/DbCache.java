package jp.newpart.common.cache;

import jp.newpart.common.utils.JsonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * データベースキャッシュコンポーネント
 * データベースを使用してキャッシュオブジェクトを保存および管理します
 */
@Component
public class DbCache {

    private static final Log logger = LogFactory.getLog(DbCache.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * キャッシュオブジェクトを設定します
     *
     * @param key   キャッシュアイテムのキー
     * @param value キャッシュアイテムの値
     */
    public <T> void setCacheObject(final String key, final Object value) {
        setCacheObject(key, value, null, null);
    }

    /**
     * キャッシュオブジェクトを設定し、有効期限を指定します
     *
     * @param key      キャッシュアイテムのキー
     * @param value    キャッシュアイテムの値
     * @param timeout  有効期限
     * @param timeUnit 有効期限の時間単位
     */
    public <T> void setCacheObject(final String key, final Object value, final Integer timeout, final TimeUnit timeUnit) {
        Timestamp expiryTime = getExpiryTime(timeout, timeUnit);
        String sql = "MERGE INTO sys_cache AS target " +
                "USING (VALUES (?, ?, ?)) AS source (cache_key, cache_value, expiry_time) " +
                "ON target.cache_key = source.cache_key " +
                "WHEN MATCHED THEN " +
                "    UPDATE SET cache_value = source.cache_value, expiry_time = source.expiry_time " +
                "WHEN NOT MATCHED THEN " +
                "    INSERT (cache_key, cache_value, expiry_time) " +
                "    VALUES (source.cache_key, source.cache_value, source.expiry_time);";
        executeUpdate(sql, key, JsonUtil.toJsonStr(value), expiryTime);
    }

    /**
     * キャッシュアイテムの有効期限を設定します
     *
     * @param key     キャッシュアイテムのキー
     * @param timeout 有効期限（秒）
     * @return 設定に成功したかどうか
     */
    public boolean expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * キャッシュアイテムの有効期限を設定します
     *
     * @param key     キャッシュアイテムのキー
     * @param timeout 有効期限
     * @param unit    有効期限の時間単位
     * @return 設定に成功したかどうか
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        Timestamp expiryTime = Timestamp.from(Instant.now().plusMillis(unit.toMillis(timeout)));
        String sql = "UPDATE sys_cache SET expiry_time = ? WHERE cache_key = ?";
        return executeUpdate(sql, expiryTime, key) > 0;
    }

    /**
     * キャッシュアイテムの残り有効期限を取得します
     *
     * @param key キャッシュアイテムのキー
     * @return 残り有効期限（ミリ秒），キャッシュが存在しない場合は-1を返します
     */
    public long getExpire(final String key) {
        String sql = "SELECT TOP 1 expiry_time FROM sys_cache WHERE cache_key = ?";
        List<Timestamp> expiryTimes = queryForList(sql, new Object[]{key}, Timestamp.class);
        if (expiryTimes.isEmpty() || expiryTimes.get(0) == null) {
            return -1; // キャッシュが存在しません
        }
        return expiryTimes.get(0).getTime() - System.currentTimeMillis();
    }

    /**
     * キャッシュアイテムが存在するかどうかをチェックします
     *
     * @param key キャッシュアイテムのキー
     * @return キャッシュアイテムが存在するかどうか
     */
    public Boolean hasKey(String key) {
        String sql = "SELECT COUNT(*) AS cnt FROM sys_cache WHERE cache_key = ? AND expiry_time >= ?";
        return queryForObject(sql, new Object[]{key, Timestamp.from(Instant.now())}, Integer.class) > 0;
    }

    /**
     * キャッシュオブジェクトを取得します
     *
     * @param key   キャッシュアイテムのキー
     * @param clazz キャッシュアイテムのクラスタイプ
     * @return キャッシュオブジェクト、存在しない場合はnullを返します
     */
    public <T> T getCacheObject(final String key, Class<T> clazz) {
        String sql = "SELECT cache_value FROM sys_cache WHERE cache_key = ? AND expiry_time >= ? limit 1";
        List<String> values = queryForList(sql, new Object[]{key, Timestamp.from(Instant.now())}, String.class);
        return values.isEmpty() ? null : JsonUtil.parseObject(values.get(0), clazz);
    }

    /**
     * キャッシュアイテムを削除します
     *
     * @param key キャッシュアイテムのキー
     * @return 削除に成功したかどうか
     */
    public boolean deleteObject(final String key) {
        String sql = "DELETE FROM sys_cache WHERE cache_key = ?";
        return executeUpdate(sql, key) > 0;
    }

    /**
     * 複数のキャッシュアイテムを削除します
     *
     * @param collection キャッシュアイテムのキーのコレクション
     * @return 削除に成功したかどうか
     */
    public boolean deleteObject(final Collection<String> collection) {
        if (collection.isEmpty()) {
            // 空集合は削除成功とみなします
            return true;
        }
        String sql = "DELETE FROM sys_cache WHERE cache_key IN (" + String.join(",", Collections.nCopies(collection.size(), "?")) + ")";
        return executeUpdate(sql, collection.toArray()) > 0;
    }

    /**
     * 指定されたパターンに一致するキャッシュアイテムのキーのコレクションを取得します
     *
     * @param pattern 文字列プレフィックスパターン
     * @return キャッシュアイテムのキーのコレクション
     */
    public Collection<String> keys(final String pattern) {
        String sql = "SELECT cache_key FROM sys_cache WHERE cache_key LIKE ?";
        return queryForList(sql, new Object[]{pattern.concat("%")}, String.class);
    }

    /**
     * キャッシュアイテムの最大有効期限を取得します
     *
     * @return 最大有効期限
     */
    private Timestamp getMaxExpiryTime() {
        // 最大有効期限を設定化
        return Timestamp.from(Instant.now().plusMillis(TimeUnit.DAYS.toMillis(365 * 80)));
    }

    /**
     * 有効期限と時間単位に基づいて有効期限時間を計算します
     *
     * @param timeout  有効期限
     * @param timeUnit 有効期限の時間単位
     * @return 有効期限時間
     */
    private Timestamp getExpiryTime(Integer timeout, TimeUnit timeUnit) {
        if (timeout != null && timeUnit != null) {
            return Timestamp.from(Instant.now().plusMillis(timeUnit.toMillis(timeout)));
        }
        return getMaxExpiryTime();
    }

    /**
     * SQL更新を実行します
     *
     * @param sql  SQL文
     * @param args パラメータ
     * @return 影響を受けた行数
     */
    private int executeUpdate(String sql, Object... args) {
        try {
            return jdbcTemplate.update(sql, args);
        } catch (Exception e) {
            logger.error("SQL更新の実行に失敗しました: " + sql, e);
            throw new RuntimeException("SQL更新の実行に失敗しました", e);
        }
    }

    /**
     * リストを照会します
     *
     * @param sql          SQL文
     * @param args         パラメータ
     * @param requiredType 戻り値の型
     * @param <T>          泛型型
     * @return 照会結果リスト
     */
    private <T> List<T> queryForList(String sql, Object[] args, Class<T> requiredType) {
        try {
            return jdbcTemplate.queryForList(sql, args, requiredType);
        } catch (Exception e) {
            logger.error("SQL照会の実行に失敗しました: " + sql, e);
            throw new RuntimeException("SQL照会の実行に失敗しました", e);
        }
    }

    /**
     * 単一のオブジェクトを照会します
     *
     * @param sql          SQL文
     * @param args         パラメータ
     * @param requiredType 戻り値の型
     * @param <T>          泛型型
     * @return 照会結果
     */
    private <T> T queryForObject(String sql, Object[] args, Class<T> requiredType) {
        try {
            return jdbcTemplate.queryForObject(sql, args, requiredType);
        } catch (Exception e) {
            logger.error("SQL照会の実行に失敗しました: " + sql, e);
            throw new RuntimeException("SQL照会の実行に失敗しました", e);
        }
    }
}
