package org.ltt204.identityservice.application.services;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.ltt204.identityservice.domain.entities.User;
import org.ltt204.identityservice.domain.services.ITokenService;
import org.ltt204.identityservice.infra.persist.jpa.entities.JpaInvalidatedToken;
import org.ltt204.identityservice.presentations.web.advices.AppException;
import org.ltt204.identityservice.presentations.web.advices.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TokenService implements ITokenService {
    @Value("${jwt.revoked-token-prefix}")
    String prefixRevokedToken;
    @Value("${jwt.signer-key}")
    String ACCESS_TOKEN_SIGNER_KEY;
    @Value("${jwt.refreshable-duration}")
    long VALID_DURATION;
    @Value("${jwt.refresh-signer-key}")
    String REFRESH_TOKEN_SIGNER_KEY;
    @Value("${jwt.valid-duration}")
    long REFRESHABLE_DURATION;

    final RedisTemplate<String, String> redisTemplate;

    @Override
    public String generateAccessToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("ltt204.org")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .claim("scope", buildScope(user))
                .build();

        return getPayload(header, claimsSet, ACCESS_TOKEN_SIGNER_KEY);
    }

    @Override
    public String generateRefreshToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("ltt204.org")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .build();

        return getPayload(header, claimsSet, REFRESH_TOKEN_SIGNER_KEY);
    }

    @Override
    public boolean validateAccessToken(String token) throws JOSEException, ParseException {
        JWSVerifier jwsAccessVerifier = new MACVerifier(ACCESS_TOKEN_SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);


        if (!signedJWT.verify(jwsAccessVerifier)) {
            return false;
        }

        if (signedJWT.getJWTClaimsSet().getExpirationTime() == null) {
            return false;
        }

        return !isInBlacklist(token);
    }

    @Override
    public boolean validateRefreshToken(String token) throws JOSEException, ParseException {
        JWSVerifier jwsRefreshVerifier = new MACVerifier(REFRESH_TOKEN_SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        if (!signedJWT.verify(jwsRefreshVerifier)) {
            return false;
        }

        if (signedJWT.getJWTClaimsSet().getExpirationTime() == null) {
            return false;
        }

        return !isInBlacklist(token);
    }

    @Override
    public boolean isInBlacklist(String token) {
        var key = prefixRevokedToken + token;
        return redisTemplate.hasKey(key);
    }

    @Override
    public void invalidateToken(String token) {
        JpaInvalidatedToken jpaInvalidatedToken;

        try {
            jpaInvalidatedToken = JpaInvalidatedToken
                    .builder()
                    .token(token)
                    .expirationTime(SignedJWT.parse(token).getJWTClaimsSet().getExpirationTime())
                    .build();
        } catch (ParseException e) {
            throw new AppException(
                    ErrorCode.INVALID_TOKEN_FORMAT
            );
        }

        if (jpaInvalidatedToken.getExpirationTime().before(new Date())) {
            return; // Token is already expired
        }

        var key = prefixRevokedToken + jpaInvalidatedToken.getToken();

        log.info("Redis key: {}", key);

        redisTemplate.opsForValue().set(key, String.valueOf(new Date()));
        redisTemplate.expire(key, jpaInvalidatedToken.getExpirationTime().getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> stringJoiner.add(role.getName()));
        }

        return stringJoiner.toString();
    }

    private String getPayload(JWSHeader header, JWTClaimsSet claimsSet, String refreshTokenSignerKey) {
        Payload payload = claimsSet.toPayload();

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(refreshTokenSignerKey));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
