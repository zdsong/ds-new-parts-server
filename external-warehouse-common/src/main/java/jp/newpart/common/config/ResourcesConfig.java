package jp.newpart.common.config;

import jp.newpart.common.constant.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 一般的な設定
 *
 * @author nextosd
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /** ローカルファイルアップロードパス */
        registry.addResourceHandler(Constants.RESOURCE_PREFIX + "/**")
                .addResourceLocations("file:" + AppConfig.getProfile() + "/");
    }

    /**
     * クロスドメイン設定
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // アクセス元アドレスの設定
        config.addAllowedOriginPattern("*");
        // アクセス元リクエストヘッダーの設定
        config.addAllowedHeader("*");
        // アクセス元リクエストメソッドの設定
        config.addAllowedMethod("*");
        // 有効期間 1800秒
        config.setMaxAge(1800L);
        // 映射パスの追加、すべてのリクエストをインターセプト
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // 新しいCorsFilterを返す
        return new CorsFilter(source);
    }
}
