package org.ltt204.identityservice.application.dtos.role;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDto {
    String name;
    String description;
    Set<String> permissions;
}
