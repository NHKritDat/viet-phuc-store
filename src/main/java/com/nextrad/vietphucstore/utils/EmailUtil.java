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

    @Value("${RESET_PASSWORD_URL}")
    private String resetPasswordUrl;
    @Value("${VERIFY_EMAIL_URL}")
    private String verifyEmailUrl;

    @Async
    public void resetPassword(String email, String fullName, String token) {
        String subject = "Reset your password";
        String url = resetPasswordUrl + "?token=" + token;
        String content = """
                <div>
                    Dear %s,
                    <br>
                    If you want to reset your password, please <a href="%s" target="_blank">Click here</a>
                    <br>
                    <br>
                    <div style="border-top:1px solid #eaeaea; padding-top:10px;">
                        Best Regards,
                        <br>
                        Dap Viet
                        <br>
                    </div>
                </div>
                """.formatted(fullName, url);
        send(email, subject, content);
    }

    @Async
    public void verifyEmail(String email, String fullName, String token) {
        String subject = "Verify your email";
        String url = verifyEmailUrl + "?token=" + token;
        String content = """
                <div>
                    Dear %s,
                    <br>
                    If you want to verify your email, please <a href="%s" target="_blank">Click here</a>
                    <br>
                    <br>
                    <div style="border-top:1px solid #eaeaea; padding-top:10px;">
                        Best Regards,
                        <br>
                        Dap Viet
                        <br>
                    </div>
                </div>
                """.formatted(fullName, url);
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
