package jp.newpart.common.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jp.newpart.common.validator.constraint.LmPasswordValidator;

import java.lang.annotation.*;

/**
 * passwordチェック
 *
 * @author nextosd
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = LmPasswordValidator.class)
public @interface LmPassword {

    /**
     * メッセージ
     */
    String message();

    /**
     * パラメータ
     */
    String[] params() default {};

    /**
     * グループ
     */
    Class<?>[] groups() default {};

    /**
     * ペイロード
     */
    Class<? extends Payload>[] payload() default {};

}
