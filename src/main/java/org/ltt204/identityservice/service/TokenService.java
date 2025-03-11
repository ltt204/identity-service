package org.ltt204.identityservice.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.ltt204.identityservice.entity.InvalidatedToken;
import org.ltt204.identityservice.entity.User;
import org.ltt204.identityservice.exception.AppException;
import org.ltt204.identityservice.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.DateFormat;
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
public class TokenService {
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


    public boolean isValid(String token) throws JOSEException, ParseException {
        JWSVerifier jwsAccessVerifier = new MACVerifier(ACCESS_TOKEN_SIGNER_KEY.getBytes());
        JWSVerifier jwsRefreshVerifier = new MACVerifier(REFRESH_TOKEN_SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        var valid = signedJWT.verify(jwsAccessVerifier) || signedJWT.verify(jwsRefreshVerifier);
        var isExpired = signedJWT.getJWTClaimsSet().getExpirationTime().after(new Date());
        return valid && isExpired && !isInBlacklist(token);
    }

    public boolean isInBlacklist(String token) {
        var key = prefixRevokedToken + token;
        return redisTemplate.hasKey(key);
    }

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

    public void invalidateToken(String token) {
        InvalidatedToken invalidatedToken;

        try {
            invalidatedToken = InvalidatedToken
                    .builder()
                    .token(token)
                    .expirationTime(SignedJWT.parse(token).getJWTClaimsSet().getExpirationTime())
                    .build();
        } catch (ParseException e) {
            throw new AppException(
                    ErrorCode.INVALID_TOKEN_FORMAT
            );
        }

        if (invalidatedToken.getExpirationTime().before(new Date())) {
            return; // Token is already expired
        }

        var key = prefixRevokedToken + invalidatedToken.getToken();
        log.info("Key: {}", key);
        redisTemplate.opsForValue().set(key, String.valueOf(new Date()));
        redisTemplate.expire(key, invalidatedToken.getExpirationTime().getTime() - System.currentTimeMillis(), TimeUnit.MICROSECONDS);
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
