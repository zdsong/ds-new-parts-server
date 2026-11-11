package jp.newpart.common.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jp.newpart.common.resolver.DecryptFieldArgumentResolver;
import jp.newpart.common.resolver.DecryptRequestBodyConverter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.TimeZone;

/**
 * Web設定クラス、Spring MVCの機能をカスタマイズするためにWebMvcConfigurerインターフェースを実装します。
 *
 * @author nextosd
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * ObjectMapperを作成し、設定を行います。
     *
     * @return 設定済みのObjectMapperインスタンス
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        // Java配列でJSON配列を解析する特性を無効にし、型の安全性を確保します。
        objectMapper.disable(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY);
        // 反直列化時に未知のプロパティが存在しても例外をスローしないようにします。
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // タイムゾーンを設定します。
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        return objectMapper;
    }

    /**
     * カスタムの引数解釈器を追加します。
     *
     * @param resolvers 引数解釈器リスト
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // フィールド復号化をサポートするためにDecryptFieldArgumentResolverを追加します。
        resolvers.add(new DecryptFieldArgumentResolver());
    }

    /**
     * カスタムメッセージコンバーターを設定します。
     *
     * @param converters メッセージコンバーターのリスト
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // リクエストボディの復号化をサポートするためにDecryptRequestBodyConverterを作成して追加します。
        DecryptRequestBodyConverter decryptConverter = new DecryptRequestBodyConverter(objectMapper());
        // converters.add(0, decryptConverter);  // springdoc v3/api-docs return base64
        converters.add(decryptConverter);
    }


}
