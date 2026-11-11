package jp.newpart.common.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jp.newpart.common.validator.constraint.LmUserNameValidator;

import java.lang.annotation.*;

/**
 * 氏名チェック
 * 　入力された名前にスペースが無い場合はエラーEA35表示します
 *
 * @author nextosd
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = LmUserNameValidator.class)
public @interface LmUserName {

    /**
     * メッセージ
     */
    String message() default "{EA35}";

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
