package jp.newpart.common.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jp.newpart.common.validator.constraint.LmMailValidator;

import java.lang.annotation.*;

/**
 * メールチェック
 *
 * @author nextosd
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = LmMailValidator.class)
public @interface LmMail {

    /**
     * メッセージ
     */
    String message() default "{EM006}";

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
