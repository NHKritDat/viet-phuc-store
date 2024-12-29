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
    INVALID_TOKEN("This token is invalid", HttpStatus.BAD_REQUEST),
    TOKEN_NOT_FOUND("Token not found", HttpStatus.NOT_FOUND),
    UNAVAILABLE_TOKEN("This token is unavailable", HttpStatus.BAD_REQUEST),
    SIGNATURE_VERIFICATION_FAILED("MAC signature verification failed", HttpStatus.INTERNAL_SERVER_ERROR),
    //Email error
    INVALID_EMAIL("This email is invalid", HttpStatus.BAD_REQUEST),
    //User error
    USER_NOT_FOUND("User not found", HttpStatus.NOT_FOUND),
    WRONG_PASSWORD("Wrong password", HttpStatus.FORBIDDEN),
    PASSWORD_NOT_MATCH("Password not match", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED("This email is existed", HttpStatus.BAD_REQUEST),
    //Product error
    PRODUCT_NOT_FOUND("Product not found", HttpStatus.NOT_FOUND),
    PRODUCT_TYPE_NOT_FOUND("Product type not found", HttpStatus.NOT_FOUND),
    PRODUCT_COLLECTION_NOT_FOUND("Product collection not found", HttpStatus.NOT_FOUND),
    PRODUCT_SIZE_NOT_FOUND("Product size not found", HttpStatus.NOT_FOUND),
    PRODUCT_QUANTITY_NOT_FOUND("Product quantity not found", HttpStatus.NOT_FOUND),
    //Order error
    CART_NOT_FOUND("Cart not found", HttpStatus.NOT_FOUND),
    CART_EMPTY("Cart is empty", HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND("Order not found", HttpStatus.NOT_FOUND),
    ORDER_DETAIL_NOT_FOUND("Order detail not found", HttpStatus.NOT_FOUND),
    FEEDBACK_NOT_FOUND("Feedback not found", HttpStatus.NOT_FOUND),
    //Permission error
    NO_PERMISSION("You are not allowed to do this!", HttpStatus.FORBIDDEN),
    ;
    String message;
    HttpStatus status;
}
