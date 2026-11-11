package jp.newpart.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jp.newpart.common.constant.CacheConstants;
import jp.newpart.common.enums.LimitType;

/**
 * 限流注記
 * 
 * @author nextosd
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter
{
    /**
     * 限流key
     */
    public String key() default CacheConstants.RATE_LIMIT_KEY;

    /**
     * 制限時間、単位秒
     */
    public int time() default 60;

    /**
     * 限流回数
     */
    public int count() default 100;

    /**
     * 限流タイプ
     */
    public LimitType limitType() default LimitType.DEFAULT;
}
