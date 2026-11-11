package jp.newpart.common.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jp.newpart.common.validator.constraint.LmLengthValidator;

import java.lang.annotation.*;

/**
 * byteLengthチェック
 *
 * @author nextosd
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = LmLengthValidator.class)
public @interface LmLength {

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
    String message() default "{EM005}";

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

    /**
     * 最大・最小サイズタイプ
     */
    LengthType minMaxType() default LengthType.MIN_AND_MAX;

    enum LengthType {
        /**
         * 最小～最大のサイズの間にOK、以外NG
         */
        MIN_AND_MAX(0),
        /**
         * 最小または、最大のサイズはOK、以外NG
         */
        MIN_OR_MAX(1);
        private final int value;

        LengthType(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }
}
