package org.ltt204.identityservice.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.ltt204.identityservice.dto.request.UserSignInRequestDto;
import org.ltt204.identityservice.dto.response.auth.AuthenticationResponseDto;
import org.ltt204.identityservice.exception.ErrorCode;
import org.ltt204.identityservice.exception.AppException;
import org.ltt204.identityservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

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

    public AuthenticationResponseDto signIn(UserSignInRequestDto requestDto) {
        var user = userRepository.findFirstByUsername(requestDto.getUsername()).orElseThrow(() ->
        {
            var error = ErrorCode.NOT_FOUND;
            error.setMessage("User not found");
            return new AppException(error);
        });

        var token = generateToken(user.getUsername());

        if (passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            return AuthenticationResponseDto.builder()
                    .authenticated(true)
                    .token(token)
                    .build();
        }

        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    private String generateToken(String userName) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userName)
                .issuer("ltt204.org")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.HOURS).toEpochMilli()
                ))
                .build();

        Payload payload = claimsSet.toPayload();

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(ACCESS_TOKEN_SIGNER_KEY));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }
}
