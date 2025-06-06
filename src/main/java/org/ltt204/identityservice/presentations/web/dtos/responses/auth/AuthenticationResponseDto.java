package org.ltt204.identityservice.presentations.web.dtos.responses.auth;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationResponseDto {
    boolean authenticated;
    String accessToken;
    String refreshToken;
}
