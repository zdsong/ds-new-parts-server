package jp.newpart.common.validator.enums;

/**
 * 文字種類枚挙
 *
 * @author nextosd
 */
public enum CheckTypeEnum {
    /**
     * 英数記号
     */
    ALPHABET_NUMERIC_SYMBOL,
    /**
     * 英字（大小文字）
     */
    ALPHABET,
    /**
     * 文字数
     */
    CHAR_COUNT,
    /**
     * 並び順
     */
    ORDER_NO,
    /**
     * 英字（大文字）
     */
    ALPHABET_UPPERCASE,
    /**
     * 英字（小文字）
     */
    ALPHABET_LOWERCASE,
    /**
     * 英数字
     */
    ALPHABET_NUMERIC,
    /**
     * 数字
     */
    NUMERIC,
    /**
     * 整数
     */
    INTEGER,
    /**
     * 正の整数
     */
    POSITIVE_INTEGER,
    /**
     * 小数
     */
    DECIMAL,
    /**
     * 半角のみ
     */
    HALF_ANGLE,
    /**
     * カナ
     */
    KANA,
    /**
     * 日付
     */
    YYYYMMDD,
    /**
     * 時刻
     */
    HHMM;
}
