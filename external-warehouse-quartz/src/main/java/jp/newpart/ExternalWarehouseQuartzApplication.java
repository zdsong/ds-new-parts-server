package jp.newpart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.ApplicationContext;

import java.util.Locale;
import java.util.TimeZone;

/**
 * Start Application
 *
 * @author nextosd
 */
@SpringBootApplication
public class ExternalWarehouseQuartzApplication {

    public static void main(String[] args)
    {
        // デフォルトロケールを日本語に設定
        LocaleContextHolder.setDefaultLocale(Locale.JAPAN);
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tokyo"));
        ApplicationContext context = SpringApplication.run(ExternalWarehouseQuartzApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  Quartz 起動成功しました   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
