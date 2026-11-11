package jp.newpart.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.newpart.common.constant.Constants;
import org.springframework.util.AntPathMatcher;
import jp.newpart.common.text.StrFormatter;

/**
 * 文字列ツールクラス
 * 
 * @author nextosd
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils
{
    /**
     * ブレース（中括弧）
     */
    public static final String BRACES_START = "{";
    /**
     * ブレース（中括弧）
     */
    public static final String BRACES_END = "}";
    /** 空の文字列 */
    private static final String NULLSTR = "";

    /** アンダーライン */
    private static final char SEPARATOR = '_';

    /** アスタリスク */
    private static final char ASTERISK = '*';

    /**
     * 取得パラメータがNULLではありません
     * 
     * @param value
     * @param defaultValue
     * @return value
     */
    public static <T> T nvl(T value, T defaultValue)
    {
        return value != null ? value : defaultValue;
    }

    /**
     * コレクションが空かどうかを判断するには、List、Set、Queueを含む
     * 
     * @param coll Collection
     * @return true：空 false：非空
     */
    public static boolean isEmpty(Collection<?> coll)
    {
        return isNull(coll) || coll.isEmpty();
    }

    /**
     * * コレクションが空ではないかどうかを判断するには、List、Set、Queueを含む
     * 
     * @param coll 判断するコレクション
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Collection<?> coll)
    {
        return !isEmpty(coll);
    }

    /**
     * * オブジェクト配列が空かどうかを判断する
     * 
     * @param objects 判断するオブジェクト配列
     * @return true：空 false：非空
     */
    public static boolean isEmpty(Object[] objects)
    {
        return isNull(objects) || (objects.length == 0);
    }

    /**
     * * オブジェクト配列が空でないかどうかを判断する
     * 
     * @param objects 判断するオブジェクト配列
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Object[] objects)
    {
        return !isEmpty(objects);
    }

    /**
     * * Mapが空かどうかを判断する
     * 
     * @param map 判断するMap
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Map<?, ?> map)
    {
        return isNull(map) || map.isEmpty();
    }

    /**
     * * Mapが空かどうかを判断する
     * 
     * @param map 判断するMap
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Map<?, ?> map)
    {
        return !isEmpty(map);
    }

    /**
     * * 文字列が空白列であるかどうかを判断する
     * 
     * @param str String
     * @return true：空 false：非空
     */
    public static boolean isEmpty(String str)
    {
        return isNull(str) || NULLSTR.equals(str.trim());
    }

    /**
     * * 文字列が空白でないかどうかを判断する
     * 
     * @param str String
     * @return true：文字列 false：空文字列
     */
    public static boolean isNotEmpty(String str)
    {
        return !isEmpty(str);
    }

    /**
     * * オブジェクトが空かどうかを判断する
     * 
     * @param object Object
     * @return true：空 false：非空
     */
    public static boolean isNull(Object object)
    {
        return object == null;
    }

    /**
     * * オブジェクトが空でないかどうかを判断する
     * 
     * @param object Object
     * @return true：非空 false：空
     */
    public static boolean isNotNull(Object object)
    {
        return !isNull(object);
    }

    /**
     * オブジェクトの配列が空かどうかを判断します
     *
     * @param objects
     * @return
     */
    public static boolean isObjectEmpty(Object objects) {
        return isNull(objects) || isEmpty(String.valueOf(objects));
    }

    /**
     * * オブジェクトが配列タイプかどうかを判断する（Javaベースパターンの配列）
     * 
     * @param object
     * @return true：はい false：いいえ
     */
    public static boolean isArray(Object object)
    {
        return isNotNull(object) && object.getClass().isArray();
    }

    /**
     * スペースを削除
     */
    public static String trim(String str)
    {
        return (str == null ? "" : str.trim());
    }

    /**
     * 指定された文字列を置換する指定された区間内の文字は「*」です
     *
     * @param str 文字列
     * @param startInclude 開始位置（含む）
     * @param endExclude 終了位置（含まない）
     * @return 置換後の文字列
     */
    public static String hide(CharSequence str, int startInclude, int endExclude)
    {
        if (isEmpty(str))
        {
            return NULLSTR;
        }
        final int strLength = str.length();
        if (startInclude > strLength)
        {
            return NULLSTR;
        }
        if (endExclude > strLength)
        {
            endExclude = strLength;
        }
        if (startInclude > endExclude)
        {
            return NULLSTR;
        }
        final char[] chars = new char[strLength];
        for (int i = 0; i < strLength; i++)
        {
            if (i >= startInclude && i < endExclude)
            {
                chars[i] = ASTERISK;
            }
            else
            {
                chars[i] = str.charAt(i);
            }
        }
        return new String(chars);
    }

    /**
     * 切り取り文字列
     *
     * @param str 文字列
     * @param start 始める
     * @return 文字列
     */
    public static String substring(final String str, int start)
    {
        if (str == null)
        {
            return NULLSTR;
        }

        if (start < 0)
        {
            start = str.length() + start;
        }

        if (start < 0)
        {
            start = 0;
        }
        if (start > str.length())
        {
            return NULLSTR;
        }

        return str.substring(start);
    }

    /**
     * 切り取り文字列
     * 
     * @param str 文字列
     * @param start 始める
     * @param end 終了
     * @return 文字列
     */
    public static String substring(final String str, int start, int end)
    {
        if (str == null)
        {
            return NULLSTR;
        }

        if (end < 0)
        {
            end = str.length() + end;
        }
        if (start < 0)
        {
            start = str.length() + start;
        }

        if (end > str.length())
        {
            end = str.length();
        }

        if (start > end)
        {
            return NULLSTR;
        }

        if (start < 0)
        {
            start = 0;
        }
        if (end < 0)
        {
            end = 0;
        }

        return str.substring(start, end);
    }

    /**
     * 空白文字ではなく、空白文字であるかどうかを判断する
     * 
     * @param str value
     * @return boolean
     */
    public static boolean hasText(String str)
    {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str)
    {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++)
        {
            if (!Character.isWhitespace(str.charAt(i)))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 書式設定テキスト、{}はプレースホルダを表します<br/>
     * この方法は、単純にプレースホルダ{}をパラメータに順に置換するだけです<br/>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") -> this is a for b<br>
     * エスケープ{}： format("this is \\{} for {}", "a", "b") -> this is \{} for a<br>
     * エスケープ\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
     * 
     * @param template テキストテンプレート、置換された部分を{}で表す
     * @param params パラメータ値
     * @return
     */
    public static String format(String template, Object... params)
    {
        if (isEmpty(params) || isEmpty(template))
        {
            return template;
        }
        return StrFormatter.format(template, params);
    }

    /**
     * http（s）かどうか：//先頭
     * 
     * @param link
     * @return boolean
     */
    public static boolean ishttp(String link)
    {
        return StringUtils.startsWithAny(link, Constants.HTTP, Constants.HTTPS);
    }

    /**
     * 文字列シフトset
     * 
     * @param str 文字列
     * @param sep 分隔符
     * @return set
     */
    public static final Set<String> str2Set(String str, String sep)
    {
        return new HashSet<String>(str2List(str, sep, true, false));
    }

    /**
     * 文字列変換リスト
     * 
     * @param str 文字列
     * @param sep 分隔符
     * @param filterBlank 空白をフィルタ
     * @param trim 首尾の空白を取る
     * @return list
     */
    public static final List<String> str2List(String str, String sep, boolean filterBlank, boolean trim)
    {
        List<String> list = new ArrayList<String>();
        if (StringUtils.isEmpty(str))
        {
            return list;
        }

        if (filterBlank && StringUtils.isBlank(str))
        {
            return list;
        }
        String[] split = str.split(sep);
        for (String string : split)
        {
            if (filterBlank && StringUtils.isBlank(string))
            {
                continue;
            }
            if (trim)
            {
                string = string.trim();
            }
            list.add(string);
        }

        return list;
    }

    /**
     * collectionリストに配列arrayが含まれているかどうかを判断
     *
     * @param collection 指定集合
     * @param array 指定配列
     * @return boolean
     */
    public static boolean containsAny(Collection<String> collection, String... array)
    {
        if (isEmpty(collection) || isEmpty(array))
        {
            return false;
        }
        else
        {
            for (String str : array)
            {
                if (collection.contains(str))
                {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 指定した文字列に指定した文字列リストのいずれかの文字列が含まれているかどうかを検索します。
     * 同時に大文字と小文字を無視します。
     *
     * @param cs 文字列の指定
     * @param searchCharSequences チェックする文字列配列
     * @return 任意の文字列を含むかどうか
     */
    public static boolean containsAnyIgnoreCase(CharSequence cs, CharSequence... searchCharSequences)
    {
        if (isEmpty(cs) || isEmpty(searchCharSequences))
        {
            return false;
        }
        for (CharSequence testStr : searchCharSequences)
        {
            if (containsIgnoreCase(cs, testStr))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * ラクダの峰の下線の名前付け
     */
    public static String toUnderScoreCase(String str)
    {
        if (str == null)
        {
            return null;
        }
        StringBuilder sb = new StringBuilder();

        boolean preCharIsUpperCase = true;

        boolean curreCharIsUpperCase = true;

        boolean nexteCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if (i > 0)
            {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            }
            else
            {
                preCharIsUpperCase = false;
            }

            curreCharIsUpperCase = Character.isUpperCase(c);

            if (i < (str.length() - 1))
            {
                nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase)
            {
                sb.append(SEPARATOR);
            }
            else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase)
            {
                sb.append(SEPARATOR);
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 文字列を含めるかどうか
     * 
     * @param str 文字列の検証
     * @param strs 文字列グループ
     * @return true/false
     */
    public static boolean inStringIgnoreCase(String str, String... strs)
    {
        if (str != null && strs != null)
        {
            for (String s : strs)
            {
                if (str.equalsIgnoreCase(trim(s)))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * アンダースコア大文字方式で命名された文字列をラクダピーク式に変換します。
     * 変換前のアンダースコア大文字で命名された文字列が空の場合は、空の文字列を返します。
     * 例：HELLO _ WORLD->HelloWorld
     * 
     * @param name
     * @return
     */
    public static String convertToCamelCase(String name)
    {
        StringBuilder result = new StringBuilder();

        if (name == null || name.isEmpty())
        {
            return "";
        }
        else if (!name.contains("_"))
        {
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        String[] camels = name.split("_");
        for (String camel : camels)
        {
            if (camel.isEmpty())
            {
                continue;
            }
            result.append(camel.substring(0, 1).toUpperCase());
            result.append(camel.substring(1).toLowerCase());
        }
        return result.toString();
    }

    /**
     * ラクダピーク命名法
     * 例：user_name->userName
     */
    public static String toCamelCase(String s)
    {
        if (s == null)
        {
            return null;
        }
        if (s.indexOf(SEPARATOR) == -1)
        {
            return s;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);

            if (c == SEPARATOR)
            {
                upperCase = true;
            }
            else if (upperCase)
            {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            }
            else
            {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 指定した文字列が指定した文字列リストのいずれかに一致するかどうかを検索します
     * 
     * @param str 文字列の指定
     * @param strs チェックする文字列配列
     * @return 一致するかどうか
     */
    public static boolean matches(String str, List<String> strs)
    {
        if (isEmpty(str) || isEmpty(strs))
        {
            return false;
        }
        for (String pattern : strs)
        {
            if (isMatch(pattern, str))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * urlが規則的な構成であるかどうかを判断する：
     * ？単一文字を表す、
     * * 階層にまたがってはならない階層パス内の任意の文字列を表します。
     * ** は任意のレイヤーパスを表し、
     * 
     * @param pattern 規則
     * @param url url
     * @return
     */
    public static boolean isMatch(String pattern, String url)
    {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(pattern, url);
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj)
    {
        return (T) obj;
    }

    /**
     * 数値の左に0を補足して、指定した長さにします。数値を文字列に変換した後、sizeより長い場合は、最後のsize文字のみが保持されます。
     * 
     * @param num 数値
     * @param size 文字列指定長
     * @return
     */
    public static final String padl(final Number num, final int size)
    {
        return padl(num.toString(), size, '0');
    }

    /**
     * 文字列の左揃え元の文字列sの長さがsizeより大きい場合は、最後のsize文字のみが保持されます。
     * 
     * @param s 元の文字列
     * @param size 文字列指定長
     * @param c 補足文字
     * @return 指定された長さの文字列を返します。元の文字列を左揃えまたは切り取りします。
     */
    public static final String padl(final String s, final int size, final char c)
    {
        final StringBuilder sb = new StringBuilder(size);
        if (s != null)
        {
            final int len = s.length();
            if (s.length() <= size)
            {
                for (int i = size - len; i > 0; i--)
                {
                    sb.append(c);
                }
                sb.append(s);
            }
            else
            {
                return s.substring(len - size, len);
            }
        }
        else
        {
            for (int i = size; i > 0; i--)
            {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 左側と右側の空白を取り除く。
     *
     * @param value 対象値
     * @return 加工後文字列
     */
    public static String trimBlank(String value) {
        String regularExp = "^[\\s　]+|[\\s　]+$";
        value = value.replaceAll(regularExp, "");
        return value;
    }

    /**
     * 左側の空白を取り除く。
     *
     * @param value 対象値
     * @return 加工後文字列
     */
    public static String trimLeftBlank(String value) {
        String regularExp = "^[\\s　]+";
        value = value.replaceAll(regularExp, "");
        return value;
    }
}