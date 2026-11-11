package jp.newpart.common.validator.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jp.newpart.common.validator.annotation.LmByteLength;
import jp.newpart.common.utils.spring.SpringUtils;
import jp.newpart.common.validator.service.IValidatorService;

/**
 * バイト長バリデーター
 *
 * @author nextosd
 */
public class LmByteLengthValidator implements ConstraintValidator<LmByteLength, String> {

    private LmByteLength lmByteLength;

    /**
     * 最小長さの値
     */
    private Integer min;

    /**
     * 最大長さの値
     */
    private Integer max;

    /**
     * 初期化
     */
    @Override
    public void initialize(LmByteLength lmByteLength) {
        this.min = lmByteLength.min();
        this.max = lmByteLength.max();
        this.lmByteLength = lmByteLength;
    }

    /**
     * 検証
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = false;
        if (SpringUtils.getBean(IValidatorService.class).byteLengthValidator(value, min, max)) {
            isValid = true;
        }
        if (!isValid) {
            String messageString = SpringUtils.getBean(IValidatorService.class).getErrorMessage(lmByteLength.message(),
                    lmByteLength.params());
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(messageString).addConstraintViolation();
        }
        return isValid;
    }
}
