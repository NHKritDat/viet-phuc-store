package com.nextrad.vietphucstore.utils;

import com.nextrad.vietphucstore.entities.order.Order;
import com.nextrad.vietphucstore.entities.order.OrderDetail;
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

    @Async
    public void orderDetail(Order order) {
        String subject = "Chi tiết đơn hàng từ Đắp Việt";
        String header = """
                <div>
                    Chào %s,
                    <br>
                    Cảm ơn bạn đã đặt hàng tại Đắp Việt. Dưới đây là chi tiết đơn hàng của bạn:
                    <br><br>
                """.formatted(order.getName());
        StringBuilder body = new StringBuilder("<strong>Order ID:</strong>" + order.getId() + " <br><ul><li><strong>Địa chỉ:</strong> "
                + order.getAddress() + ", " + order.getDistrict() + ", " + order.getProvince() +
                "</li><li><strong>Người đặt:</strong> " + order.getName() +
                "</li><li><strong>Số điện thoại:</strong> " + order.getPhone() +
                "</li><li><strong>Tổng giá sản phẩm:</strong> " + order.getProductTotal() +
                "</li><li><strong>Phí vận chuyển:</strong> " + order.getShippingFee() +
                "</li><li><strong>Phương thức thanh toán:</strong> " + order.getPaymentMethod() +
                "</li><li><strong>Tổng giá:</strong> " + (order.getProductTotal() + order.getShippingFee()) +
                "</li></ul><br><strong>Sản phẩm:</strong>" +
                "<table style=\"width:100%; border-collapse: collapse; border: 1px solid #eaeaea;\">" +
                "<thead><tr style=\"background-color:#f8f8f8;\">" +
                "<th style=\"border: 1px solid #eaeaea; padding: 8px;\">Tên sản phẩm</th>" +
                "<th style=\"border: 1px solid #eaeaea; padding: 8px;\">Ảnh</th>" +
                "<th style=\"border: 1px solid #eaeaea; padding: 8px;\">Kích cỡ</th>" +
                "<th style=\"border: 1px solid #eaeaea; padding: 8px;\">Số lượng</th>" +
                "<th style=\"border: 1px solid #eaeaea; padding: 8px;\">Đơn giá</th></tr></thead><tbody>");
        for (OrderDetail od : order.getOrderDetails()) {
            body.append("<tr><td style=\"border: 1px solid #eaeaea; padding: 8px;\">")
                    .append(od.getProductQuantity().getProduct().getName())
                    .append("</td><td style=\"border: 1px solid #eaeaea; padding: 8px;\"><img src=\"")
                    .append(imagesUtil.convertStringToImages(od.getProductQuantity().getProduct().getPictures()).get(0))
                    .append("\" style=\"width:50px; height:auto;\"></td>")
                    .append("<td style=\"border: 1px solid #eaeaea; padding: 8px;\">")
                    .append(od.getProductQuantity().getProductSize().getName())
                    .append("</td><td style=\"border: 1px solid #eaeaea; padding: 8px;\">")
                    .append(od.getQuantity())
                    .append("</td><td style=\"border: 1px solid #eaeaea; padding: 8px;\">")
                    .append(od.getProductQuantity().getProduct().getUnitPrice())
                    .append("</td></tr>");
        }
        body.append("</tbody></table><br><div style=\"border-top:1px solid #eaeaea; padding-top:10px;\">")
                .append("Trân trọng,<br>Đắp Việt<br></div></div>");
        send(order.getEmail(), subject, header + body);
    }

    @Async
    public void resetPassword(String email, String name, String token) {
        String subject = "Cài đặt lại mật khẩu tài khoản tại Đắp Việt";
        String url = resetPasswordUrl + token;
        String content = """
                <div>
                    Chào %s,
                    <br>
                    Nếu bạn muốn cài lại mật khẩu, vui lòng <a href="%s" target="_blank">nhấn vào đây</a>
                    <br>
                    <br>
                    <div style="border-top:1px solid #eaeaea; padding-top:10px;">
                        Trân trọng,
                        <br>
                        Đắp Việt
                        <br>
                    </div>
                </div>
                """.formatted(name, url);
        send(email, subject, content);
    }

    @Async
    public void verifyEmail(String email, String name, String token) {
        String subject = "Xác thực email tài khoản tại Đắp Việt";
        String url = verifyEmailUrl + token;
        String content = """
                <div>
                    Chào %s,
                    <br>
                    Vui lòng xác thực email tài khoản của bạn qua <a href="%s" target="_blank">đường dẫn này</a>
                    <br>
                    <br>
                    <div style="border-top:1px solid #eaeaea; padding-top:10px;">
                        Trân trọng,
                        <br>
                        Đắp Việt
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
