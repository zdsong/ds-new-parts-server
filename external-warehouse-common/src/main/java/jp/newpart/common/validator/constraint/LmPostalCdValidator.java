package jp.newpart.common.validator.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jp.newpart.common.utils.ConvertUtils;
import jp.newpart.common.utils.ValidatorUtils;
import jp.newpart.common.utils.spring.SpringUtils;
import jp.newpart.common.validator.annotation.LmPostalCd;
import jp.newpart.common.validator.service.IValidatorService;

/**
 * 郵便番号バリデーター
 *
 * @author nextosd
 */
public class LmPostalCdValidator implements ConstraintValidator<LmPostalCd, String> {

    private LmPostalCd lmPostalCd;

    /**
     * 初期化
     */
    @Override
    public void initialize(LmPostalCd lmPostalCd) {
        this.lmPostalCd = lmPostalCd;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValid = true;

        if(ValidatorUtils.isNotEmpty(value)){
            // 半角の変換
            String halfValue = ConvertUtils.toDBC(value);
            isValid = ValidatorUtils.isPostalCd(halfValue);
            if (!isValid) {
                String messageString = SpringUtils.getBean(IValidatorService.class).getErrorMessage(lmPostalCd.message(),
                        lmPostalCd.params());
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(messageString).addConstraintViolation();
            }
        }

        return isValid;
    }
}
