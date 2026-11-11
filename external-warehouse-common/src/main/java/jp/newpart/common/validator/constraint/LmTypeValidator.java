package jp.newpart.common.validator.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jp.newpart.common.utils.StringUtils;
import jp.newpart.common.utils.ValidatorUtils;
import jp.newpart.common.utils.spring.SpringUtils;
import jp.newpart.common.validator.annotation.LmType;
import jp.newpart.common.validator.service.IValidatorService;

/**
 * 文字タイプチェック
 *
 * @author nextosd
 */
public class LmTypeValidator implements ConstraintValidator<LmType, Object> {

    private LmType lmType;

    /**
     * 初期化
     */
    @Override
    public void initialize(LmType lmType) {
        this.lmType = lmType;
    }

    /**
     * Implements the validation logic.
     * The state of {@code value} must not be altered.
     * <p>
     * This method can be accessed concurrently, thread-safety must be ensured
     * by the implementation.
     *
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return {@code false} if {@code value} does not pass the constraint
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean isValid = true;
        if (StringUtils.isNotNull(value)) {
            String checkValue = String.valueOf(value);
            if (StringUtils.isNotEmpty(checkValue)) {
                String defaultMsgCode = lmType.message();
                switch (lmType.checkType()) {
                    case ALPHABET_NUMERIC_SYMBOL:
                        // 英数字記号のみで入力してください。
                        isValid = ValidatorUtils.isAlphabetNumericSymbol(StringUtils.trimLeftBlank(checkValue));
                        break;
                    case ALPHABET_NUMERIC:
                        // 英数字のみで入力してください。
                        isValid = ValidatorUtils.isAlphabetNumeric(StringUtils.trimLeftBlank(checkValue));
                        break;
                    case CHAR_COUNT:
                        // 文字数は1～64で入力してください。
                        isValid = ValidatorUtils.isCharCount(StringUtils.trimLeftBlank(checkValue));
                        defaultMsgCode = "{EM001}";
                        break;
                    case ORDER_NO:
                        // 並び順は1～10で入力してください。
                        isValid = ValidatorUtils.isOrderNo(StringUtils.trimLeftBlank(checkValue));
                        defaultMsgCode = "{EM002}";
                        break;

                    default:
                        break;
                }
                if (!isValid) {
                    String messageString = SpringUtils.getBean(IValidatorService.class).getErrorMessage(defaultMsgCode,
                            lmType.params());
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(messageString).addConstraintViolation();
                }
            }
        }
        return isValid;
    }
}
