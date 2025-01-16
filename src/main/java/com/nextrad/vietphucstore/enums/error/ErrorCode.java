package com.nextrad.vietphucstore.enums.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum ErrorCode {
    //Token error
    INVALID_TOKEN("Token không hợp lệ", HttpStatus.BAD_REQUEST),
    TOKEN_NOT_FOUND("Token không được tìm thấy", HttpStatus.NOT_FOUND),
    UNAVAILABLE_TOKEN("Token không khả dụng", HttpStatus.FORBIDDEN),
    SIGNATURE_VERIFICATION_FAILED("MAC signature verification failed", HttpStatus.INTERNAL_SERVER_ERROR),
    //Email error
    INVALID_EMAIL("Email không hợp lệ", HttpStatus.BAD_REQUEST),
    //User error
    USER_NOT_FOUND("Người dùng không tìm thấy", HttpStatus.NOT_FOUND),
    WRONG_PASSWORD("Sai mật khẩu", HttpStatus.FORBIDDEN),
    PASSWORD_NOT_MATCH("Mật khẩu không giống", HttpStatus.FORBIDDEN),
    PASSWORD_NOT_STRONG("Mật khẩu phải có in hoa có kí tự và số", HttpStatus.FORBIDDEN),
    EMAIL_EXISTED("Email đã tồn tại", HttpStatus.BAD_REQUEST),
    //Product error
    PRODUCT_NOT_FOUND("Sản phẩm không được tìm thấy", HttpStatus.NOT_FOUND),
    PRODUCT_TYPE_NOT_FOUND("Loại sản phẩm không tìm thấy", HttpStatus.NOT_FOUND),
    PRODUCT_COLLECTION_NOT_FOUND("Bộ sưu tập không được tìm thấy", HttpStatus.NOT_FOUND),
    PRODUCT_SIZE_NOT_FOUND("Kích cỡ sản phẩm không được tìm thấy", HttpStatus.NOT_FOUND),
    PRODUCT_QUANTITY_NOT_FOUND("Sản phẩm không thể tìm thấy số lượng", HttpStatus.NOT_FOUND),
    //Order error
    CART_NOT_FOUND("Giỏ hàng không được tìm thấy", HttpStatus.NOT_FOUND),
    CART_EMPTY("Giả hàng rỗng", HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND("Đơn hàng không được tìm thấy", HttpStatus.NOT_FOUND),
    ORDER_DETAIL_NOT_FOUND("Chi tiết đơn hàng không được tìm thấy", HttpStatus.NOT_FOUND),
    FEEDBACK_NOT_FOUND("Đánh giá không được tìm thấy", HttpStatus.NOT_FOUND),
    //Permission error
    NO_PERMISSION("Bạn không có quyền thực hiện yêu cầu này", HttpStatus.FORBIDDEN),
    PRODUCT_QUANTITY_NOT_ENOUGH("Số lượng sản phẩm bạn đặt hiện không đủ", HttpStatus.BAD_REQUEST),
    MISSING_SELECT_PRODUCT("Vui lòng chọn ít nhất một sản phẩm", HttpStatus.BAD_REQUEST),
    ALREADY_CANCELED("Đơn hàng đã được hủy", HttpStatus.BAD_REQUEST),
    CAN_NOT_CANCEL("Đơn hàng đã rời kho", HttpStatus.BAD_REQUEST),
    ;
    String message;
    HttpStatus status;
}
