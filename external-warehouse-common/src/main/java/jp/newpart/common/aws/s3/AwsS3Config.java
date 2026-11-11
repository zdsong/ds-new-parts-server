package jp.newpart.common.aws.s3;

import jp.newpart.common.utils.ValueUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

/**
 * AWS S3設定クラス
 * 
 * @author nextosd
 */
@Configuration
@ConfigurationProperties(prefix = "aws.s3")
public class AwsS3Config {
    
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
     * S3バケット名
     */
    private String bucketName;
    
    /**
     * ファイルパスプレフィックス
     */
    private String pathPrefix = "";
    
    /**
     * エンドポイントURL (LocalStackなどのローカル開発環境用)
     */
    private String endpointUrl;
    
    /**
     * ファイルアップロードサイズ制限（MB）
     */
    private long maxFileSize = 100;
    
    /**
     * S3クライアントを作成
     * 
     * @return S3Clientインスタンス
     */
    @Bean
    public S3Client s3Client() {
        AwsCredentialsProvider awsCredentialsProvider = null;

        if(ValueUtils.isNotEmpty(accessKeyId) && ValueUtils.isNotEmpty(secretAccessKey)){
            AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
            awsCredentialsProvider = StaticCredentialsProvider.create(awsCreds);
        } else {
            // IAM
            awsCredentialsProvider = DefaultCredentialsProvider.create();
        }

        S3ClientBuilder builder = S3Client.builder()
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
    
    public String getBucketName() {
        return bucketName;
    }
    
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
    
    public String getPathPrefix() {
        return pathPrefix;
    }
    
    public void setPathPrefix(String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }
    
    public String getEndpointUrl() {
        return endpointUrl;
    }
    
    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }
    
    public long getMaxFileSize() {
        return maxFileSize;
    }
    
    public void setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }
}