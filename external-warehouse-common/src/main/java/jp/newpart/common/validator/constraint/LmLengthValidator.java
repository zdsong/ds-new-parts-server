package jp.newpart.common.validator.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jp.newpart.common.validator.annotation.LmLength;
import jp.newpart.common.utils.ConvertUtils;
import jp.newpart.common.utils.StringUtils;
import jp.newpart.common.utils.spring.SpringUtils;
import jp.newpart.common.validator.service.IValidatorService;

/**
 * バイト長バリデーター
 *
 * @author nextosd
 */
public class LmLengthValidator implements ConstraintValidator<LmLength, Object> {

    private LmLength lmLength;

    /**
     * 最小長さの値
     */
    private Integer min;

    /**
     * 最大長さの値
     */
    private Integer max;

    private LmLength.LengthType minMaxType;

    /**
     * 初期化
     */
    @Override
    public void initialize(LmLength lmLength) {
        this.min = lmLength.min();
        this.max = lmLength.max();
        this.minMaxType = lmLength.minMaxType();
        this.lmLength = lmLength;
    }

    /**
     * 検証
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = false;
        int valueLen = 0;

        if (StringUtils.isObjectEmpty(value)) {
            return true;
        }

        String input = ConvertUtils.toStr(value);
        valueLen = input.length();

        if (LmLength.LengthType.MIN_OR_MAX.equals(this.minMaxType)) {
            if (min == valueLen || valueLen == max) {
                isValid = true;
            }
        } else {
            if (min <= valueLen && valueLen <= max) {
                isValid = true;
            }
        }

        if (!isValid) {
            String messageString = SpringUtils.getBean(IValidatorService.class).getErrorMessage(this.lmLength.message(),
                    this.lmLength.params());
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(messageString).addConstraintViolation();
        }
        return isValid;
    }
}
