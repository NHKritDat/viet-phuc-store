package com.nextrad.vietphucstore.services;

import com.nextrad.vietphucstore.enums.TokenStatus;

import java.util.UUID;

public interface TokenService {
    boolean existsByIdAndStatus(UUID id, TokenStatus status);
}
