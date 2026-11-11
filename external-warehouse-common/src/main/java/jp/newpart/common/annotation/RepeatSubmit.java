package jp.newpart.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * カスタム注記フォームの再送信を防ぐ
 * 
 * @author nextosd
 *
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit
{
    /**
     * 間隔（ms）、それ未満は重複コミットとみなす
     */
    public int interval() default 5000;

    /**
     * ヒントメッセージ
     */
    public String message() default "繰り返しコミットは許可されていません。後で再試行してください";
}
