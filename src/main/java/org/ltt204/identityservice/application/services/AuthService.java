package org.ltt204.identityservice.application.services;

import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.ltt204.identityservice.application.dtos.auth.AuthTokenDto;
import org.ltt204.identityservice.application.dtos.auth.SignInCredentialsDto;
import org.ltt204.identityservice.application.dtos.auth.TokenPairDto;
import org.ltt204.identityservice.domain.repositories.IUserRepository;
import org.ltt204.identityservice.domain.services.interfaces.IAuthService;
import org.ltt204.identityservice.presentations.web.advices.AppException;
import org.ltt204.identityservice.presentations.web.advices.ErrorCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;

import static com.nimbusds.jwt.SignedJWT.parse;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    PasswordEncoder passwordEncoder;
    TokenService tokenService;
    IUserRepository userRepository;

    @Override
    public boolean isValidToken(String token) {
        try {
            return !tokenService.isInBlacklist(token)
                    && (tokenService.validateAccessToken(token)
                    || tokenService.validateRefreshToken(token));
        } catch (JOSEException | ParseException e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    @Override
    public AuthTokenDto authenticate(
            SignInCredentialsDto signInCredentialsDto
    ) {
        var user = userRepository.findFirstByUserName(signInCredentialsDto.getUsername());

        var accessToken = tokenService.generateAccessToken(user);
        var refreshToken = tokenService.generateRefreshToken(user);

        if (passwordEncoder.matches(signInCredentialsDto.getPassword(), user.getPassword())) {
            return AuthTokenDto.builder()
                    .authenticated(true)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }

        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    @Override
    public TokenPairDto refreshToken(TokenPairDto tokenPairDto) throws ParseException, JOSEException {
        var accessToken = tokenPairDto.getAccessToken();
        var refreshToken = tokenPairDto.getRefreshToken();
        var isValid = tokenService.validateRefreshToken(refreshToken);

        if (!isValid) {
            var error = ErrorCode.UNAUTHORIZED;
            error.setMessage("Token is invalid");
            throw new AppException(error);
        }

        var username = parse(accessToken).getJWTClaimsSet().getSubject();
        var user = userRepository.findFirstByUserName(String.valueOf(username));

        // Invalidate refresh and access token
        revokeToken(tokenPairDto);

        return TokenPairDto.builder()
                .accessToken(tokenService.generateAccessToken(user))
                .refreshToken(tokenService.generateRefreshToken(user))
                .build();
    }

    @Override
    public void revokeToken(TokenPairDto tokenPairDto) {
        tokenService.invalidateToken(tokenPairDto.getAccessToken());
        tokenService.invalidateToken(tokenPairDto.getRefreshToken());
    }
}
