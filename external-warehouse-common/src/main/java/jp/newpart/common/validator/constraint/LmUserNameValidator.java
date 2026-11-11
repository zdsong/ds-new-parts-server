package jp.newpart.common.validator.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jp.newpart.common.utils.ValidatorUtils;
import jp.newpart.common.utils.spring.SpringUtils;
import jp.newpart.common.validator.annotation.LmUserName;
import jp.newpart.common.validator.service.IValidatorService;

/**
 * 氏名バリデーター
 *
 * @author nextosd
 */
public class LmUserNameValidator implements ConstraintValidator<LmUserName, String> {

    private LmUserName lmUserName;

    /**
     * 初期化
     */
    @Override
    public void initialize(LmUserName lmUserName) {
        this.lmUserName = lmUserName;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        boolean isValid;
        isValid = ValidatorUtils.isMiddleSpace(value);
        if (!isValid) {
            String messageString = SpringUtils.getBean(IValidatorService.class).getErrorMessage(lmUserName.message(),
                    lmUserName.params());
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(messageString).addConstraintViolation();
        }

        return isValid;
    }
}
