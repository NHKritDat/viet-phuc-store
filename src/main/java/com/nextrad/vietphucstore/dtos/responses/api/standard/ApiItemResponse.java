package com.nextrad.vietphucstore.dtos.responses.api.standard;

public record ApiItemResponse<T>(
        T data,
        String message
) {
}
