package jp.newpart.common.aws.ses;

import jp.newpart.common.utils.ValueUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

/**
 * AWS SESメール送信サービス
 * 
 * @author nextosd
 */
@Service
public class AwsSesEmailService {
    private static final Logger log = LoggerFactory.getLogger(AwsSesEmailService.class);
    
    @Autowired
    private SesClient sesClient;
    
    @Autowired
    private AwsSesConfig awsSesConfig;
    
    /**
     * シンプルなテキストメールを送信
     * 
     * @param to 受信者メールアドレス
     * @param subject メール件名
     * @param content メール内容
     * @return 送信成功かどうか
     */
    public boolean sendSimpleEmail(String to, String cc, String bcc, String subject, String content) {
        try {
            String[] toAddressList = to.split(";");

            Destination.Builder destinationBuilder = Destination.builder()
                    .toAddresses(toAddressList);

            if(ValueUtils.isNotEmpty(cc)){
                destinationBuilder.ccAddresses(cc.split(";"));
            }

            if(ValueUtils.isNotEmpty(bcc)){
                destinationBuilder.bccAddresses(bcc.split(";"));
            }

            Destination destination = destinationBuilder.build();

            Message message = Message.builder()
                    .subject(Content.builder()
                            .charset(awsSesConfig.getCharset())
                            .data(subject)
                            .build())
                    .body(Body.builder()
                            .text(Content.builder()
                                    .charset(awsSesConfig.getCharset())
                                    .data(content)
                                    .build())
                            .build())
                    .build();
            
            SendEmailRequest request = SendEmailRequest.builder()
                    .destination(destination)
                    .message(message)
                    .source(awsSesConfig.getFromEmail())
                    .build();
            
            sesClient.sendEmail(request);
            log.info("シンプルメール送信成功 - 受信者: {}, 件名: {}", to, subject);
            return true;
            
        } catch (SdkException e) {
            log.error("シンプルメール送信失敗 - 受信者: {}, 件名: {}, エラー: {}", to, subject, e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * HTMLメールを送信
     * 
     * @param to 受信者メールアドレス
     * @param subject メール件名
     * @param htmlContent HTMLメール内容
     * @return 送信成功かどうか
     */
    public boolean sendHtmlEmail(String to, String cc, String bcc, String subject, String htmlContent) {
        try {
            Destination.Builder destinationBuilder = Destination.builder()
                    .toAddresses(to.split(";"));

            if(ValueUtils.isNotEmpty(cc)){
                destinationBuilder.ccAddresses(cc.split(";"));
            }

            if(ValueUtils.isNotEmpty(bcc)){
                destinationBuilder.bccAddresses(bcc.split(";"));
            }

            Destination destination = destinationBuilder.build();

            Message message = Message.builder()
                    .subject(Content.builder()
                            .charset(awsSesConfig.getCharset())
                            .data(subject)
                            .build())
                    .body(Body.builder()
                            .html(Content.builder()
                                    .charset(awsSesConfig.getCharset())
                                    .data(htmlContent)
                                    .build())
                            .build())
                    .build();
            
            SendEmailRequest request = SendEmailRequest.builder()
                    .destination(destination)
                    .message(message)
                    .source(awsSesConfig.getFromEmail())
                    .build();

            sesClient.sendEmail(request);
            log.info("HTMLメール送信成功 - 受信者: {}, 件名: {}", to, subject);
            return true;
            
        } catch (SdkException e) {
            log.error("HTMLメール送信失敗 - 受信者: {}, 件名: {}, エラー: {}", to, subject, e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 添付ファイル付きメールを送信（ファイルパス経由）
     * 
     * @param to 受信者メールアドレス
     * @param subject メール件名
     * @param content メール内容
     * @param attachmentPath 添付ファイルパス
     * @return 送信成功かどうか
     */
    public boolean sendEmailWithAttachment(String to, String cc, String bcc, String subject, String content,
                                          String attachmentPath) {
        try {
            // ファイル内容を読み取り
            java.nio.file.Path path = java.nio.file.Paths.get(attachmentPath);
            byte[] attachmentContent = java.nio.file.Files.readAllBytes(path);
            String attachmentName = path.getFileName().toString();
            
            return sendEmailWithAttachment(to, cc, bcc, subject, content, attachmentName, attachmentContent);
        } catch (Exception e) {
            log.error("添付ファイル読み取り失敗 - ファイルパス: {}, エラー: {}", attachmentPath, e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 添付ファイル付きメールを送信
     * 
     * @param to 受信者メールアドレス
     * @param subject メール件名
     * @param content メール内容
     * @param attachmentName 添付ファイル名
     * @param attachmentContent 添付ファイル内容
     * @return 送信成功かどうか
     */
    public boolean sendEmailWithAttachment(String to, String cc, String bcc, String subject, String content,
                                          String attachmentName, byte[] attachmentContent) {
        try {
            Destination.Builder destinationBuilder = Destination.builder()
                    .toAddresses(to.split(";"));

            if(ValueUtils.isNotEmpty(cc)){
                destinationBuilder.ccAddresses(cc.split(";"));
            }

            if(ValueUtils.isNotEmpty(bcc)){
                destinationBuilder.bccAddresses(bcc.split(";"));
            }

            Destination destination = destinationBuilder.build();
            
            Message message = Message.builder()
                    .subject(Content.builder()
                            .charset(awsSesConfig.getCharset())
                            .data(subject)
                            .build())
                    .body(Body.builder()
                            .text(Content.builder()
                                    .charset(awsSesConfig.getCharset())
                                    .data(content)
                                    .build())
                            .build())
                    .build();
            
            SendEmailRequest request = SendEmailRequest.builder()
                    .destination(destination)
                    .message(message)
                    .source(awsSesConfig.getFromEmail())
                    .build();
            
            sesClient.sendEmail(request);
            log.info("添付ファイル付きメール送信成功 - 受信者: {}, 件名: {}, 添付ファイル: {}", to, subject, attachmentName);
            return true;
            
        } catch (SdkException e) {
            log.error("添付ファイル付きメール送信失敗 - 受信者: {}, 件名: {}, 添付ファイル: {}, エラー: {}", 
                    to, subject, attachmentName, e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * バッチメール送信
     * 
     * @param recipients 受信者リスト
     * @param subject メール件名
     * @param content メール内容
     * @return 成功送信数
     */
    public int sendBulkEmail(java.util.List<String> recipients, String cc, String bcc, String subject, String content) {
        int successCount = 0;
        for (String recipient : recipients) {
            if (sendSimpleEmail(recipient, cc, bcc, subject, content)) {
                successCount++;
            }
        }
        log.info("バッチメール送信完了 - 総数: {}, 成功: {}", recipients.size(), successCount);
        return successCount;
    }
    
    /**
     * ユーザー登録成功メールを送信
     * 
     * @param to 受信者メールアドレス
     * @param username ユーザー名
     * @return 送信成功かどうか
     */
    public boolean sendUserRegistrationEmail(String to, String username) {
        String subject = "External Warehouseシステムへのご登録ありがとうございます";
        String content = String.format("""
            尊敬なる %s 様：
            
            External Warehouseシステムにご登録いただき、誠にありがとうございます。
            
            お客様のアカウントは正常に作成されました。
            システムにログインして各種機能をご利用いただけます。
            
            何かご質問がございましたら、システム管理者までお問い合わせください。
            
            ご利用ありがとうございます。
            
            External Warehouseシステムチーム
            """, username);
        
        return sendSimpleEmail(to, null, null, subject, content);
    }
    
    /**
     * パスワードリセットメールを送信
     * 
     * @param to 受信者メールアドレス
     * @param username ユーザー名
     * @param resetUrl パスワードリセットリンク
     * @return 送信成功かどうか
     */
    public boolean sendPasswordResetEmail(String to, String username, String resetUrl) {
        String subject = "External Warehouseシステム - パスワードリセット";
        String htmlContent = String.format("""
            <html>
            <body>
                <h3>尊敬なる %s 様：</h3>
                <p>External Warehouseシステムのログインパスワードのリセットをリクエストされました。</p>
                <p>以下のリンクをクリックしてパスワードをリセットしてください：</p>
                <p><a href="%s" style="color: #1890ff; text-decoration: none;">パスワードリセット</a></p>
                <p>リンクがクリックできない場合は、以下のアドレスをブラウザにコピーして開いてください：</p>
                <p style="color: #666; font-size: 12px;">%s</p>
                <p>このリンクは24時間後に無効になります。</p>
                <p>パスワードリセットをリクエストされていない場合は、このメールを無視してください。</p>
                <br>
                <p>External Warehouseシステムチーム</p>
            </body>
            </html>
            """, username, resetUrl, resetUrl);
        
        return sendHtmlEmail(to, null, null, subject, htmlContent);
    }
    
    /**
     * システム通知メールを送信
     * 
     * @param to 受信者メールアドレス
     * @param title 通知タイトル
     * @param message 通知内容
     * @return 送信成功かどうか
     */
    public boolean sendSystemNotificationEmail(String to, String title, String message) {
        String subject = "External Warehouseシステム通知 - " + title;
        String htmlContent = String.format("""
            <html>
            <body>
                <h3>%s</h3>
                <p>%s</p>
                <br>
                <p style="color: #999; font-size: 12px;">このメールはシステムから自動送信されています。返信しないでください。</p>
            </body>
            </html>
            """, title, message);
        
        return sendHtmlEmail(to, null, null, subject, htmlContent);
    }
}