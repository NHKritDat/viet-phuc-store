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
    INVALID_TOKEN("This token is invalid", HttpStatus.BAD_REQUEST),
    UNAVAILABLE_TOKEN("This token is unavailable", HttpStatus.BAD_REQUEST),
    SIGNATURE_VERIFICATION_FAILED("MAC signature verification failed", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_EMAIL("This email is invalid", HttpStatus.BAD_REQUEST),
    ;
    String message;
    HttpStatus status;
}
