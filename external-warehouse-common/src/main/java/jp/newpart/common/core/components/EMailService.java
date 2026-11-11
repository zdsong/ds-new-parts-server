package jp.newpart.common.core.components;

import jp.newpart.common.aws.ses.AwsSesEmailService;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.MessageUtils;
import jp.newpart.common.utils.ValueUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * EMail Service
 */
@Component
public class EMailService {

    @Autowired
    private AwsSesEmailService awsSesEmailService;

    @Autowired
    private JavaMailService mailService;

    @Value("${farm.mailSendType:javamail}")
    private String mailSendType;

    /**
     * 簡単なテキストメールを送信する
     *
     * @param to      受け取る人のメールアドレス
     * @param subject メールの件名
     * @param text    メールの本文
     */
    public void sendSimpleMail(String to, String subject, String text) {
        if(ValueUtils.isEqual(mailSendType, "javamail")){
            // Email 送信
            mailService.sendSimpleMail(to, null, null, subject, text);
        } else if (awsSesEmailService.sendSimpleEmail(to, null, null, subject, text) == false) {
            // 認証コードが送信失敗しました。管理者にお問い合わせください。
            throw new ServiceException(MessageUtils.message("E0015"));
        }
    }

    /**
     * HTMLメールを送信する
     *
     * @param to      受け取る人のメールアドレス
     * @param subject メールの件名
     * @param html    HTML形式のメール本文
     */
    public void sendHtmlMail(String to, String subject, String html) {
        if(ValueUtils.isEqual(mailSendType, "javamail")){
            // Email 送信
            mailService.sendHtmlMail(to, null, null, subject, html);
        } else if (awsSesEmailService.sendHtmlEmail(to, null, null, subject, html) == false) {
            // 認証コードが送信失敗しました。管理者にお問い合わせください。
            throw new ServiceException(MessageUtils.message("E0015"));
        }
    }

    /**
     * 簡単なテキストメールを送信する
     *
     * @param to      受け取る人のメールアドレス
     * @param subject メールの件名
     * @param text    メールの本文
     */
    public void sendSimpleMail(String to, String cc, String bcc, String subject, String text) {
        if(ValueUtils.isEqual(mailSendType, "javamail")){
            // Email 送信
            mailService.sendSimpleMail(to, cc, bcc, subject, text);
        } else if (awsSesEmailService.sendSimpleEmail(to, cc, bcc, subject, text) == false) {
            // 認証コードが送信失敗しました。管理者にお問い合わせください。
            throw new ServiceException(MessageUtils.message("E0015"));
        }
    }

    /**
     * HTMLメールを送信する
     *
     * @param to      受け取る人のメールアドレス
     * @param subject メールの件名
     * @param html    HTML形式のメール本文
     */
    public void sendHtmlMail(String to, String cc, String bcc, String subject, String html) {
        if(ValueUtils.isEqual(mailSendType, "javamail")){
            // Email 送信
            mailService.sendHtmlMail(to, cc, bcc, subject, html);
        } else if (awsSesEmailService.sendHtmlEmail(to, cc, bcc, subject, html) == false) {
            // 認証コードが送信失敗しました。管理者にお問い合わせください。
            throw new ServiceException(MessageUtils.message("E0015"));
        }
    }
}

