package com.nextrad.vietphucstore.utils;

import com.google.api.client.json.webtoken.JsonWebToken;
import com.google.auth.oauth2.TokenVerifier;
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

    private static final String ID_FIELD = "id";
    private static final String FULL_NAME_FIELD = "fullName";
    private static final String ADDRESS_FIELD = "address";
    private static final String PHONE_FIELD = "phone";
    private static final String AVATAR_FIELD = "avatar";
    private static final String ROLE_FIELD = "role";
    private static final String STATUS_FIELD = "status";
    private static final String CREATED_BY_FIELD = "createdBy";
    private static final String CREATED_DATE_FIELD = "createdDate";
    private static final String UPDATED_BY_FIELD = "updatedBy";
    private static final String UPDATED_DATE_FIELD = "updatedDate";

    public String[] getJwtInfo(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            return new String[]{
                    claimsSet.getJWTID(),
                    claimsSet.getStringClaim(ID_FIELD),
                    claimsSet.getStringClaim(FULL_NAME_FIELD),
                    claimsSet.getSubject(),
                    claimsSet.getStringClaim(ADDRESS_FIELD),
                    claimsSet.getStringClaim(PHONE_FIELD),
                    claimsSet.getStringClaim(AVATAR_FIELD),
                    claimsSet.getStringClaim(ROLE_FIELD),
                    claimsSet.getStringClaim(STATUS_FIELD),
                    claimsSet.getStringClaim(CREATED_BY_FIELD),
                    claimsSet.getClaim(CREATED_DATE_FIELD).toString(),
                    claimsSet.getStringClaim(UPDATED_BY_FIELD),
                    claimsSet.getClaim(UPDATED_DATE_FIELD).toString()
            };
        } catch (ParseException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    public String[] getInfo(String token) {
        JsonWebToken.Payload payload = getPayload(token);
        return new String[]{
                payload.getSubject(),
                payload.get(FULL_NAME_FIELD).toString(),
                payload.get(AVATAR_FIELD).toString()
        };
    }

    public Token createEntity(String token, boolean available) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
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
                .claim(ID_FIELD, user.getId().toString())
                .claim(FULL_NAME_FIELD, user.getFullName())
                .claim(ADDRESS_FIELD, user.getAddress())
                .claim(PHONE_FIELD, user.getPhone())
                .claim(AVATAR_FIELD, user.getAvatar() != null ? user.getAvatar() : "")
                .claim(ROLE_FIELD, user.getRole().name())
                .claim(STATUS_FIELD, user.getStatus())
                .claim(CREATED_BY_FIELD, user.getCreatedBy())
                .claim(CREATED_DATE_FIELD, user.getCreatedDate())
                .claim(UPDATED_BY_FIELD, user.getUpdatedBy())
                .claim(UPDATED_DATE_FIELD, user.getUpdatedDate())
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
