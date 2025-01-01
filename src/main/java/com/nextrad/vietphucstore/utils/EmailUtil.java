package com.nextrad.vietphucstore.utils;

import com.nextrad.vietphucstore.entities.order.Order;
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
    private final ImagesUtil imagesUtil;

    @Value("${RESET_PASSWORD_URL}")
    private String resetPasswordUrl;
    @Value("${VERIFY_EMAIL_URL}")
    private String verifyEmailUrl;

    public void orderDetail(Order order) {
        String subject = "Order detail";
        String content =
                """
                        <div>
                            Dear %s,
                            <br>
                            Your order has been placed successfully. Your order code is %s.
                            <br><br>
                            <strong>Order Details:</strong>
                            <br>
                            <ul>
                                <li><strong>Address:</strong> %s</li>
                                <li><strong>Phone:</strong> %s</li>
                                <li><strong>Total Product Value:</strong> %s</li>
                                <li><strong>Shipping Fee:</strong> %s</li>
                                <li><strong>Payment Method:</strong> %s</li>
                                <li><strong>Total Price:</strong> %s</li>
                            </ul>
                            <br>
                            <strong>Products:</strong>
                            <table style="width:100%; border-collapse: collapse; border: 1px solid #eaeaea;">
                                <thead>
                                    <tr style="background-color:#f8f8f8;">
                                        <th style="border: 1px solid #eaeaea; padding: 8px;">Product Name</th>
                                        <th style="border: 1px solid #eaeaea; padding: 8px;">Image</th>
                                        <th style="border: 1px solid #eaeaea; padding: 8px;">Size</th>
                                        <th style="border: 1px solid #eaeaea; padding: 8px;">Quantity</th>
                                        <th style="border: 1px solid #eaeaea; padding: 8px;">Unit Price</th>
                                    </tr>
                                </thead>
                                <tbody>
                        """
                        .formatted(
                                order.getName(),
                                order.getId(),
                                order.getAddress() + ", " + order.getDistrict() + ", " + order.getProvince(),
                                order.getPhone(),
                                order.getProductTotal(),
                                order.getShippingFee(),
                                order.getPaymentMethod(),
                                order.getProductTotal() + order.getShippingFee()
                        ) +
                        order.getOrderDetails().stream().map(orderDetail ->
                                        """
                                                <tr>
                                                    <td style="border: 1px solid #eaeaea; padding: 8px;">%s</td>
                                                    <td style="border: 1px solid #eaeaea; padding: 8px;">
                                                        <img src="%s" style="width: 50px; height: 50px;">
                                                    </td>
                                                    <td style="border: 1px solid #eaeaea; padding: 8px;">%s</td>
                                                    <td style="border: 1px solid #eaeaea; padding: 8px;">%s</td>
                                                    <td style="border: 1px solid #eaeaea; padding: 8px;">%s</td>
                                                </tr>
                                                """
                                                .formatted(
                                                        orderDetail.getProductQuantity().getProduct().getName(),
                                                        imagesUtil.convertStringToImages(
                                                                orderDetail.getProductQuantity().getProduct().getPictures()
                                                        ).get(0),
                                                        orderDetail.getProductQuantity().getProductSize().getName(),
                                                        orderDetail.getQuantity(),
                                                        orderDetail.getProductQuantity().getProduct().getUnitPrice())
                                )
                                .reduce("", String::concat) +
                        """
                                        </tbody>
                                    </table>
                                    <br>
                                    <div style="border-top:1px solid #eaeaea; padding-top:10px;">
                                        Best Regards,
                                        <br>
                                        Dap Viet
                                        <br>
                                    </div>
                                </div>
                                """;
        send(order.getEmail(), subject, content);
    }

    @Async
    public void resetPassword(String email, String name, String token) {
        String subject = "Reset your password";
        String url = resetPasswordUrl + token;
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
                """.formatted(name, url);
        send(email, subject, content);
    }

    @Async
    public void verifyEmail(String email, String name, String token) {
        String subject = "Verify your email";
        String url = verifyEmailUrl + token;
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
                """.formatted(name, url);
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
