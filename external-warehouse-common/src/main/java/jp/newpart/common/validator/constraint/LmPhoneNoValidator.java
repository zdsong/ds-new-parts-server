package jp.newpart.common.validator.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jp.newpart.common.validator.annotation.LmPhoneNo;
import jp.newpart.common.utils.ConvertUtils;
import jp.newpart.common.utils.ValidatorUtils;
import jp.newpart.common.utils.spring.SpringUtils;
import jp.newpart.common.validator.service.IValidatorService;

/**
 * 電話番号バリデーター
 *
 * @author nextosd
 */
public class LmPhoneNoValidator implements ConstraintValidator<LmPhoneNo, String> {

    private LmPhoneNo lmPhoneNo;

    /**
     * 初期化
     */
    @Override
    public void initialize(LmPhoneNo lmPhoneNo) {
        this.lmPhoneNo = lmPhoneNo;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValid = true;
        if(ValidatorUtils.isEmpty(value)){
            return true;
        }
        // 半角の変換
        String halfValue = ConvertUtils.toDBC(value);
        isValid = ValidatorUtils.isPhoneNo(halfValue);
        if (!isValid) {
            String messageString = SpringUtils.getBean(IValidatorService.class).getErrorMessage(lmPhoneNo.message(),
                    lmPhoneNo.params());
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(messageString).addConstraintViolation();
        }

        return isValid;
    }
}
