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
    INVALID_TOKEN("This auth is invalid", HttpStatus.BAD_REQUEST),
    TOKEN_NOT_FOUND("Token not found", HttpStatus.NOT_FOUND),
    UNAVAILABLE_TOKEN("This auth is unavailable", HttpStatus.BAD_REQUEST),
    SIGNATURE_VERIFICATION_FAILED("MAC signature verification failed", HttpStatus.INTERNAL_SERVER_ERROR),
    //Email error
    INVALID_EMAIL("This email is invalid", HttpStatus.BAD_REQUEST),
    //User error
    USER_NOT_FOUND("User not found", HttpStatus.NOT_FOUND),
    WRONG_PASSWORD("Wrong password", HttpStatus.UNAUTHORIZED),
    PASSWORD_NOT_MATCH("Password not match", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED("This email is existed", HttpStatus.BAD_REQUEST),
    //Product error
    PRODUCT_NOT_FOUND("Product not found", HttpStatus.NOT_FOUND),
    PRODUCT_TYPE_NOT_FOUND("Product type not found", HttpStatus.NOT_FOUND),
    PRODUCT_COLLECTION_NOT_FOUND("Product collection not found", HttpStatus.NOT_FOUND),
    PRODUCT_SIZE_NOT_FOUND("Product size not found", HttpStatus.NOT_FOUND),
    ;
    String message;
    HttpStatus status;
}
