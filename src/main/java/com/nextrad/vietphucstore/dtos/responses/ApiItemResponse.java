package com.nextrad.vietphucstore.dtos.responses;

public record ApiItemResponse<T>(
        T data,
        String message
) {
}
