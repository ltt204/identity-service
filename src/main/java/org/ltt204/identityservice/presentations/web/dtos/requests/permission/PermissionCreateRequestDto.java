package org.ltt204.identityservice.presentations.web.dtos.requests.permission;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionCreateRequestDto {
    String name;
    String description;
}
