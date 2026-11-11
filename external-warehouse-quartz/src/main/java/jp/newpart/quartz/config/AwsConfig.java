package jp.newpart.quartz.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * AWS和邮件配置类
 * 
 * @author nextosd
 * @version 1.0
 */
@Configuration
public class AwsConfig {
    
    // AWS S3配置
    @Value("${aws.s3.access-key:test}")
    private String awsAccessKey;
    
    @Value("${aws.s3.secret-key:test}")
    private String awsSecretKey;
    
    @Value("${aws.s3.region:us-east-1}")
    private String awsRegion;
    
    @Value("${aws.s3.bucket-name:my-unencrypted-bucket}")
    private String bucketName;
    
    // AWS SES配置
    @Value("${aws.ses.access-key:test}")
    private String sesAccessKey;
    
    @Value("${aws.ses.secret-key:test}")
    private String sesSecretKey;
    
    @Value("${aws.ses.region:us-east-1}")
    private String sesRegion;
    
    // Getter方法用于在其他地方获取配置值
    
    public String getAwsAccessKey() {
        return awsAccessKey;
    }
    
    public String getAwsSecretKey() {
        return awsSecretKey;
    }
    
    public String getAwsRegion() {
        return awsRegion;
    }
    
    public String getBucketName() {
        return bucketName;
    }
    
    public String getSesAccessKey() {
        return sesAccessKey;
    }
    
    public String getSesSecretKey() {
        return sesSecretKey;
    }
    
    public String getSesRegion() {
        return sesRegion;
    }
}