package com.nextrad.vietphucstore.exceptions;

import com.nextrad.vietphucstore.enums.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AppException extends RuntimeException{
    ErrorCode errorCode;
}
