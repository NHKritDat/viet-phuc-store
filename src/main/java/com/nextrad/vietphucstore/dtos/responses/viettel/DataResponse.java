package com.nextrad.vietphucstore.dtos.responses.viettel;

public record DataResponse<T>(
        int status,
        boolean error,
        String message,
        T data
) {
}
