package com.nextrad.vietphucstore.exceptions;

import com.nextrad.vietphucstore.dtos.responses.standard.ApiItemResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    public <T> ResponseEntity<ApiItemResponse<T>> handleAppException(AppException e) {
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(new ApiItemResponse<>(null, e.getErrorCode().getMessage()));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiItemResponse<Boolean>> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        return ResponseEntity.status(403)
                .body(new ApiItemResponse<>(e.getAuthorizationResult().isGranted(), e.getMessage()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiItemResponse<String>> handleNullPointerException(NullPointerException e) {
        return ResponseEntity.status(500)
                .body(new ApiItemResponse<>(Arrays.toString(e.getStackTrace()), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiItemResponse<String>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.status(400)
                .body(new ApiItemResponse<>(e.getName(), e.getMessage()));
    }
}
