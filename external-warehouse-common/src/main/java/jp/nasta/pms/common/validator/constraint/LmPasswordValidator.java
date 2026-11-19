package jp.nasta.pms.common.validator.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jp.nasta.pms.common.utils.spring.SpringUtils;
import jp.nasta.pms.common.validator.annotation.LmPassword;
import jp.nasta.pms.common.validator.service.IValidatorService;

/**
 * パスワードバリデーター
 *
 * @author nextosd
 */
public class LmPasswordValidator implements ConstraintValidator<LmPassword, String> {

    private LmPassword lmPassword;

    /**
     * 初期化
     */
    @Override
    public void initialize(LmPassword lmPassword) {
        this.lmPassword = lmPassword;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String messageString = SpringUtils.getBean(IValidatorService.class).getErrorMessage(lmPassword.message(),
                lmPassword.params());
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageString).addConstraintViolation();
        return SpringUtils.getBean(IValidatorService.class).passwordValidator(value);
    }
}
