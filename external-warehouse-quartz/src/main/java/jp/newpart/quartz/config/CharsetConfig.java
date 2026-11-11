package jp.newpart.quartz.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

import jakarta.servlet.Filter;
import java.nio.charset.StandardCharsets;

/**
 * 文字コード設定クラス
 * ログとコンソールの文字化け問題を解決する
 */
@Configuration
public class CharsetConfig {

    /**
     * 文字コードフィルターを設定する
     * すべてのリクエストとレスポンスがUTF-8エンコーディングを使用することを確認する
     */
    @Bean("customCharacterEncodingFilter")
    public FilterRegistrationBean<Filter> characterEncodingFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding(StandardCharsets.UTF_8.name());
        filter.setForceEncoding(true);
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
    
    /**
     * システムのデフォルト文字コードを設定
     */
    @Bean
    public String setDefaultCharset() {
        // システムのデフォルト文字コードをUTF-8に設定する
        System.setProperty("file.encoding", StandardCharsets.UTF_8.name());
        System.setProperty("sun.jnu.encoding", StandardCharsets.UTF_8.name());
        
        // コンソール出力エンコーディングを設定する
        System.setProperty("console.encoding", StandardCharsets.UTF_8.name());
        
        return "Charset configured to UTF-8";
    }
}