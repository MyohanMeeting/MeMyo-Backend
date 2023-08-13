package meet.myo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import meet.myo.domain.EmailCertification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
public class EmailService {

    //의존성 주입을 통해서 필요한 객체를 가져온다.
    private final JavaMailSender emailSender;
    // 타임리프를사용하기 위한 객체를 의존성 주입으로 가져온다
    private String authNum; //랜덤 인증 코드

    @Autowired //TODO: ? RequiredArgument 로 하면 오류 발생, 하지만 컴파일하는 데에는 문제가 없음
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    //메일 양식 작성
    public MimeMessage createEmailForm(String email, String authNum) throws MessagingException, UnsupportedEncodingException {

        String setFrom = "myohanmeeting@gmail.com"; //email-config에 설정한 (보내는 사람)
        String toEmail = email; //받는 사람
        String title = "MyohanMeeting 회원가입 인증 번호"; //제목
        String content = "인증 번호: " + authNum; //내용

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email); //보낼 이메일 설정
        message.setSubject(title); //제목 설정
        message.setFrom(setFrom); //보내는 이메일
        message.setText(content); //내용 설정

        return message;
    }

    //실제 메일 전송
    public String sendEmail(String toEmail, String authNum) throws MessagingException, UnsupportedEncodingException {

        //메일전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailForm(toEmail, authNum);
        //실제 메일 전송
        emailSender.send(emailForm);

        return authNum; //인증 코드 반환
    }
}