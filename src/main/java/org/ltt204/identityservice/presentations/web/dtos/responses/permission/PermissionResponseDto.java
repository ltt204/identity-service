package org.ltt204.identityservice.presentations.web.dtos.responses.permission;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionResponseDto {
    int id;
    String name;
    String description;
}
