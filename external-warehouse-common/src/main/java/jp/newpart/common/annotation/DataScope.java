package jp.newpart.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * データ権限フィルタ注記
 * 
 * @author nextosd
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope
{

    /**
     * ユーザー・テーブルの別名
     */
    public String userAlias() default "";

    /**
     * 権限文字（複数の役割が必要な権限に一致するために使用）は、デフォルトでは権限注記@ssに基づいて取得され、複数の権限はカンマで区切られます
     */
    public String permission() default "";
}
