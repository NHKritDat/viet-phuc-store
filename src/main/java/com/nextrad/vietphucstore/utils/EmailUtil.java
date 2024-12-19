package com.nextrad.vietphucstore.utils;

import com.nextrad.vietphucstore.enums.error.ErrorCode;
import com.nextrad.vietphucstore.exceptions.AppException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailUtil {
    private final JavaMailSender javaMailSender;

    @Value("${VERIFY_EMAIL_SUBJECT}")
    private String verifyEmailSubject;
    @Value("${VERIFY_EMAIL_CONTENT}")
    private String verifyEmailContent;
    @Value("${VERIFY_EMAIL_URL}")
    private String verifyEmailUrl;
    @Value("${RESET_PASSWORD_SUBJECT}")
    private String resetPasswordSubject;
    @Value("${RESET_PASSWORD_CONTENT}")
    private String resetPasswordContent;
    @Value("${RESET_PASSWORD_URL}")
    private String resetPasswordUrl;

    @Async
    public void resetPassword(String email, String fullName, String token) {
        String subject = resetPasswordSubject;
        String url = resetPasswordUrl + "?token=" + token;
        String content = resetPasswordContent.formatted(fullName, url);
        send(email, subject, content);
    }

    @Async
    public void verifyEmail(String email, String fullName, String token) {
        String subject = verifyEmailSubject;
        String url = verifyEmailUrl + "?auth=" + token;
        String content = verifyEmailContent.formatted(fullName, url);
        send(email, subject, content);
    }

    private void send(String email, String subject, String content) {
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
