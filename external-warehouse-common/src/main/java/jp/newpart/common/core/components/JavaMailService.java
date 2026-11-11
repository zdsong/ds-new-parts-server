package jp.newpart.common.core.components;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.MessageUtils;
import jp.newpart.common.utils.ValueUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * JavaMail
 */
@Component
@Slf4j
public class JavaMailService {

    /**
     * メール送信インスタンス
     */
    @Autowired
    private JavaMailSender mailSender;

    /**
     * 送信元メールアドレス
     */
    @Value("${spring.mail.from-email}")
    private String fromEmail;

    /**
     * 簡単なテキストメールを送信する
     *
     * @param to      受け取る人のメールアドレス
     * @param subject メールの件名
     * @param text    メールの本文
     */
    public void sendSimpleMail(String to, String cc, String bcc, String subject, String text) {
        sendMail(to, cc, bcc, subject, text, false);
    }

    /**
     * HTMLメールを送信する
     *
     * @param to      受け取る人のメールアドレス
     * @param subject メールの件名
     * @param html    HTML形式のメール本文
     */
    public void sendHtmlMail(String to, String cc, String bcc, String subject, String html) {
        sendMail(to, cc, bcc, subject, html, true);
    }

    /**
     * メールを送信する共通メソッド
     *
     * @param to      受け取る人のメールアドレス
     * @param subject メールの件名
     * @param content メールの本文
     * @param isHtml  本文がHTML形式か否か
     */
    private void sendMail(String to,  String cc, String bcc, String subject, String content, boolean isHtml) {
        // パラメータの空チェック
        if (to == null || to.trim().isEmpty() || subject == null || subject.trim().isEmpty() || content == null || content.trim().isEmpty()) {
            log.error("Invalid email parameters: to={}, subject={}, content={}", to, subject, content);
            throw new IllegalArgumentException("Invalid email parameters");
        }

        try {
            // メールメッセージの作成
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, isHtml);
            helper.setFrom(fromEmail);
            helper.setTo(to.split(";"));

            if(ValueUtils.isNotEmpty(cc)){
                helper.setCc(cc.split(";"));
            }
            if(ValueUtils.isNotEmpty(bcc)){
                helper.setBcc(bcc.split(";"));
            }

            helper.setSubject(subject);
            helper.setText(content, isHtml);
            // メールの送信
            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email to: {}. Error: {}", to, e.getMessage(), e);
            throw new ServiceException(MessageUtils.message("E0015"));
        }
    }
}
