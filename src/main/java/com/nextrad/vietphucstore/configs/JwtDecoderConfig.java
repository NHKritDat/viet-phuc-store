package com.nextrad.vietphucstore.configs;

import com.nextrad.vietphucstore.enums.ErrorCode;
import com.nextrad.vietphucstore.enums.TokenStatus;
import com.nextrad.vietphucstore.exceptions.AppException;
import com.nextrad.vietphucstore.services.TokenService;
import com.nextrad.vietphucstore.utils.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.spec.SecretKeySpec;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class JwtDecoderConfig implements JwtDecoder {

    private final TokenService tokenService;
    private final TokenUtil tokenUtil;
    @Value("${JWT_SECRET_KEY}")
    private String jwtSecretKey;
    private NimbusJwtDecoder jwtDecoder;

    @Bean
    public JwtDecoder jwtDecoder() {
        jwtDecoder = NimbusJwtDecoder
                .withSecretKey(new SecretKeySpec(jwtSecretKey.getBytes(), "HS256"))
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
        return jwtDecoder;
    }

    @Override
    public Jwt decode(String token) {
        String id = tokenUtil.getJwtId(token);
        if (tokenService.existsByIdAndStatus(UUID.fromString(id), TokenStatus.INACTIVE)) {
            throw new AppException(ErrorCode.UNAVAILABLE_TOKEN);
        }
        return jwtDecoder.decode(token);
    }
}
