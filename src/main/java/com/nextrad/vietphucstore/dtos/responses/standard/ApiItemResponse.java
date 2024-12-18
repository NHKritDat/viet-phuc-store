package com.nextrad.vietphucstore.dtos.responses.standard;

public record ApiItemResponse<T>(
        T data,
        String message
) {
}
