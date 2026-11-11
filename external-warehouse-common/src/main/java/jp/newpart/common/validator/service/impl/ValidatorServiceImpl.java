package jp.newpart.common.validator.service.impl;

import jp.newpart.common.utils.MessageUtils;
import jp.newpart.common.utils.StringUtils;
import org.springframework.stereotype.Service;

import jp.newpart.common.validator.service.IValidatorService;

import java.util.ArrayList;
import java.util.List;

/**
 * 検証サービス実現
 *
 * @author nextosd
 */
@Service
public class ValidatorServiceImpl implements IValidatorService {

    /**
     * エラーメッセージを取得する
     *
     * @param value
     * @param param
     * @return
     */
    @Override
    public String getErrorMessage(String value, String... param) {
        String messageString = value;
        if (isMessageCode(value)) {
            List<String> tmpParam = new ArrayList<>();
            messageString = getMessageCode(value);
            for (Object item : param) {
                if (item instanceof String && isMessageCode(String.valueOf(item))) {
                    tmpParam.add(MessageUtils.message(getMessageLabelCode(String.valueOf(item))));
                } else {
                    tmpParam.add(String.valueOf(item));
                }
            }
            messageString = MessageUtils.message(messageString, (Object[]) tmpParam.toArray(new String[0]));
        }
        return messageString;
    }


    /**
     * バイト長バリデーター
     *
     * @param value
     * @param min
     * @param max
     * @return
     */
    @Override
    public boolean byteLengthValidator(String value, Integer min, Integer max) {
        Integer sum = 0;
        int[] asds = StringUtils.toCodePoints(value);
        for (int asd : asds) {
            if ((asd >= 0) && (asd <= 255)) {
                sum++;
            } else {
                sum = sum + 2;
            }
        }
        return min <= sum && sum <= max;
    }

    /**
     * パスワードバリデーター
     *
     * @param value 入力値
     * @return 結果
     */
    @Override
    public boolean passwordValidator(String value) {
        if (StringUtils.isNotEmpty(value)) {
            String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[#?!@$%^&*-])[A-Za-z\\d#?!@$%^&*-]{8,20}$";

            return value.matches(regex);
        }
        return true;
    }

    /**
     * パラメータが多言語コードかどうかを判断
     *
     * @param code チェックするパラメータ
     * @return 多言語コードであればtrue
     */
    private boolean isMessageCode(String code) {
        return StringUtils.startsWith(code, StringUtils.BRACES_START)
                && StringUtils.endsWith(code, StringUtils.BRACES_END);
    }

    /**
     * パラメータから多言語コードを取得
     *
     * @param param 多言語コードを含むパラメータ
     * @return 多言語コード
     */
    private String getMessageCode(String param) {
        return StringUtils.replaceEach(param, new String[]{StringUtils.BRACES_START, StringUtils.BRACES_END},
                new String[]{StringUtils.EMPTY, StringUtils.EMPTY});
    }

    /**
     * パラメータから多言語コードを取得
     *
     * @param param 多言語コードを含むパラメータ
     * @return 多言語コード
     */
    private String getMessageLabelCode(String param) {
        return "label." + StringUtils.replaceEach(param, new String[]{StringUtils.BRACES_START, StringUtils.BRACES_END},
                new String[]{StringUtils.EMPTY, StringUtils.EMPTY});
    }
}
