package jp.newpart.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jp.newpart.common.enums.BusinessType;
import jp.newpart.common.enums.OperatorType;

/**
 * カスタム操作ログ注記
 * 
 * @author nextosd
 *
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log
{
    /**
     * モジュール
     */
    public String title() default "";

    /**
     * 機能
     */
    public BusinessType businessType() default BusinessType.OTHER;

    /**
     * オペレータカテゴリ
     */
    public OperatorType operatorType() default OperatorType.MANAGE;

    /**
     * 要求されたパラメータを保存するかどうか
     */
    public boolean isSaveRequestData() default true;

    /**
     * レスポンスのパラメータを保存するかどうか
     */
    public boolean isSaveResponseData() default true;

    /**
     * 指定された要求パラメータを除外
     */
    public String[] excludeParamNames() default {};
}
