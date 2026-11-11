package jp.newpart.common.text;

import jp.newpart.common.exception.UtilException;
import jp.newpart.common.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.Set;

/**
 * タイプ変換器
 *
 * @author nextosd
 */
public class Convert
{
    /**
     * 文字に変換
     *
     * @param value 値
     * @param defaultValue デフォルト値
     * @return 
     */
    public static String toStr(Object value, String defaultValue)
    {
        if (null == value)
        {
            return defaultValue;
        }
        if (value instanceof String)
        {
            return (String) value;
        }
        return value.toString();
    }

    /**
     * 文字に変換
     *
     * @param value 値
     * @return 
     */
    public static String toStr(Object value)
    {
        return toStr(value, null);
    }

    /**
     * 文字に変換
     *
     * @param value 値
     * @param defaultValue デフォルト値
     * @return 
     */
    public static Character toChar(Object value, Character defaultValue)
    {
        if (null == value)
        {
            return defaultValue;
        }
        if (value instanceof Character)
        {
            return (Character) value;
        }

        final String valueStr = toStr(value, null);
        return StringUtils.isEmpty(valueStr) ? defaultValue : valueStr.charAt(0);
    }

    /**
     * 文字に変換
     *
     * @param value 値
     * @return 
     */
    public static Character toChar(Object value)
    {
        return toChar(value, null);
    }

    /**
     * byteに変換
     *
     * @param value 値
     * @param defaultValue デフォルト値
     * @return 
     */
    public static Byte toByte(Object value, Byte defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof Byte)
        {
            return (Byte) value;
        }
        if (value instanceof Number)
        {
            return ((Number) value).byteValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            return Byte.parseByte(valueStr);
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     * byteに変換
     *
     * @param value 値
     * @return 
     */
    public static Byte toByte(Object value)
    {
        return toByte(value, null);
    }

    /**
     * Shortに変換
     *
     * @param value 値
     * @param defaultValue デフォルト値
     * @return 
     */
    public static Short toShort(Object value, Short defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof Short)
        {
            return (Short) value;
        }
        if (value instanceof Number)
        {
            return ((Number) value).shortValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            return Short.parseShort(valueStr.trim());
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     * Shortに変換
     *
     * @param value 値
     * @return 
     */
    public static Short toShort(Object value)
    {
        return toShort(value, null);
    }

    /**
     * Numberに変換
     *
     * @param value 値
     * @param defaultValue デフォルト値
     * @return 
     */
    public static Number toNumber(Object value, Number defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof Number)
        {
            return (Number) value;
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            return NumberFormat.getInstance().parse(valueStr);
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     * Numberに変換
     *
     * @param value 値
     * @return 
     */
    public static Number toNumber(Object value)
    {
        return toNumber(value, null);
    }

    /**
     * intに変換
     *
     * @param value 値
     * @param defaultValue デフォルト値
     * @return 
     */
    public static Integer toInt(Object value, Integer defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof Integer)
        {
            return (Integer) value;
        }
        if (value instanceof Number)
        {
            return ((Number) value).intValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            return Integer.parseInt(valueStr.trim());
        }
        catch (Exception e)
        {
            throw new UtilException("型変換エラーです。Int型のデータを入力してください。");
        }
    }

    /**
     * intに変換
     *
     * @param value 値
     * @return 
     */
    public static Integer toInt(Object value)
    {
        return toInt(value, null);
    }

    /**
     * Integer配列に変換
     *
     * @param str 値
     * @return 
     */
    public static Integer[] toIntArray(String str)
    {
        return toIntArray(",", str);
    }

    /**
     * Long配列に変換
     *
     * @param str 値
     * @return 
     */
    public static Long[] toLongArray(String str)
    {
        return toLongArray(",", str);
    }

    /**
     * Integer配列に変換
     *
     * @param split 区切り文字
     * @param split 値
     * @return 
     */
    public static Integer[] toIntArray(String split, String str)
    {
        if (StringUtils.isEmpty(str))
        {
            return new Integer[] {};
        }
        String[] arr = str.split(split);
        final Integer[] ints = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++)
        {
            final Integer v = toInt(arr[i], 0);
            ints[i] = v;
        }
        return ints;
    }

    /**
     * Long配列に変換<br>
     *
     * @param split 区切り文字
     * @param str 値
     * @return 
     */
    public static Long[] toLongArray(String split, String str)
    {
        if (StringUtils.isEmpty(str))
        {
            return new Long[] {};
        }
        String[] arr = str.split(split);
        final Long[] longs = new Long[arr.length];
        for (int i = 0; i < arr.length; i++)
        {
            final Long v = toLong(arr[i], null);
            longs[i] = v;
        }
        return longs;
    }

    /**
     * String配列に変換<br>
     *
     * @param str 値
     * @return 
     */
    public static String[] toStrArray(String str)
    {
        if (StringUtils.isEmpty(str))
        {
            return new String[] {};
        }
        return toStrArray(",", str);
    }

    /**
     * String配列に変換<br>
     *
     * @param split 区切り文字
     * @param split 値
     * @return 
     */
    public static String[] toStrArray(String split, String str)
    {
        return str.split(split);
    }

    /**
     * longに変換
     *
     * @param value 値
     * @param defaultValue デフォルト値
     * @return 
     */
    public static Long toLong(Object value, Long defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof Long)
        {
            return (Long) value;
        }
        if (value instanceof Number)
        {
            return ((Number) value).longValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            return new BigDecimal(valueStr.trim()).longValue();
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     * longに変換
     *
     * @param value 値
     * @return 
     */
    public static Long toLong(Object value)
    {
        return toLong(value, null);
    }

    /**
     * doubleに変換<br>
     *
     * @param value 値
     * @param defaultValue デフォルト値
     * @return 
     */
    public static Double toDouble(Object value, Double defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof Double)
        {
            return (Double) value;
        }
        if (value instanceof Number)
        {
            return ((Number) value).doubleValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            return new BigDecimal(valueStr.trim()).doubleValue();
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     * doubleに変換<br>
     *
     * @param value 値
     * @return 
     */
    public static Double toDouble(Object value)
    {
        return toDouble(value, null);
    }

    /**
     * Floatに変換<br>
     *
     * @param value 値
     * @param defaultValue デフォルト値
     * @return 
     */
    public static Float toFloat(Object value, Float defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof Float)
        {
            return (Float) value;
        }
        if (value instanceof Number)
        {
            return ((Number) value).floatValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            return Float.parseFloat(valueStr.trim());
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     * Floatに変換<br>
     *
     * @param value 値
     * @return 
     */
    public static Float toFloat(Object value)
    {
        return toFloat(value, null);
    }

    /**
     * booleanに変換<br>
     *
     * @param value 値
     * @param defaultValue デフォルト値
     * @return 
     */
    public static Boolean toBool(Object value, Boolean defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof Boolean)
        {
            return (Boolean) value;
        }
        String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        valueStr = valueStr.trim().toLowerCase();
        switch (valueStr)
        {
            case "true":
            case "yes":
            case "ok":
            case "1":
                return true;
            case "false":
            case "no":
            case "0":
                return false;
            default:
                return defaultValue;
        }
    }

    /**
     * booleanに変換<br>
     *
     * @param value 値
     * @return 
     */
    public static Boolean toBool(Object value)
    {
        return toBool(value, null);
    }

    /**
     * Enumオブジェクトに変換<br>
     *
     * @param clazz Enum Class
     * @param value 値
     * @param defaultValue デフォルト値
     * @return Enum
     */
    public static <E extends Enum<E>> E toEnum(Class<E> clazz, Object value, E defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (clazz.isAssignableFrom(value.getClass()))
        {
            @SuppressWarnings("unchecked")
            E myE = (E) value;
            return myE;
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            return Enum.valueOf(clazz, valueStr);
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     * Enumオブジェクトに変換<br>
     *
     * @param clazz EnumのClass
     * @param value 値
     * @return Enum
     */
    public static <E extends Enum<E>> E toEnum(Class<E> clazz, Object value)
    {
        return toEnum(clazz, value, null);
    }

    /**
     * BigIntegerに変換<br>
     *
     * @param value 値
     * @param defaultValue デフォルト値
     * @return
     */
    public static BigInteger toBigInteger(Object value, BigInteger defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof BigInteger)
        {
            return (BigInteger) value;
        }
        if (value instanceof Long)
        {
            return BigInteger.valueOf((Long) value);
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            return new BigInteger(valueStr);
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     * BigIntegerに変換<br>
     *
     * @param value
     * @return
     */
    public static BigInteger toBigInteger(Object value)
    {
        return toBigInteger(value, null);
    }

    /**
     * BigDecimalに変換<br>
     *
     * @param value 値
     * @param defaultValue デフォルト値
     * @return
     */
    public static BigDecimal toBigDecimal(Object value, BigDecimal defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof BigDecimal)
        {
            return (BigDecimal) value;
        }
        if (value instanceof Long)
        {
            return new BigDecimal((Long) value);
        }
        if (value instanceof Double)
        {
            return BigDecimal.valueOf((Double) value);
        }
        if (value instanceof Integer)
        {
            return new BigDecimal((Integer) value);
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            return new BigDecimal(valueStr);
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     * BigDecimalに変換<br>
     *
     * @param value 値
     * @return
     */
    public static BigDecimal toBigDecimal(Object value)
    {
        return toBigDecimal(value, null);
    }

    /**
     * オブジェクトを文字列に変換<br>
     * 1、Byte配列とByteBufferは対応する文字列の配列
     * 2に変換され、オブジェクト配列はArrays.toStringメソッドを呼び出す
     *
     * @param obj オブジェクト
     * @return 文字列
     */
    public static String utf8Str(Object obj)
    {
        return str(obj, CharsetKit.CHARSET_UTF_8);
    }

    /**
     * オブジェクトを文字列に変換<br>
     * 1、Byte配列とByteBufferは対応する文字列の配列
     * 2に変換され、オブジェクト配列はArrays.toStringメソッドを呼び出す
     *
     * @param obj オブジェクト
     * @param charsetName 文字列
     * @return 文字列
     */
    public static String str(Object obj, String charsetName)
    {
        return str(obj, Charset.forName(charsetName));
    }

    /**
     * オブジェクトを文字列に変換<br>
     * 1、Byte配列とByteBufferは対応する文字列の配列
     * 2に変換され、オブジェクト配列はArrays.toStringメソッドを呼び出す
     *
     * @param obj オブジェクト
     * @param charset 文字セット
     * @return 文字列
     */
    public static String str(Object obj, Charset charset)
    {
        if (null == obj)
        {
            return null;
        }

        if (obj instanceof String)
        {
            return (String) obj;
        }
        else if (obj instanceof byte[])
        {
            return str((byte[]) obj, charset);
        }
        else if (obj instanceof Byte[])
        {
            byte[] bytes = ArrayUtils.toPrimitive((Byte[]) obj);
            return str(bytes, charset);
        }
        else if (obj instanceof ByteBuffer)
        {
            return str((ByteBuffer) obj, charset);
        }
        return obj.toString();
    }

    /**
     * byte配列を文字列に変換するには
     *
     * @param bytes byte配列
     * @param charset 文字セット
     * @return 文字列
     */
    public static String str(byte[] bytes, String charset)
    {
        return str(bytes, StringUtils.isEmpty(charset) ? Charset.defaultCharset() : Charset.forName(charset));
    }

    /**
     * デコードバイトコード
     *
     * @param data 文字列
     * @param charset 文字セット。このフィールドが空白の場合、復号の結果はプラットフォームに依存します
     * @return
     */
    public static String str(byte[] data, Charset charset)
    {
        if (data == null)
        {
            return null;
        }

        if (null == charset)
        {
            return new String(data);
        }
        return new String(data, charset);
    }

    /**
     * エンコードされたbyteBufferデータを文字列に変換する
     *
     * @param data データ
     * @param charset 文字セット、現在のシステム文字セットを使用する場合は空
     * @return 文字列
     */
    public static String str(ByteBuffer data, String charset)
    {
        if (data == null)
        {
            return null;
        }

        return str(data, Charset.forName(charset));
    }

    /**
     * エンコードされたbyteBufferデータを文字列に変換する
     *
     * @param data データ
     * @param charset 文字セット、現在のシステム文字セットを使用する場合は空
     * @return 文字列
     */
    public static String str(ByteBuffer data, Charset charset)
    {
        if (null == charset)
        {
            charset = Charset.defaultCharset();
        }
        return charset.decode(data).toString();
    }

    // ----------------------------------------------------------------------- 全角半角変換
    /**
     * 半角-> 全角
     *
     * @param input String.
     * @return 全角.
     */
    public static String toSBC(String input)
    {
        return toSBC(input, null);
    }

    /**
     * 半角->全角
     *
     * @param input String
     * @param notConvertSet 置換されない文字コレクション
     * @return
     */
    public static String toSBC(String input, Set<Character> notConvertSet)
    {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++)
        {
            if (null != notConvertSet && notConvertSet.contains(c[i]))
            {
                continue;
            }

            if (c[i] == ' ')
            {
                c[i] = '\u3000';
            }
            else if (c[i] < '\177')
            {
                c[i] = (char) (c[i] + 65248);

            }
        }
        return new String(c);
    }

    /**
     * 全角->半角
     *
     * @param input String.
     * @return 半角
     */
    public static String toDBC(String input)
    {
        return toDBC(input, null);
    }

    /**
     * 全角を半角に置換
     *
     * @param text テキスト
     * @param notConvertSet 置換されない文字コレクション
     * @return
     */
    public static String toDBC(String text, Set<Character> notConvertSet)
    {
        char[] c = text.toCharArray();
        for (int i = 0; i < c.length; i++)
        {
            if (null != notConvertSet && notConvertSet.contains(c[i]))
            {
                continue;
            }

            if (c[i] == '\u3000')
            {
                c[i] = ' ';
            }
            else if (c[i] > '\uFF00' && c[i] < '\uFF5F')
            {
                c[i] = (char) (c[i] - 65248);
            }
        }
        String returnString = new String(c);

        return returnString;
    }

}
