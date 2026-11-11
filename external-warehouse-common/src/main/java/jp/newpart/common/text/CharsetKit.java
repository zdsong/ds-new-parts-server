package jp.newpart.common.text;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import jp.newpart.common.utils.StringUtils;

/**
 * 文字セットツールクラス
 * 
 * @author nextosd
 */
public class CharsetKit
{
    /** ISO-8859-1 */
    public static final String ISO_8859_1 = "ISO-8859-1";
    /** UTF-8 */
    public static final String UTF_8 = "UTF-8";
    /** GBK */
    public static final String GBK = "GBK";

    /** ISO-8859-1 */
    public static final Charset CHARSET_ISO_8859_1 = Charset.forName(ISO_8859_1);
    /** UTF-8 */
    public static final Charset CHARSET_UTF_8 = Charset.forName(UTF_8);
    /** GBK */
    public static final Charset CHARSET_GBK = Charset.forName(GBK);

    /**
     * Charsetオブジェクトに変換
     * 
     * @param charset
     * @return Charset
     */
    public static Charset charset(String charset)
    {
        return StringUtils.isEmpty(charset) ? Charset.defaultCharset() : Charset.forName(charset);
    }

    /**
     * 変換文字列の文字セットエンコーディング
     * 
     * @param source 文字列
     * @param srcCharset ソース文字セット、デフォルトISO-8859-1
     * @param destCharset ターゲット文字セット、デフォルトUTF-8
     * @return
     */
    public static String convert(String source, String srcCharset, String destCharset)
    {
        return convert(source, Charset.forName(srcCharset), Charset.forName(destCharset));
    }

    /**
     * 変換文字列の文字セットエンコーディング
     * 
     * @param source 文字列
     * @param srcCharset ソース文字セット、デフォルトISO-8859-1
     * @param destCharset ターゲット文字セット、デフォルトUTF-8
     * @return
     */
    public static String convert(String source, Charset srcCharset, Charset destCharset)
    {
        if (null == srcCharset)
        {
            srcCharset = StandardCharsets.ISO_8859_1;
        }

        if (null == destCharset)
        {
            destCharset = StandardCharsets.UTF_8;
        }

        if (StringUtils.isEmpty(source) || srcCharset.equals(destCharset))
        {
            return source;
        }
        return new String(source.getBytes(srcCharset), destCharset);
    }

    /**
     * システム文字セットエンコーディング
     *
     * @return システム文字セットエンコーディング
     */
    public static String systemCharset()
    {
        return Charset.defaultCharset().name();
    }
}
