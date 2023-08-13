package meet.myo.controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import meet.myo.service.EmailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

//    @PostMapping("login/mailConfirm")
//    public String mailConfirm(@RequestBody EmailAuthRequestDto emailDto) throws MessagingException, UnsupportedEncodingException {
//
//        String authCode = emailService.sendEmail(emailDto.getEmail());
//        //return authCode;
//    }
}