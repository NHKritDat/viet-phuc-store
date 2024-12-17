package com.nextrad.vietphucstore.services.imps;

import com.nextrad.vietphucstore.repositories.TokenRepository;
import com.nextrad.vietphucstore.repositories.UserRepository;
import com.nextrad.vietphucstore.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @Override
    public boolean existsByIdAndStatus(UUID id, boolean status) {
        return tokenRepository.existsByIdAndAvailable(id, status);
    }
}
