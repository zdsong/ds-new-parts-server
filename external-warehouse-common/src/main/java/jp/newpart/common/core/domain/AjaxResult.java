package jp.newpart.common.core.domain;

import jp.newpart.common.constant.HttpStatus;
import jp.newpart.common.utils.MessageUtils;
import jp.newpart.common.utils.StringUtils;

import java.util.HashMap;
import java.util.Objects;

/**
 * 操作メッセージリマインダー
 *
 * @author nextosd
 */
public class AjaxResult extends HashMap<String, Object> {
    /**
     * ステータスコード
     */
    public static final String CODE_TAG = "code";
    /**
     * 戻り内容
     */
    public static final String MSG_TAG = "message";
    /**
     * データオブジェクト
     */
    public static final String DATA_TAG = "data";
    /**
     * 成功のメッセージ内容
     */
    public static final String SUCCESS_MSG = "I0001";
    /**
     * 失敗のメッセージ内容
     */
    public static final String ERROR_MSG = "NG";

    private static final long serialVersionUID = 1L;

    /**
     * 新しい AjaxResult オブジェクトを初期化し、空のメッセージを表すようにします。
     */
    public AjaxResult() {
    }

    /**
     * 新しい AjaxResult オブジェクトを初期化します
     *
     * @param code ステータスコード
     * @param msg  戻り内容
     */
    public AjaxResult(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 新しい AjaxResult オブジェクトを初期化します
     *
     * @param code ステータスコード
     * @param msg  戻り内容
     * @param data データオブジェクト
     */
    public AjaxResult(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (StringUtils.isNotNull(data)) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 成功メッセージを返します
     *
     * @return 成功メッセージ
     */
    public static AjaxResult success() {
        return AjaxResult.success(StringUtils.EMPTY);
    }

    /**
     * 成功データを返します
     *
     * @return 成功メッセージ
     */
    public static AjaxResult success(Object data) {
        return AjaxResult.success(MessageUtils.message(SUCCESS_MSG), data);
    }

    /**
     * 成功メッセージを返します
     *
     * @param msg  戻り内容
     * @param data データオブジェクト
     * @return 成功メッセージ
     */
    public static AjaxResult success(String msg, Object data) {
        return new AjaxResult(HttpStatus.SUCCESS, msg, data);
    }

    /**
     * 警告メッセージを返します
     *
     * @param msg 戻り内容
     * @return 警告メッセージ
     */
    public static AjaxResult warn(String msg) {
        return AjaxResult.warn(msg, null);
    }

    /**
     * 警告メッセージを返します
     *
     * @param msg  戻り内容
     * @param data データオブジェクト
     * @return 警告メッセージ
     */
    public static AjaxResult warn(String msg, Object data) {
        return new AjaxResult(HttpStatus.WARN, msg, data);
    }

    /**
     * エラーメッセージを返します
     *
     * @return エラーメッセージ
     */
    public static AjaxResult error() {
        return AjaxResult.error(HttpStatus.ERROR, ERROR_MSG);
    }

    /**
     * エラーメッセージを返します
     *
     * @param msg 戻り内容
     * @return エラーメッセージ
     */
    public static AjaxResult error(String msg) {
        return AjaxResult.error(HttpStatus.ERROR, msg);
    }

    /**
     * エラーメッセージを返します
     *
     * @param code ステータスコード
     * @param msg  戻り内容
     * @return エラーメッセージ
     */
    public static AjaxResult error(int code, String msg) {
        return new AjaxResult(code, msg, null);
    }

    /**
     * 成功メッセージかどうかを確認します
     *
     * @return 結果
     */
    public boolean isSuccess() {
        return Objects.equals(HttpStatus.SUCCESS, this.get(CODE_TAG));
    }

    /**
     * 警告メッセージかどうかを確認します
     *
     * @return 結果
     */
    public boolean isWarn() {
        return Objects.equals(HttpStatus.WARN, this.get(CODE_TAG));
    }

    /**
     * エラーメッセージかどうかを確認します
     *
     * @return 結果
     */
    public boolean isError() {
        return Objects.equals(HttpStatus.ERROR, this.get(CODE_TAG));
    }

    /**
     * チェーン呼び出しを便利にする
     *
     * @param key   キー
     * @param value 値
     * @return データオブジェクト
     */
    @Override
    public AjaxResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}