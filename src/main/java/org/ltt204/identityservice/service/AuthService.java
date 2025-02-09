package org.ltt204.identityservice.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.ltt204.identityservice.dto.request.token.TokenIntrospectRequestDto;
import org.ltt204.identityservice.dto.request.user.UserSignInRequestDto;
import org.ltt204.identityservice.dto.response.auth.AuthenticationResponseDto;
import org.ltt204.identityservice.dto.response.auth.IntrospectResponseDto;
import org.ltt204.identityservice.entity.User;
import org.ltt204.identityservice.exception.AppException;
import org.ltt204.identityservice.exception.ErrorCode;
import org.ltt204.identityservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthService {

    @NonFinal
    @Value("${jwt.signerKey}")
    String ACCESS_TOKEN_SIGNER_KEY;
    @NonFinal
    @Value("${jwt.valid-duration}")
    long VALID_DURATION;


    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public IntrospectResponseDto introspect(TokenIntrospectRequestDto introspectRequestDto) {
        var token = introspectRequestDto.getToken();

        try {
            JWSVerifier jwsVerifier = new MACVerifier(ACCESS_TOKEN_SIGNER_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);

            var valid = signedJWT.verify(jwsVerifier);
            var isExpired = signedJWT.getJWTClaimsSet().getExpirationTime().after(new Date());

            return IntrospectResponseDto.builder()
                    .valid(
                            valid && isExpired
                    )
                    .build();

        } catch (JOSEException | ParseException e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    public AuthenticationResponseDto authenticate(UserSignInRequestDto requestDto) {
        var user = userRepository.findFirstByUsername(requestDto.getUsername()).orElseThrow(() ->
        {
            var error = ErrorCode.NOT_FOUND;
            error.setMessage("User not found");
            return new AppException(error);
        });

        var token = generateToken(user);

        if (passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            return AuthenticationResponseDto.builder()
                    .authenticated(true)
                    .token(token)
                    .build();
        }

        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("ltt204.org")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope", buildScope(user))
                .build();

        Payload payload = claimsSet.toPayload();

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(ACCESS_TOKEN_SIGNER_KEY));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> stringJoiner.add(role.getName()));
        }

        return stringJoiner.toString();
    }
}
