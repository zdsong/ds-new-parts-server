package jp.newpart.common.xss;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * カスタムxssチェック注記
 * 
 * @author nextosd
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
@Constraint(validatedBy = { XssValidator.class })
public @interface Xss
{
    String message()

    default "スクリプト実行を許可しない";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
