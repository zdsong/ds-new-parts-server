package jp.newpart.common.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jp.newpart.common.validator.constraint.LmByteLengthValidator;

import java.lang.annotation.*;

/**
 * byteLengthチェック
 *
 * @author nextosd
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = LmByteLengthValidator.class)
public @interface LmByteLength {

    /**
     * 最小長さの値
     */
    int min() default 0;

    /**
     * 最大長さの値
     */
    int max() default Integer.MAX_VALUE;

    /**
     * メッセージ
     */
    String message() default "{E002}";

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
