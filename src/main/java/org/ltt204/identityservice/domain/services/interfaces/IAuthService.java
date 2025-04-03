package org.ltt204.identityservice.domain.services.interfaces;

import com.nimbusds.jose.JOSEException;
import org.ltt204.identityservice.application.dtos.auth.AuthTokenDto;
import org.ltt204.identityservice.application.dtos.auth.SignInCredentialsDto;
import org.ltt204.identityservice.application.dtos.auth.TokenPairDto;

import java.text.ParseException;

public interface IAuthService {
    AuthTokenDto authenticate(SignInCredentialsDto signInCredentialsDto);

    boolean isValidToken(String token);

    void revokeToken(TokenPairDto tokenPairDto);

    TokenPairDto refreshToken(TokenPairDto tokenPairDto) throws ParseException, JOSEException;
}
