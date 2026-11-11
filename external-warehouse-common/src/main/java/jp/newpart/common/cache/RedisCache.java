package jp.newpart.common.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * spring redis
 *
 * @author nextosd
 **/
@SuppressWarnings(value = { "unchecked", "rawtypes" })
@Component
@ConditionalOnBean(RedisTemplate.class)
public class RedisCache {

    @Autowired
    public RedisTemplate redisTemplate;

    /**
     * 基本的なオブジェクトをキャッシュし、Integer、String、実体類等
     *
     * @param key キャッシュされたキー値
     * @param value キャッシュされた値
     */
    public <T> void setCacheObject(final String key, final T value)
    {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 基本的なオブジェクトをキャッシュし、Integer、String、実体類等
     *
     * @param key キャッシュされたキー値
     * @param value キャッシュされた値
     * @param timeout 時間
     * @param timeUnit 時間単位
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit)
    {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 有効時間の設定
     *
     * @param key Redis キー
     * @param timeout タイムアウト時間
     * @return true=OK；false=失敗
     */
    public boolean expire(final String key, final long timeout)
    {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 有効時間の設定
     *
     * @param key Redis キー
     * @param timeout タイムアウト時間
     * @param unit 時間単位
     * @return true=OK；false=失敗
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit)
    {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 有効時間の取得
     *
     * @param key Redis キー
     * @return 有効時間
     */
    public long getExpire(final String key)
    {
        return redisTemplate.getExpire(key);
    }

    /**
     * keyが存在するかどうかを判断する
     *
     * @param key キー
     * @return true：存在 / false：存在なし
     */
    public Boolean hasKey(String key)
    {
        return redisTemplate.hasKey(key);
    }

    /**
     * キャッシュされた基本オブジェクトを取得します。
     *
     * @param key キャッシュキー値
     * @return キャッシュキー値に対応するデータ
     */
    public <T> T getCacheObject(final String key)
    {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * オブジェクトの削除
     *
     * @param key
     */
    public boolean deleteObject(final String key)
    {
        return redisTemplate.delete(key);
    }

    /**
     * アセンブリオブジェクトの削除
     *
     * @param collection ブジェクト
     * @return
     */
    public boolean deleteObject(final Collection collection)
    {
        return redisTemplate.delete(collection) > 0;
    }

    /**
     * キャッシュマップ
     *
     * @param key
     * @param dataMap
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap)
    {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * キャッシュマップ
     *
     * @param key キャッシュキー値
     * @return
     */
    public <T> Map<String, T> getCacheMap(final String key)
    {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * Hashへのデータ
     *
     * @param key キャッシュキー値
     * @param hKey Hashキー
     * @param value 値
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value)
    {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * Hashのデータを取得する
     *
     * @param key キャッシュキー値
     * @param hKey Hashキー
     * @return
     */
    public <T> T getCacheMapValue(final String key, final String hKey)
    {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * 複数のHashのデータを取得する
     *
     * @param key キャッシュキー値
     * @param hKeys Hashキー
     * @return
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys)
    {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * Hashからデータを削除
     *
     * @param key キャッシュキー値
     * @param hKey Hashキー
     * @return
     */
    public boolean deleteCacheMapValue(final String key, final String hKey)
    {
        return redisTemplate.opsForHash().delete(key, hKey) > 0;
    }

    /**
     * キャッシュされた基本オブジェクトのリストを取得する
     *
     * @param pattern 文字列接頭辞
     * @return
     */
    public Collection<String> keys(final String pattern)
    {
        return redisTemplate.keys(pattern);
    }
}
