package com.nextrad.vietphucstore.utils;

import com.google.api.client.json.webtoken.JsonWebToken;
import com.google.auth.oauth2.TokenVerifier;
import com.nextrad.vietphucstore.entities.user.User;
import com.nextrad.vietphucstore.enums.error.ErrorCode;
import com.nextrad.vietphucstore.exceptions.AppException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

@Component
public class TokenUtil {
    @Value("${ISSUER}")
    private String issuer;
    @Value("${AUDIENCE}")
    private String audience;
    @Value("${JWT_SECRET_KEY}")
    private String secretKey;
    @Value("${ACCESS_TOKEN_EXP}")
    private long accessTokenExp;
    @Value("${REFRESH_TOKEN_EXP}")
    private long refreshTokenExp;

    private JsonWebToken.Payload getPayload(String token) {
        TokenVerifier verifier = TokenVerifier.newBuilder()
                .setAudience(audience)
                .setIssuer(issuer)
                .build();
        try {
            return verifier.verify(token).getPayload();
        } catch (TokenVerifier.VerificationException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    public String genAccessToken(User user) {
        return genToken(user, accessTokenExp);
    }

    public String genRefreshToken(User user) {
        return genToken(user, refreshTokenExp);
    }

    private String genToken(User user, long expTime) {
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)
                .type(JOSEObjectType.JWT)
                .build();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer("nextrad.com")
                .issueTime(new Date(System.currentTimeMillis()))
                .expirationTime(new Date(System.currentTimeMillis() + expTime))
                .jwtID(UUID.randomUUID().toString())
                .subject(user.getEmail())
                .claim("id", user.getId().toString())
                .claim("fullName", user.getFullName())
                .claim("address", user.getAddress())
                .claim("phone", user.getPhone())
                .claim("avatar", user.getAvatar() != null ? user.getAvatar() : "")
                .claim("role", user.getRole().name())
                .claim("status", user.getStatus())
                .claim("createdBy", user.getCreatedBy())
                .claim("createdDate", user.getCreatedDate())
                .claim("updatedBy", user.getUpdatedBy())
                .claim("updatedDate", user.getUpdatedDate())
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(secretKey.getBytes()));
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.SIGNATURE_VERIFICATION_FAILED);
        }
        return jwsObject.serialize();
    }

    public String getJwtId(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getJWTID();
        } catch (ParseException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }
}
