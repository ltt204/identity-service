package org.ltt204.identityservice.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.ltt204.identityservice.dto.request.auth.LogoutRequestDto;
import org.ltt204.identityservice.dto.request.auth.RevokeTokenRequestDto;
import org.ltt204.identityservice.dto.request.auth.TokenIntrospectRequestDto;
import org.ltt204.identityservice.dto.request.user.UserSignInRequestDto;
import org.ltt204.identityservice.dto.response.auth.AuthenticationResponseDto;
import org.ltt204.identityservice.dto.response.auth.IntrospectResponseDto;
import org.ltt204.identityservice.dto.response.auth.RevokeTokenResponseDto;
import org.ltt204.identityservice.entity.InvalidatedToken;
import org.ltt204.identityservice.exception.AppException;
import org.ltt204.identityservice.exception.ErrorCode;
import org.ltt204.identityservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthService {
    //    InvalidatedTokenRepository invalidatedTokenRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    //    UnifiedJedis unifiedJedis;
    RedisTemplate<String, String> redisTemplate;
    TokenService tokenService;

    public IntrospectResponseDto introspect(TokenIntrospectRequestDto introspectRequestDto) {
        var token = introspectRequestDto.getToken();

        try {
            return IntrospectResponseDto.builder().valid(tokenService.isValid(token)).build();
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

        var accessToken = tokenService.generateAccessToken(user);
        var refreshToken = tokenService.generateRefreshToken(user);

        if (passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            return AuthenticationResponseDto.builder()
                    .authenticated(true)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }

        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    public RevokeTokenResponseDto revokeToken(RevokeTokenRequestDto revokeTokenRequestDto) throws ParseException, JOSEException {
        var accessToken = revokeTokenRequestDto.getAccessToken();
        var refreshToken = revokeTokenRequestDto.getRefreshToken();
        var isValid = tokenService.isValid(refreshToken);

        if (!isValid) {
            var error = ErrorCode.UNAUTHORIZED;
            error.setMessage("Token is invalid");
            throw new AppException(error);
        }

        var username = SignedJWT.parse(accessToken).getJWTClaimsSet().getSubject();
        log.info(String.valueOf(username));
        var user = userRepository.findFirstByUsername(String.valueOf(username)).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        // Invalidate refresh and access token
        logout(LogoutRequestDto.builder().refreshToken(refreshToken).accessToken(accessToken).build());

        return RevokeTokenResponseDto.builder()
                .accessToken(tokenService.generateAccessToken(user))
                .refreshToken(tokenService.generateRefreshToken(user))
                .build();
    }

    public void logout(LogoutRequestDto logoutRequestDto) {
        var accessToken = logoutRequestDto.getAccessToken();
        var refreshToken = logoutRequestDto.getRefreshToken();

        tokenService.invalidateToken(accessToken);
        tokenService.invalidateToken(refreshToken);
    }
}
