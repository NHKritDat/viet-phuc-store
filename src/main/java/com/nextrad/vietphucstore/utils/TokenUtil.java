package com.nextrad.vietphucstore.utils;

import com.google.api.client.json.webtoken.JsonWebToken;
import com.google.auth.oauth2.TokenVerifier;
import com.nextrad.vietphucstore.dtos.responses.inner.user.CheckTokenResult;
import com.nextrad.vietphucstore.entities.user.Token;
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
    private static final String NAME_FIELD = "name";
    private static final String AVATAR_FIELD = "avatar";
    private static final String ROLE_FIELD = "scope";

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

    public CheckTokenResult checkToken(String token, boolean available) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return new CheckTokenResult(true, setEntity(signedJWT, available));
        } catch (ParseException e) {
            return new CheckTokenResult(false, null);
        }
    }

    public String getEmailFromJwt(String token) {
        try {
            return SignedJWT.parse(token).getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    public String[] getJwtInfo(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            return new String[]{
                    claimsSet.getJWTID(),
                    claimsSet.getStringClaim(NAME_FIELD),
                    claimsSet.getSubject(),
                    claimsSet.getStringClaim(AVATAR_FIELD),
                    claimsSet.getStringClaim(ROLE_FIELD)
            };
        } catch (ParseException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    public String[] getInfo(String token) {
        JsonWebToken.Payload payload = getPayload(token);
        return new String[]{
                payload.getSubject(),
                payload.get(NAME_FIELD).toString(),
                payload.get(AVATAR_FIELD).toString()
        };
    }

    public Token createEntity(String token, boolean available) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return setEntity(signedJWT, available);
        } catch (ParseException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    private Token setEntity(SignedJWT signedJWT, boolean available) {
        try {
            Token entity = new Token();
            entity.setId(UUID.fromString(signedJWT.getJWTClaimsSet().getJWTID()));
            entity.setAvailable(available);
            entity.setCreatedBy(signedJWT.getJWTClaimsSet().getSubject());
            entity.setUpdatedBy(signedJWT.getJWTClaimsSet().getSubject());
            entity.setExpAt(signedJWT.getJWTClaimsSet().getExpirationTime());
            return entity;
        } catch (ParseException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

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
                .claim(NAME_FIELD, user.getName())
                .claim(AVATAR_FIELD, user.getAvatar())
                .claim(ROLE_FIELD, user.getRole().name())
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
            return SignedJWT.parse(token).getJWTClaimsSet().getJWTID();
        } catch (ParseException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }
}
