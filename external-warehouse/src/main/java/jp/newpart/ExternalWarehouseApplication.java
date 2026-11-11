package jp.newpart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.TimeZone;

/**
 * 新・産直 Start Application
 * 
 * @author nextosd
 */
@SpringBootApplication
public class ExternalWarehouseApplication
{
    public static void main(String[] args)
    {
        // デフォルトロケールを日本語に設定
        LocaleContextHolder.setDefaultLocale(Locale.JAPAN);
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tokyo"));
        // OSのシステムプロキシ設定がJVMに引き継がれ、DB接続がSOCKS経由となり失敗する事象を回避する
        String socksProxyHost = System.getProperty("socksProxyHost");
        if ("127.0.0.1".equals(socksProxyHost) || "localhost".equals(socksProxyHost))
        {
            System.clearProperty("socksProxyHost");
            System.clearProperty("socksProxyPort");
        }
        SpringApplication.run(ExternalWarehouseApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  新部品管理システム｜外部倉庫管理 起動成功しました   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
