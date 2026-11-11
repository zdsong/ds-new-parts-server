package jp.newpart.common.cache;

import jp.newpart.common.config.AppConfig;
import jp.newpart.common.constant.CacheConstants;
import jp.newpart.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * App キャッシュコンポーネント
 * キャッシュオブジェクトを保存および管理します
 */
@Component
public class AppCache {

    @Autowired(required = false)
    private RedisCache redisCache;

    @Autowired
    private DbCache dbCache;

    @Autowired
    private AppConfig appConfig;

    /**
     * 実際のキャッシュ実装を取得する。
     * cache=db または RedisCache が利用できない場合は DbCache を返す。
     */
    private RedisCache getRedisCache()
    {
        // RedisCache Beanが存在しない場合はnullを返す（呼び出し元でdbCacheにフォールバック）
        return redisCache;
    }

    /**
     * 基本的なオブジェクトをキャッシュし、Integer、String、実体類等
     *
     * @param key キャッシュされたキー値
     * @param value キャッシュされた値
     */
    public <T> void setCacheObject(final String key, final T value)
    {
        RedisCache rc = getRedisCache();
        if (rc == null || StringUtils.equals(appConfig.getCache(), CacheConstants.CACHE_TOOL_DB)) {
            dbCache.setCacheObject(key, value);
        } else {
            rc.setCacheObject(key, value);
        }
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
        RedisCache rc = getRedisCache();
        if (rc == null || StringUtils.equals(appConfig.getCache(), CacheConstants.CACHE_TOOL_DB)) {
            dbCache.setCacheObject(key, value, timeout, timeUnit);
        } else {
            rc.setCacheObject(key, value, timeout, timeUnit);
        }
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
        RedisCache rc = getRedisCache();
        if (rc == null || StringUtils.equals(appConfig.getCache(), CacheConstants.CACHE_TOOL_DB)) {
            return dbCache.expire(key, timeout);
        } else {
            return rc.expire(key, timeout);
        }
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
        RedisCache rc = getRedisCache();
        if (rc == null || StringUtils.equals(appConfig.getCache(), CacheConstants.CACHE_TOOL_DB)) {
            return dbCache.expire(key, timeout, unit);
        } else {
            return rc.expire(key, timeout, unit);
        }
    }

    /**
     * 有効時間の取得
     *
     * @param key Redis キー
     * @return 有効時間
     */
    public long getExpire(final String key) {
        RedisCache rc = getRedisCache();
        if (rc == null || StringUtils.equals(appConfig.getCache(), CacheConstants.CACHE_TOOL_DB)) {
            return dbCache.getExpire(key);
        } else {
            return rc.getExpire(key);
        }
    }

    /**
     * keyが存在するかどうかを判断する
     *
     * @param key キー
     * @return true：存在 / false：存在なし
     */
    public Boolean hasKey(String key)
    {
        RedisCache rc = getRedisCache();
        if (rc == null || StringUtils.equals(appConfig.getCache(), CacheConstants.CACHE_TOOL_DB)) {
            return dbCache.hasKey(key);
        } else {
            return rc.hasKey(key);
        }
    }

    /**
     * キャッシュされた基本オブジェクトを取得します。
     *
     * @param key キャッシュキー値
     * @return キャッシュキー値に対応するデータ
     */
    public <T> T getCacheObject(final String key, Class<T> clazz)
    {
        RedisCache rc = getRedisCache();
        if (rc == null || StringUtils.equals(appConfig.getCache(), CacheConstants.CACHE_TOOL_DB)) {
            return dbCache.getCacheObject(key, clazz);
        } else {
            return rc.getCacheObject(key);
        }
    }

    /**
     * オブジェクトの削除
     *
     * @param key
     */
    public boolean deleteObject(final String key)
    {
        RedisCache rc = getRedisCache();
        if (rc == null || StringUtils.equals(appConfig.getCache(), CacheConstants.CACHE_TOOL_DB)) {
            return dbCache.deleteObject(key);
        } else {
            return rc.deleteObject(key);
        }
    }

    /**
     * アセンブリオブジェクトの削除
     *
     * @param collection ブジェクト
     * @return
     */
    public boolean deleteObject(final Collection collection)
    {
        RedisCache rc = getRedisCache();
        if (rc == null || StringUtils.equals(appConfig.getCache(), CacheConstants.CACHE_TOOL_DB)) {
            return dbCache.deleteObject(collection);
        } else {
            return rc.deleteObject(collection);
        }
    }

    /**
     * キャッシュされた基本オブジェクトのリストを取得する
     *
     * @param pattern 文字列接頭辞
     * @return
     */
    public Collection<String> keys(final String pattern)
    {
        RedisCache rc = getRedisCache();
        if (rc == null || StringUtils.equals(appConfig.getCache(), CacheConstants.CACHE_TOOL_DB)) {
            return dbCache.keys(pattern);
        } else {
            return rc.keys(pattern);
        }
    }
}
