package jp.newpart.common.validator.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jp.newpart.common.validator.annotation.LmMail;
import jp.newpart.common.utils.ValidatorUtils;
import jp.newpart.common.utils.spring.SpringUtils;
import jp.newpart.common.validator.service.IValidatorService;

/**
 * メールバリデーター
 *
 * @author nextosd
 */
public class LmMailValidator implements ConstraintValidator<LmMail, String> {

    private LmMail lmMail;

    /**
     * 初期化
     */
    @Override
    public void initialize(LmMail lmMail) {
        this.lmMail = lmMail;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        boolean isValid = true;
        isValid = ValidatorUtils.isMail(value);
        if (!isValid) {
            String messageString = SpringUtils.getBean(IValidatorService.class).getErrorMessage(lmMail.message(),
                    lmMail.params());
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(messageString).addConstraintViolation();
        }

        return isValid;
    }
}
