package com.nextrad.vietphucstore.utils;

import com.nextrad.vietphucstore.enums.ErrorCode;
import com.nextrad.vietphucstore.exceptions.AppException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailUtil {
    private final JavaMailSender javaMailSender;

    private void send(String email, String subject, String content){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content, true);
        } catch (MessagingException e) {
            throw new AppException(ErrorCode.INVALID_EMAIL);
        }
        javaMailSender.send(mimeMessage);
    }
}
