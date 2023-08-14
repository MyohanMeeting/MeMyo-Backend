package meet.myo.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    

    //메일 양식 작성
    public MimeMessage createEmailForm(String email, String authNum) throws MessagingException {
        String setFrom = "myohanmeeting@gmail.com"; //email-config 에 설정한 (보내는 사람)
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
        MimeMessage emailForm = createEmailForm(toEmail, authNum);
        emailSender.send(emailForm);
        //인증코드 반환
        return authNum;
    }
}