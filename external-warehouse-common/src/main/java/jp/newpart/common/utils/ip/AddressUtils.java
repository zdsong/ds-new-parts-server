package jp.newpart.common.utils.ip;

import jp.newpart.common.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * アドレスクラスの取得
 * 
 * @author nextosd
 */
public class AddressUtils
{
    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    // 不明なアドレス
    public static final String UNKNOWN = "XX XX";

    public static String getRealAddressByIP(String ip)
    {
        // イントラネットは照会しない
        if (IpUtils.internalIp(ip))
        {
            return "イントラネットIP";
        }
        if (AppConfig.isAddressEnabled())
        {
            // クエリーIPサイトを使用したIP情報の解析
            // 当分実現しない
            return ip;
        }
        return UNKNOWN;
    }
}
