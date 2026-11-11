package jp.newpart.web.core.config;

import jp.newpart.common.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Swagger2 Config
 * 
 * @author nextosd
 */
@Configuration
public class SwaggerConfig
{
    /** システム設定 */
    @Autowired
    private AppConfig appConfig;
    
    /**
     * カスタムOpenAPIオブジェクト
     */
    @Bean
    public OpenAPI customOpenApi()
    {
        return new OpenAPI().components(new Components()
            // 資格認定の要求ヘッダの設定
            .addSecuritySchemes("apikey", securityScheme()))
            .addSecurityItem(new SecurityRequirement().addList("apikey"))
            .info(getApiInfo());
    }
    
    @Bean
    public SecurityScheme securityScheme()
    {
        return new SecurityScheme()
            .type(SecurityScheme.Type.APIKEY)
            .name("Authorization")
            .in(SecurityScheme.In.HEADER)
            .scheme("Bearer");
    }
    
    /**
     * 要約情報の追加
     */
    public Info getApiInfo()
    {
        return new Info()
            // タイトルの設定
            .title("タイトル: 農家（農業法人）支援_インタフェース・ドキュメント")
            .description("ヤマト運輸 農家（農業法人）支援 - 新・産直 システム")
            .contact(new Contact().name(appConfig.getName()))
            .version("Version:" + appConfig.getVersion());
    }
}
