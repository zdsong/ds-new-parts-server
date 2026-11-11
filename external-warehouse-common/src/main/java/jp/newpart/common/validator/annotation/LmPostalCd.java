package jp.newpart.common.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jp.newpart.common.validator.constraint.LmPostalCdValidator;

import java.lang.annotation.*;

/**
 * 郵便番号をチェックする
 *
 * @author nextosd
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = LmPostalCdValidator.class)
public @interface LmPostalCd {

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
