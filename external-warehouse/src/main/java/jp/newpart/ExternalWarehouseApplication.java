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
        SpringApplication.run(ExternalWarehouseApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  新・産直 起動成功しました   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
