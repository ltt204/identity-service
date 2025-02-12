package org.ltt204.identityservice.dto.response.auth;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RevokeTokenResponseDto {
    String accessToken;
    String refreshToken;
}
