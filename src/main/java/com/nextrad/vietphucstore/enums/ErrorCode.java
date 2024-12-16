package com.nextrad.vietphucstore.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_TOKEN("This token is invalid", HttpStatus.BAD_REQUEST),
    UNAVAILABLE_TOKEN("This token is unavailable", HttpStatus.BAD_REQUEST),
    SIGNATURE_VERIFICATION_FAILED("MAC signature verification failed", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_EMAIL("This email is invalid", HttpStatus.BAD_REQUEST),
    ;
    private final String message;
    private final HttpStatus status;
}
