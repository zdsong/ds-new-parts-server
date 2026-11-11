package jp.newpart.common.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jp.newpart.common.validator.constraint.LmTypeValidator;
import jp.newpart.common.validator.enums.CheckTypeEnum;

import java.lang.annotation.*;

/**
 * 文字タイプチェック
 *
 * @author nextosd
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = LmTypeValidator.class)
public @interface LmType {

    /**
     * メッセージ
     */
    String message() default "{EM004}";

    /**
     * チェックタイプ
     */
    CheckTypeEnum checkType() default CheckTypeEnum.ALPHABET_NUMERIC_SYMBOL;

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
