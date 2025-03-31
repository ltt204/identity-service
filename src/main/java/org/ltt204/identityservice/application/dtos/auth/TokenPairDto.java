package org.ltt204.identityservice.application.dtos.auth;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenPairDto {
    String accessToken;
    String refreshToken;
}
