package jp.newpart.common.utils;

import java.util.regex.Pattern;

/**
 * 文字列ツールクラス
 *
 * @author nextosd
 */
public class ValidatorUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 英数字記号
     */
    private static final Pattern PATTERN_ALPHABET_NUMERIC_SYMBOL = Pattern.compile("^[a-zA-Z0-9\\-_.,@#$%^&*()+={}\\[\\]|\\\\:;\"'<>/?~!]+$");

    /**
     * 記号
     */
    private static final Pattern PATTERN_SYMBOL = Pattern.compile("^[\\-_.,@#$%^&*()+={}\\[\\]|\\\\:;\"'<>/?~!]+$");

    /**
     * 英数字
     */
    private static final Pattern PATTERN_ALPHABET_NUMERIC = Pattern.compile("^[a-zA-Z0-9]+$");
    /**
     * 英字（大小文字）
     */
    private static final Pattern PATTERN_ALPHABET = Pattern.compile("^[a-zA-Z]+$");
    /**
     * 英字（大文字）
     */
    private static final Pattern PATTERN_ALPHABET_UPPERCASE = Pattern.compile("^[A-Z]+$");
    /**
     * 英字（小文字）
     */
    private static final Pattern PATTERN_ALPHABET_LOWERCASE = Pattern.compile("^[a-z]+$");

    /**
     * 半角カタカナ
     */
    private static final Pattern PATTERN_KANA_HALF = Pattern.compile("^[\uFF65-\uFF9F]+$");
    /**
     * 全角カタカナ
     */
    private static final Pattern PATTERN_KANA = Pattern.compile("^[\u30a0-\u30ff]+$");
    /**
     * ひらがな
     */
    private static final Pattern PATTERN_HIRAGANA = Pattern.compile("^[\u3040-\u309F]+$");
    /**
     * 数値（整数）
     */
    private static final Pattern PATTERN_NUMERIC = Pattern.compile("^[0-9]+$|-[0-9]+$");
    /**
     * 数値（整数） マイナスを除く
     */
    private static final Pattern PATTERN_PLUS_NUMBER = Pattern.compile("^[0-9]+$");
    /**
     * 数値（正の整数）
     */
    private static final Pattern PATTERN_POSITIVE_INTEGER = Pattern.compile("^([1-9][0-9]*)$");
    /**
     * 数値（整数）
     */
    private static final Pattern PATTERN_DECIMAL = Pattern.compile("^([1-9]\\d*|0)(\\.\\d+)?$|^(-[1-9]\\d*|0)(\\.\\d+)?$");
    /**
     * 半角のみ
     */
    private static final Pattern PATTERN_HALF_ANGLE = Pattern.compile("^[\\x01-\\x7E\\xA1-\\xDF]+$");
    /**
     * メール
     */
    private static final Pattern PATTERN_MAIL = Pattern.compile("^[0-9a-zA-Z_!#$%&'*+\\-/=?^`{}~]+(\\.[0-9a-zA-Z_!#$%&'*+\\-/=?^`{}~]+)*@[0-9a-zA-Z-]+(\\.[0-9a-zA-Z-]+)+$");
    /**
     * 郵便番号 TODO適切な郵便番号の形式？？？？
     */
    private static final Pattern PATTERN_POSTAL_CD = Pattern.compile("^\\d{3}-?\\d{4}$");
    /**
     * 電話番号 TODO適切な電話番号形式？？？？？
     */
    private static final Pattern PATTERN_PHONE_NO = Pattern.compile("^[0-9]{1,13}$");
    /**
     * 時分
     */
    private static final Pattern PATTERN_HOUR_MINUTE = Pattern.compile("^([0-1]{1}[0-9]{1}[0-5]{1}[0-9]{1})$|^([2]{1}[0-3]{1}[0-5]{1}[0-9]{1})$|^([0-1]{1}[0-9]{1}[:]{1}[0-5]{1}[0-9]{1})$|^([2]{1}[0-3]{1}[:]{1}[0-5]{1}[0-9]{1})$");

    /**
     * 数字１～９９９
     */
    private static final Pattern PATTERN_INTEGER_999 = Pattern.compile("^[1-9][0-9]{0,2}$");

    /**
     * 数字１～９
     */
    private static final Pattern PATTERN_INTEGER_9 = Pattern.compile("^[1-9]$");

    /**
     * 数字１～64
     */
    private static final Pattern PATTERN_INTEGER_1_64 = Pattern.compile("^(?:[1-9]|[1-5][0-9]|6[0-4])$");

    /**
     * 数字１～10
     */
    private static final Pattern PATTERN_INTEGER_10 = Pattern.compile("^([1-9]|10)$");

    /**
     * 文字種010～014
     */
    private static final Pattern PATTERN_CHAR_TYPE = Pattern.compile("^01[0-4]$");

    /**
     * 入力要否010～020
     */
    private static final Pattern PATTERN_IS_INPUT = Pattern.compile("^0[12]0$");

    /**
     * 英数字記号を検証する
     *
     * @param value 検証対象の値
     * @return 結果（true：英数字、false：英数字ではない）
     */
    public static boolean isAlphabetNumericSymbol(String value) {
        boolean result = false;

        if (isNotEmpty(value)) {
            result = PATTERN_ALPHABET_NUMERIC_SYMBOL.matcher(value).matches();
        }

        return result;
    }

    /**
     * 記号を検証する
     *
     * @param value 検証対象の値
     * @return 結果（true：英数字、false：英数字ではない）
     */
    public static boolean isHalfSymbol(String value) {
        boolean result = false;

        if (isNotEmpty(value)) {
            result = PATTERN_SYMBOL.matcher(value).matches();
        }

        return result;
    }

    /**
     * 英数字を検証する
     *
     * @param value 検証対象の値
     * @return 結果（true：英数字、false：英数字ではない）
     */
    public static boolean isAlphabetNumeric(String value) {
        boolean result = false;

        if (isNotEmpty(value)) {
            result = PATTERN_ALPHABET_NUMERIC.matcher(value).matches();
        }

        return result;
    }

    /**
     * 数字1-64を検証する
     *
     * @param value 検証対象の値
     * @return 結果（true：OK、false：NG）
     */
    public static boolean isCharCount(String value) {
        boolean result = false;

        if (isNotEmpty(value)) {
            result = PATTERN_INTEGER_1_64.matcher(value).matches();
        }

        return result;
    }

    /**
     * 数字1-10を検証する
     *
     * @param value 検証対象の値
     * @return 結果（true：OK、false：NG）
     */
    public static boolean isOrderNo(String value) {
        boolean result = false;

        if (isNotEmpty(value)) {
            result = PATTERN_INTEGER_10.matcher(value).matches();
        }

        return result;
    }

    /**
     * 文字種を検証する
     *
     * @param value 検証対象の値
     * @return 結果（true：OK、false：NG）
     */
    public static boolean isCharType(String value) {
        boolean result = false;

        if (isNotEmpty(value)) {
            result = PATTERN_CHAR_TYPE.matcher(value).matches();
        }

        return result;
    }

    /**
     * 入力要否を検証する
     *
     * @param value 検証対象の値
     * @return 結果（true：OK、false：NG）
     */
    public static boolean isInput(String value) {
        boolean result = false;

        if (isNotEmpty(value)) {
            result = PATTERN_IS_INPUT.matcher(value).matches();
        }

        return result;
    }

    /**
     * 英字（大小文字）を検証する
     *
     * @param value 対象値
     * @return 結果（true：英字（大小文字）、false：英字（大小文字）ではない）
     */
    public static boolean isAlphabet(String value) {
        boolean result = false;

        if (isNotEmpty(value)) {
            result = PATTERN_ALPHABET.matcher(value).matches();
        }

        return result;
    }

    /**
     * 英字（大文字）を検証する
     *
     * @param value 対象値
     * @return 結果（true：英字、false：英字ではない）
     */
    public static boolean isAlphabetUpperCase(String value) {
        boolean result = false;

        if (isNotEmpty(value)) {
            result = PATTERN_ALPHABET_UPPERCASE.matcher(value).matches();
        }

        return result;
    }

    /**
     * 英字（小文字）を検証する
     *
     * @param value 対象値
     * @return 結果（true：英字、false：英字ではない）
     */
    public static boolean isAlphabetLowerCase(String value) {
        boolean result = false;

        if (isNotEmpty(value)) {
            result = PATTERN_ALPHABET_LOWERCASE.matcher(value).matches();
        }

        return result;
    }

    /**
     * 適切なメールの形式を検証する
     *
     * @param value 対象値
     * @return 結果（true：適切な形式、false：不正な形式）
     */
    public static Boolean isMail(String value) {
        boolean result = true;

        if (isNotEmpty(value)) {
            result = PATTERN_MAIL.matcher(value).matches();
        }

        return result;
    }

    /**
     * 半角スペースがあるか検証する
     *
     * @param value 対象値
     * @return 結果（true：適切な形式、false：不正な形式）
     */
    public static Boolean isMiddleSpace(String value) {
        boolean result = false;

        if (isNotEmpty(value)) {
            result = StringUtils.trimBlank(value).contains(" ");
        }

        return result;
    }

    /**
     * 半角カタカナを検証する
     *
     * @param value 対象値
     * @return 結果（true：半角カタカナ、false：半角カタカナではない）
     */
    public static boolean isKanaHalf(String value) {
        boolean result = false;

        if (isNotEmpty(value)) {
            result = PATTERN_KANA_HALF.matcher(value).matches();
        }

        return result;
    }

    /**
     * 全角カタカナを検証する
     *
     * @param value 対象値
     * @return 結果（true：全角カタカナ、false：全角カタカナではない）
     */
    public static boolean isKana(String value) {
        boolean result = false;

        if (isNotEmpty(value)) {
            result = PATTERN_KANA.matcher(value).matches();
        }

        return result;
    }

    /**
     * ひらがなを検証する
     *
     * @param value 対象値
     * @return 結果（true：ひらがな、false：ひらがなではない）
     */
    public static boolean isHiragana(String value) {
        boolean result = false;

        if (isNotEmpty(value)) {
            result = PATTERN_HIRAGANA.matcher(value).matches();
        }

        return result;
    }

    /**
     * 数値（整数）を検証する
     *
     * @param value 対象値
     * @return 結果（true：数値、false：数値ではない）
     */
    public static boolean isNumber(String value) {
        boolean result = false;

        if (isNotEmpty(value)) {
            result = PATTERN_NUMERIC.matcher(value).matches();
        }

        return result;
    }

    /**
     * 数値（整数）チェック マイナスを除く
     *
     * @param value 対象値
     * @return 結果（true：数値、false：数値ではない）
     */
    public static boolean isPlusNumber(String value) {
        boolean result = false;

        if (isNotEmpty(value)) {
            result = PATTERN_PLUS_NUMBER.matcher(value).matches();
        }

        return result;
    }

    /**
     * 数値（正の整数）を検証する
     *
     * @param value 対象値
     * @return 結果（true：正の整数、false：正の整数ではない）
     */
    public static boolean isPositiveInteger(String value) {
        boolean result = false;

        if (isNotEmpty(value)) {
            result = PATTERN_POSITIVE_INTEGER.matcher(value).matches();
        }

        return result;
    }

    /**
     * 数値（整数または小数）を検証する
     *
     * @param value 対象値
     * @return 結果（true：数値、false：数値ではない）
     */
    public static boolean isDecimal(String value) {
        boolean result = false;

        if (isNotEmpty(value)) {
            result = PATTERN_DECIMAL.matcher(value).matches();
        }

        return result;
    }

    /**
     * 文字列は半角のみ検証する
     *
     * @param value 対象値
     * @return 結果（true：半角、false：半角ではない）
     */
    public static Boolean isHalfAngle(String value) {
        boolean result = false;

        if (isNotEmpty(value)) {
            result = ValidatorUtils.isHalfSign(value);
        }

        return result;
    }

    /**
     * 適切な電話番号を検証する
     *
     * @param value 対象値
     * @return 結果（true：適切な形式、false：不正な形式）
     */
    public static Boolean isPhoneNo(String value) {
        boolean result = true;

        if (isNotEmpty(value)) {
            result = PATTERN_PHONE_NO.matcher(value).matches() ;;
        }

        return result;
    }

    /**
     * 半角文字を検証する
     *
     * @param value 対象値
     * @return true:半角、false:全角
     */
    public static boolean isHalfSign(String value) {

        boolean result = true;
        char c;
        if (isNotEmpty(value)) {
            for (int i = 0; i < value.length(); i++) {
                c = value.charAt(i);
                // 半角
                if (isDbcChar(c)) {
                    result = true;
                    // 全角
                } else {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    private static boolean isDbcChar(char c) {
        // 基本のなアルファベット（キーボードに表示される、スペース、数字、文字、記号）
        if (c >= 32 && c <= 127) {
            return true;
        }
        // 日本語の半角カタカナと記号
        else if (c >= 65377 && c <= 65439) {
            return true;
        }
        return false;
    }

    /**
     * 適切な郵便番号を検証する
     *
     * @param value 対象値
     * @return 結果（true：適切な形式、false：不正な形式）
     */
    public static Boolean isPostalCd(String value) {
        boolean result = true;

        if (isNotEmpty(value)) {
            result = PATTERN_POSTAL_CD.matcher(value).matches();
        }

        return result;
    }

    /**
     * Integerタイプを検証する
     *
     * @param value 対象値
     * @return 結果（true：適切な形式、false：不正な形式）
     */
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * 適切な電話番号を検証する
     *
     * @param value 対象値
     * @return 結果（true：適切な形式、false：不正な形式）
     */
    public static Boolean isHhMm(String value) {
        boolean result = true;

        if (isNotEmpty(value)) {
            result = PATTERN_HOUR_MINUTE.matcher(value).matches();
        }

        return result;
    }

    /**
     * 厳密な長さ: +、-、スペースを削除した後、10 桁または 11 桁である必要があります。
     * @param phone 対象値
     * @return 結果（true：適切な形式、false：不正な形式）
     */
    private static boolean strictLength(String phone) {
        String digits = phone.replaceAll("[^0-9]", "");
        return digits.length() == 10 || digits.length() == 11 ;
    }

}
