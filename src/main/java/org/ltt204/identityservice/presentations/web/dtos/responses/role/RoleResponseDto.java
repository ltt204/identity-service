package org.ltt204.identityservice.presentations.web.dtos.responses.role;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.ltt204.identityservice.presentations.web.dtos.responses.permission.PermissionResponseDto;

import java.util.Set;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponseDto {
    int id;
    String name;
    String description;
    Set<PermissionResponseDto> permissions;
}
