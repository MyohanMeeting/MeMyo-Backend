package meet.myo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    private final String emailUsername;
    private final String emailPassword;
    private final String mailHost;
    private final int mailPort;

    public MailConfig(
            @Value("${spring.mail.username}") String emailUsername,
            @Value("${spring.mail.password}") String emailPassword,
            @Value("${spring.mail.host}") String mailHost,
            @Value("${spring.mail.port}") int mailPort) {
        this.emailUsername = emailUsername;
        this.emailPassword = emailPassword;
        this.mailHost = mailHost;
        this.mailPort = mailPort;
    }


    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);
        mailSender.setUsername(emailUsername);
        mailSender.setPassword(emailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}