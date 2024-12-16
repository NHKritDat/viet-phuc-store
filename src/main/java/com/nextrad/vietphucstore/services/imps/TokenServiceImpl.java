package com.nextrad.vietphucstore.services.imps;

import com.nextrad.vietphucstore.enums.TokenStatus;
import com.nextrad.vietphucstore.repositories.TokenRepository;
import com.nextrad.vietphucstore.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;
    @Override
    public boolean existsByIdAndStatus(UUID id, TokenStatus status) {
        return tokenRepository.existsByIdAndStatus(id, status);
    }
}
