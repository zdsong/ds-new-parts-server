package jp.nasta.pms.common.validator.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jp.nasta.pms.common.utils.ValueUtils;
import jp.nasta.pms.common.utils.spring.SpringUtils;
import jp.nasta.pms.common.validator.annotation.LmNotBlank;
import jp.nasta.pms.common.validator.service.IValidatorService;

/**
 * 空白バリデーター
 *
 * @author nextosd
 */
public class LmNotBlankValidator implements ConstraintValidator<LmNotBlank, Object> {

    private LmNotBlank lmNotBlank;

    /**
     * 初期化
     */
    @Override
    public void initialize(LmNotBlank lmNotBlank) {
        ConstraintValidator.super.initialize(lmNotBlank);
        this.lmNotBlank = lmNotBlank;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean isValid = !ValueUtils.isEmpty(value);
        if (!isValid) {
            String messageString = SpringUtils.getBean(IValidatorService.class).getErrorMessage(lmNotBlank.message(),
                    lmNotBlank.params());
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(messageString).addConstraintViolation();
        }
        return isValid;
    }
}
