package jp.newpart.common.utils;

/**
 * ログファイルの処理と記録
 * 
 * @author nextosd
 */
public class LogUtils
{
    public static String getBlock(Object msg)
    {
        if (msg == null)
        {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }
}
