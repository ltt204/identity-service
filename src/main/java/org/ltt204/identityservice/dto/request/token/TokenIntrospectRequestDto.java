package org.ltt204.identityservice.dto.request.token;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenIntrospectRequestDto {
    String token;
}
