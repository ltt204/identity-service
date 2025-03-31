package org.ltt204.identityservice.application.dtos.auth;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthTokenDto {
    boolean authenticated;
    String accessToken;
    String refreshToken;
}
