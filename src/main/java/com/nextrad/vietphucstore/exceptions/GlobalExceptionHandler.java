package com.nextrad.vietphucstore.exceptions;

import com.nextrad.vietphucstore.dtos.responses.ApiItemResponse;
import com.nimbusds.jose.JOSEException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    public <T> ResponseEntity<ApiItemResponse<T>> handleAppException(AppException e) {
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(new ApiItemResponse<>(null, e.getErrorCode().getMessage()));
    }
}
