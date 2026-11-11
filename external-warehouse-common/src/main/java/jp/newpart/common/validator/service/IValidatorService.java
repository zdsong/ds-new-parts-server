package jp.newpart.common.validator.service;

/**
 * 検証サービスのインタフェース
 *
 * @author nextosd
 */
public interface IValidatorService {

    /**
     * エラーメッセージを取得する
     *
     * @param value
     * @param param
     * @return
     */
    String getErrorMessage(String value, String... param);

    /**
     * バイト長バリデーター
     *
     * @param value
     * @param min
     * @param max
     * @return
     */
    boolean byteLengthValidator(String value, Integer min, Integer max);

    /**
     * パスワードバリデーター
     *
     * @param value
     * @return
     */
    boolean passwordValidator(String value);
}
