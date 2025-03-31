package org.ltt204.identityservice.presentations.web.dtos.requests.auth;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenIntrospectRequestDto {
    String token;
}
