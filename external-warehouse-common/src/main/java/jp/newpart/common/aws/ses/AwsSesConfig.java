package jp.newpart.common.aws.ses;

import jp.newpart.common.utils.ValueUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.SesClientBuilder;

/**
 * AWS SES設定クラス
 * 
 * @author nextosd
 */
@Configuration
@ConfigurationProperties(prefix = "aws.ses")
public class AwsSesConfig {
    
    /**
     * AWSリージョン
     */
    private String region;
    
    /**
     * AWSアクセスキーID
     */
    private String accessKeyId;
    
    /**
     * AWSシークレットアクセスキー
     */
    private String secretAccessKey;
    
    /**
     * 送信者メールアドレス
     */
    private String fromEmail;

    /**
     * メールエンコーディング
     */
    private String charset = "UTF-8";
    
    /**
     * エンドポイントURL (LocalStackなどのローカル開発環境用)
     */
    private String endpointUrl;
    
    /**
     * SESクライアントを作成
     * 
     * @return SesClientインスタンス
     */
    @Bean
    public SesClient sesClient() {

        AwsCredentialsProvider awsCredentialsProvider = null;

        if(ValueUtils.isNotEmpty(accessKeyId) && ValueUtils.isNotEmpty(secretAccessKey)){
            AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
            awsCredentialsProvider = StaticCredentialsProvider.create(awsCreds);
        } else {
            // IAM
            awsCredentialsProvider = DefaultCredentialsProvider.create();
        }

        SesClientBuilder builder = SesClient.builder()
                .region(Region.of(region))
                .credentialsProvider(awsCredentialsProvider);
        
        // エンドポイントURLが設定されている場合はカスタムエンドポイントを使用（LocalStack用）
        if (endpointUrl != null && !endpointUrl.trim().isEmpty()) {
            builder.endpointOverride(java.net.URI.create(endpointUrl));
        }
        
        return builder.build();
    }
    
    // GetterとSetterメソッド
    public String getRegion() {
        return region;
    }
    
    public void setRegion(String region) {
        this.region = region;
    }
    
    public String getAccessKeyId() {
        return accessKeyId;
    }
    
    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }
    
    public String getSecretAccessKey() {
        return secretAccessKey;
    }
    
    public void setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
    }
    
    public String getFromEmail() {
        return fromEmail;
    }
    
    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }
    
    public String getCharset() {
        return charset;
    }
    
    public void setCharset(String charset) {
        this.charset = charset;
    }
    
    public String getEndpointUrl() {
        return endpointUrl;
    }
    
    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }
}