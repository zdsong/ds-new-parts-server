package jp.newpart.common.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jp.newpart.common.validator.constraint.LmNotBlankValidator;

import java.lang.annotation.*;

/**
 * 空白チェック
 *
 * @author nextosd
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@Documented
@Constraint(validatedBy = LmNotBlankValidator.class)
public @interface LmNotBlank {

    /**
     * メッセージ
     */
    String message() default "{EM003}";

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
