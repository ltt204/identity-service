package org.ltt204.identityservice.domain.services;

import com.nimbusds.jose.JOSEException;
import org.ltt204.identityservice.domain.entities.User;

import java.text.ParseException;

public interface ITokenService {
    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    boolean validateAccessToken(String token) throws JOSEException, ParseException;

    boolean validateRefreshToken(String token) throws JOSEException, ParseException;

    boolean isInBlacklist(String token);

    void invalidateToken(String token);
}
