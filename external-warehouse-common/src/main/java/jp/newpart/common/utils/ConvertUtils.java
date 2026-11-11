package jp.newpart.common.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.Set;

/**
 * タイプコンバータ
 *
 * @author nextosd
 */
public class ConvertUtils {
    /**
     * 文字列に変換<br>
     * 指定した値がnullまたは変換に失敗した場合は、デフォルト値を返します。<br>
     * 変換に失敗してもエラーは出ない
     *
     * @param value        変換された値
     * @param defaultValue 変換にエラーは出る時、デーフォルト値
     * @return 結果
     */
    public static String toStr(Object value, String defaultValue) {
        if (null == value) {
            return defaultValue;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return value.toString();
    }

    /**
     * 文字列に変換<br>
     * 指定した値がnullまたは変換に失敗した場合は、デフォルト値を返します。<br>
     * 変換に失敗してもエラーは出ない
     *
     * @param value 変換された値
     * @return 結果
     */
    public static String toStr(Object value) {
        return toStr(value, null);
    }

    /**
     * 文字列に変換<br>
     * 指定した値がnullまたは変換に失敗した場合は、デフォルト値を返します。<br>
     * 変換に失敗してもエラーは出ない
     *
     * @param value        変換された値
     * @param defaultValue 変換にエラーは出る時、デーフォルト値
     * @return 結果
     */
    public static Character toChar(Object value, Character defaultValue) {
        if (null == value) {
            return defaultValue;
        }
        if (value instanceof Character) {
            return (Character) value;
        }

        final String valueStr = toStr(value, null);
        return StringUtils.isEmpty(valueStr) ? defaultValue : valueStr.charAt(0);
    }

    /**
     * 文字列に変換<br>
     * 指定した値がnullまたは変換に失敗した場合は、デフォルト値を返します。<br>
     * 変換に失敗してもエラーは出ない
     *
     * @param value 変換された値
     * @return 結果
     */
    public static Character toChar(Object value) {
        return toChar(value, null);
    }

    /**
     * byteに変換<br>
     * 指定した値がnullまたは変換に失敗した場合は、デフォルト値を返します。<br>
     * 変換に失敗してもエラーは出ない
     *
     * @param value        変換された値
     * @param defaultValue 変換にエラーは出る時、デーフォルト値
     * @return 結果
     */
    public static Byte toByte(Object value, Byte defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Byte) {
            return (Byte) value;
        }
        if (value instanceof Number) {
            return ((Number) value).byteValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return Byte.parseByte(valueStr);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * byteに変換<br>
     * 指定した値が<code>null</code>，または、変換に失敗した場合は、デフォルト値を返します。<code>null</code><br>
     * 変換に失敗してもエラーは出ない
     *
     * @param value 変換された値
     * @return 結果
     */
    public static Byte toByte(Object value) {
        return toByte(value, null);
    }

    /**
     * Shortに変換<br>
     * 指定した値が<code>null</code>，または、変換に失敗した場合は、デフォルト値を返します。<code>null</code><br>
     * 変換に失敗してもエラーは出ない
     *
     * @param value        変換された値
     * @param defaultValue 変換にエラーは出る時、デーフォルト値
     * @return 結果
     */
    public static Short toShort(Object value, Short defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Short) {
            return (Short) value;
        }
        if (value instanceof Number) {
            return ((Number) value).shortValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return Short.parseShort(valueStr.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Shortに変換<br>
     * 指定した値が<code>null</code>，または、変換に失敗した場合は、デフォルト値を返します。<code>null</code><br>
     * 変換に失敗してもエラーは出ない
     *
     * @param value 変換された値
     * @return 結果
     */
    public static Short toShort(Object value) {
        return toShort(value, null);
    }

    /**
     * Numberに変換<br>
     * 指定した値が<code>null</code>，または、変換に失敗した場合は、デフォルト値を返します。<code>null</code><br>
     * 変換に失敗してもエラーは出ない
     *
     * @param value        変換された値
     * @param defaultValue 変換にエラーは出る時、デーフォルト値
     * @return 結果
     */
    public static Number toNumber(Object value, Number defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number) {
            return (Number) value;
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return NumberFormat.getInstance().parse(valueStr);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Numberに変換<br>
     * 指定した値が<code>null</code>，または、変換に失敗した場合は、デフォルト値を返します。<code>null</code><br>
     * 変換に失敗してもエラーは出ない
     *
     * @param value 変換された値
     * @return 結果
     */
    public static Number toNumber(Object value) {
        return toNumber(value, null);
    }

    /**
     * intに変換<br>
     * 指定した値が<code>null</code>，または、変換に失敗した場合は、デフォルト値を返します。<code>null</code><br>
     * 変換に失敗してもエラーは出ない
     *
     * @param value        変換された値
     * @param defaultValue 変換にエラーは出る時、デーフォルト値
     * @return 結果
     */
    public static Integer toInt(Object value, Integer defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(valueStr.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * intに変換<br>
     * 指定した値が<code>null</code>，または、変換に失敗した場合は、デフォルト値を返します。<code>null</code><br>
     * 変換に失敗してもエラーは出ない
     *
     * @param value 変換された値
     * @return 結果
     */
    public static Integer toInt(Object value) {
        return toInt(value, null);
    }

    /**
     * Integer配列に変換<br>
     *
     * @param str 変換された値
     * @return 結果
     */
    public static Integer[] toIntArray(String str) {
        return toIntArray(",", str);
    }

    /**
     * Long配列に変換<br>
     *
     * @param str 変換された値
     * @return 結果
     */
    public static Long[] toLongArray(String str) {
        return toLongArray(",", str);
    }

    /**
     * Integer配列に変換<br>
     *
     * @param split セパレータ
     * @param split 変換された値
     * @return 結果
     */
    public static Integer[] toIntArray(String split, String str) {
        if (StringUtils.isEmpty(str)) {
            return new Integer[]{};
        }
        String[] arr = str.split(split);
        final Integer[] ints = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            final Integer v = toInt(arr[i], 0);
            ints[i] = v;
        }
        return ints;
    }

    /**
     * Long配列に変換<br>
     *
     * @param split セパレータ
     * @param split 変換された値
     * @return 結果
     */
    public static Long[] toLongArray(String split, String str) {
        if (StringUtils.isEmpty(str)) {
            return new Long[]{};
        }
        String[] arr = str.split(split);
        final Long[] longs = new Long[arr.length];
        for (int i = 0; i < arr.length; i++) {
            final Long v = toLong(arr[i], null);
            longs[i] = v;
        }
        return longs;
    }

    /**
     * String配列に変換<br>
     *
     * @param str 変換された値
     * @return 結果
     */
    public static String[] toStrArray(String str) {
        return toStrArray(",", str);
    }

    /**
     * String配列に変換<br>
     *
     * @param split セパレータ
     * @param split 変換された値
     * @return 結果
     */
    public static String[] toStrArray(String split, String str) {
        return str.split(split);
    }

    /**
     * longに変換<br>
     * 指定した値がnullまたは変換に失敗した場合は、デフォルト値を返します。<br>
     * 変換に失敗してもエラーは出ない
     *
     * @param value        変換された値
     * @param defaultValue 変換にエラーは出る時、デーフォルト値
     * @return 結果
     */
    public static Long toLong(Object value, Long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            // 支持科学计数法
            return new BigDecimal(valueStr.trim()).longValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * longに変換<br>
     * 指定した値が<code>null</code>，または、変換に失敗した場合は、デフォルト値を返します。<code>null</code><br>
     * 変換に失敗してもエラーは出ない
     *
     * @param value 変換された値
     * @return 結果
     */
    public static Long toLong(Object value) {
        return toLong(value, null);
    }

    /**
     * doubleに変換<br>
     * 指定した値がnullまたは変換に失敗した場合は、デフォルト値を返します。<br>
     * 変換に失敗してもエラーは出ない
     *
     * @param value        変換された値
     * @param defaultValue 変換にエラーは出る時、デーフォルト値
     * @return 結果
     */
    public static Double toDouble(Object value, Double defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Double) {
            return (Double) value;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            // 支持科学计数法
            return new BigDecimal(valueStr.trim()).doubleValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * doubleに変換<br>
     * 指定した値がnullまたは変換に失敗した場合は、デフォルト値を返します。<code>null</code><br>
     * 変換に失敗してもエラーは出ない
     *
     * @param value 変換された値
     * @return 結果
     */
    public static Double toDouble(Object value) {
        return toDouble(value, null);
    }

    /**
     * Floatに変換<br>
     * 指定した値がnullまたは変換に失敗した場合は、デフォルト値を返します。<br>
     * 変換に失敗してもエラーは出ない
     *
     * @param value        変換された値
     * @param defaultValue 変換にエラーは出る時、デーフォルト値
     * @return 結果
     */
    public static Float toFloat(Object value, Float defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Float) {
            return (Float) value;
        }
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(valueStr.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Floatに変換<br>
     * 指定した値がnullまたは変換に失敗した場合は、デフォルト値を返します。<code>null</code><br>
     * 変換に失敗してもエラーは出ない
     *
     * @param value 変換された値
     * @return 結果
     */
    public static Float toFloat(Object value) {
        return toFloat(value, null);
    }

    /**
     * booleanに変換<br>
     * Stringサポートの値：true、false、yes、ok、no，1,0 指定した値がnullまたは変換に失敗した場合は、デフォルト値を返します。<br>
     * 変換にエラーは出る時、デーフォルト値
     *
     * @param value        変換された値
     * @param defaultValue 変換にエラーは出る時、デーフォルト値
     * @return 結果
     */
    public static Boolean toBool(Object value, Boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr)) {
            return defaultValue;
        }
        valueStr = valueStr.trim().toLowerCase();
        return switch (valueStr) {
            case "true" -> true;
            case "false" -> false;
            case "yes" -> true;
            case "ok" -> true;
            case "no" -> false;
            case "1" -> true;
            case "0" -> false;
            default -> defaultValue;
        };
    }

    /**
     * booleanに変換<br>
     * 指定した値がnullまたは変換に失敗した場合は、デフォルト値を返します。<code>null</code><br>
     * 変換に失敗してもエラーは出ない
     *
     * @param value 変換された値
     * @return 結果
     */
    public static Boolean toBool(Object value) {
        return toBool(value, null);
    }

    /**
     * Enum対象に変換<br>
     * 指定した値がnullまたは変換に失敗した場合は、デフォルト値を返します。<br>
     *
     * @param clazz        EnumのClass
     * @param value        値
     * @param defaultValue デーフォルト値値
     * @return Enum
     */
    public static <E extends Enum<E>> E toEnum(Class<E> clazz, Object value, E defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (clazz.isAssignableFrom(value.getClass())) {
            E myE = (E) value;
            return myE;
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return Enum.valueOf(clazz, valueStr);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Enum対象に変換<br>
     * 指定した値がnullまたは変換に失敗した場合は、デフォルト値を返します。<code>null</code><br>
     *
     * @param clazz EnumのClass
     * @param value 値
     * @return Enum
     */
    public static <E extends Enum<E>> E toEnum(Class<E> clazz, Object value) {
        return toEnum(clazz, value, null);
    }

    /**
     * BigIntegerに変換<br>
     * 指定した値がnullまたは変換に失敗した場合は、デフォルト値を返します。<br>
     * 変換に失敗してもエラーは出ない
     *
     * @param value        変換された値
     * @param defaultValue 変換にエラーは出る時、デーフォルト値
     * @return 結果
     */
    public static BigInteger toBigInteger(Object value, BigInteger defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof BigInteger) {
            return (BigInteger) value;
        }
        if (value instanceof Long) {
            return BigInteger.valueOf((Long) value);
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return new BigInteger(valueStr);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * BigIntegerに変換<br>
     * 指定した値がnullまたは変換に失敗した場合は、デフォルト値を返します。<br>
     * 変換に失敗してもエラーは出ない
     *
     * @param value 変換された値
     * @return 結果
     */
    public static BigInteger toBigInteger(Object value) {
        return toBigInteger(value, null);
    }

    /**
     * BigDecimalに変換<br>
     * 指定した値がnullまたは変換に失敗した場合は、デフォルト値を返します。<br>
     * 変換に失敗してもエラーは出ない
     *
     * @param value        変換された値
     * @param defaultValue 変換にエラーは出る時、デーフォルト値
     * @return 結果
     */
    public static BigDecimal toBigDecimal(Object value, BigDecimal defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Long) {
            return new BigDecimal((Long) value);
        }
        if (value instanceof Double) {
            return new BigDecimal((Double) value);
        }
        if (value instanceof Integer) {
            return new BigDecimal((Integer) value);
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return new BigDecimal(valueStr);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * BigDecimalに変換<br>
     * 指定した値がnullまたは変換に失敗した場合は、デフォルト値を返します。<br>
     * 変換に失敗してもエラーは出ない
     *
     * @param value 変換された値
     * @return 結果
     */
    public static BigDecimal toBigDecimal(Object value) {
        return toBigDecimal(value, null);
    }

    /**
     * オブジェクトを文字列に変換<br>
     * 1、Byte配列とByteBufferは文字列の配列に変換する 2、対象配列はArrays.toStringをコールする
     *
     * @param obj オブジェクト
     * @return 文字列
     */
    public static String utf8Str(Object obj) {
        return str(obj, "UTF-8");
    }

    /**
     * オブジェクトを文字列に変換する<br>
     * 1、Byte配列とByteBufferは文字列の配列に変換する 2、対象配列はArrays.toStringをコールする
     *
     * @param obj         オブジェクト
     * @param charsetName 文字セット
     * @return 文字列
     */
    public static String str(Object obj, String charsetName) {
        return str(obj, Charset.forName(charsetName));
    }

    /**
     * オブジェクトを文字列に変換する<br>
     * 1、Byte配列とByteBufferは文字列の配列に変換する 2、対象配列はArrays.toStringをコールする
     *
     * @param obj     オブジェクト
     * @param charset 文字セット
     * @return 文字列
     */
    public static String str(Object obj, Charset charset) {
        if (null == obj) {
            return null;
        }

        if (obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof byte[]) {
            return str((byte[]) obj, charset);
        } else if (obj instanceof Byte[]) {
            byte[] bytes = ArrayUtils.toPrimitive((Byte[]) obj);
            return str(bytes, charset);
        } else if (obj instanceof ByteBuffer) {
            return str((ByteBuffer) obj, charset);
        }
        return obj.toString();
    }

    /**
     * byte配列を文字列に変換する
     *
     * @param bytes   byte配列
     * @param charset 文字セット
     * @return 文字列
     */
    public static String str(byte[] bytes, String charset) {
        return str(bytes, StringUtils.isEmpty(charset) ? Charset.defaultCharset() : Charset.forName(charset));
    }

    /**
     * バイトコードをデコードする
     *
     * @param data    文字配列
     * @param charset 文字セット。空の場合、結果はプラットフォームに依存する
     * @return デコード後の文字列
     */
    public static String str(byte[] data, Charset charset) {
        if (data == null) {
            return null;
        }

        if (null == charset) {
            return new String(data);
        }
        return new String(data, charset);
    }

    /**
     * エンコードがbyteBufferのデータを文字列に変換する
     *
     * @param data    データ
     * @param charset 文字セット。空の場合、カレントシステムの文字セット
     * @return 文字配列
     */
    public static String str(ByteBuffer data, String charset) {
        if (data == null) {
            return null;
        }

        return str(data, Charset.forName(charset));
    }

    /**
     * エンコードがbyteBufferのデータを文字列に変換する
     *
     * @param data    データ
     * @param charset 文字セット。空の場合、カレントシステムの文字セット
     * @return 文字配列
     */
    public static String str(ByteBuffer data, Charset charset) {
        if (null == charset) {
            charset = Charset.defaultCharset();
        }
        return charset.decode(data).toString();
    }

    /**
     * 半角を全角に変換する
     *
     * @param input String.
     * @return 全角文字列.
     */
    public static String toSBC(String input) {
        return toSBC(input, null);
    }

    /**
     * 半角を全角に変換する
     *
     * @param input         String
     * @param notConvertSet 置換しない文字集
     * @return 全角文字列.
     */
    public static String toSBC(String input, Set<Character> notConvertSet) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (null != notConvertSet && notConvertSet.contains(c[i])) {
                continue;
            }

            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);

            }
        }
        return new String(c);
    }

    /**
     * 全角を半角に変換する
     *
     * @param input String.
     * @return 半角文字列
     */
    public static String toDBC(String input) {
        return toDBC(input, null);
    }

    /**
     * 全角を半角に置換する
     *
     * @param text          テキスト
     * @param notConvertSet 置換しない文字集
     * @return 置換後の文字
     */
    public static String toDBC(String text, Set<Character> notConvertSet) {
        char[] c = text.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (null != notConvertSet && notConvertSet.contains(c[i])) {
                continue;
            }

            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);
            }
        }
        String returnString = new String(c);

        return returnString;
    }

    /**
     * 全角スペースは半角スペースを変換する
     *
     * @param text
     * @return
     */
    public static String trimWhitespace(String text) {
        char[] c = text.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            }
        }
        return new String(c);
    }

    /**
     * NULLの場合、空白に変換する。
     *
     * @param value String.
     * @return 変換した文字列.
     */
    public static String null2Empty(String value) {
        if (null == value) {
            return StringUtils.EMPTY;
        }
        return value;
    }
}
