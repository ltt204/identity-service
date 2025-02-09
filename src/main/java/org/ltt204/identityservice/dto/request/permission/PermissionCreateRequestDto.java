package org.ltt204.identityservice.dto.request.permission;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionCreateRequestDto {
    String name;
    String description;
}
