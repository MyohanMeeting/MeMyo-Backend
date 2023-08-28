package meet.myo.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    private final String certUrl;
    private final String resendUrl;

    public EmailService(JavaMailSender emailSender,
                        @Value("${client-env.local.host}") String host,
                        @Value("${client-env.local.port}") String port) {
        this.emailSender = emailSender;
        this.certUrl = String.format("http://%s:%s/certification", host, port);
        this.resendUrl = String.format("http://%s:%s/certification", host, port);
    }

    //메일 양식 작성
    public MimeMessage createEmailForm(String email, String certCode) throws MessagingException {
        String setFrom = "myohanmeeting@gmail.com"; //email-config 에 설정한 (보내는 사람)
        String title = "MyohanMeeting 회원가입 인증 메일"; //제목
        String content = String.format("""
유기묘 입양 서비스 <묘한만남>의 회원가입 인증 메일입니다.
본인이 요청하지 않았을 경우 링크를 누르지 마시고 고객센터에 연락해 주세요.

회원가입을 완료하려면 아래 링크를 클릭해 주세요.
%s?email=%s&certCode=%s

위 링크가 만료되었다면 아래 링크를 클릭해 주세요.
%s?email=%s
""", this.certUrl, email, certCode, this.resendUrl, email);

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email); //보낼 이메일 설정
        message.setSubject(title); //제목 설정
        message.setFrom(setFrom); //보내는 이메일
        message.setText(content); //내용 설정
        return message;
    }

    @Async("threadPoolTaskExecutor")
    public void sendEmail(String toEmail, String authNum) throws MessagingException, UnsupportedEncodingException {
        MimeMessage emailForm = createEmailForm(toEmail, authNum);
        emailSender.send(emailForm);
        //:TODO void?
    }
}