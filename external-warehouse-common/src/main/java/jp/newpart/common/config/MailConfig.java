package jp.newpart.common.config;

import jp.newpart.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Mail Config
 *
 * @author nextosd
 */
@Configuration
public class MailConfig {

    @Autowired
    private Environment env;

    @Bean
    public JavaMailSender javaMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        String host = env.getProperty("spring.mail.host");

        if(StringUtils.isNotEmpty(host)) {
            int port = env.getProperty("spring.mail.port", Integer.class);
            String username = env.getProperty("spring.mail.username");
            String password = env.getProperty("spring.mail.password");

            mailSender.setHost(host);
            mailSender.setPort(port);
            mailSender.setUsername(username);
            mailSender.setPassword(password);
            mailSender.setDefaultEncoding("UTF-8");

            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", env.getProperty("spring.mail.properties.mail.smtp.auth"));
            props.put("mail.smtp.starttls.enable", env.getProperty("spring.mail.properties.mail.smtp.starttls.enable"));
            props.put("mail.smtp.starttls.required", env.getProperty("spring.mail.properties.mail.smtp.starttls.required"));
            props.put("mail.smtp.connectiontimeout", env.getProperty("spring.mail.properties.mail.smtp.connectiontimeout"));
            props.put("mail.smtp.timeout", env.getProperty("spring.mail.properties.mail.smtp.timeout"));
            props.put("mail.smtp.writetimeout", env.getProperty("spring.mail.properties.mail.smtp.writetimeout"));
            props.put("mail.debug", env.getProperty("spring.mail.debug"));
        }

        return mailSender;
    }
}
