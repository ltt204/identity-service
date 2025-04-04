package org.ltt204.identityservice.presentations.web.dtos.requests.role;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleUpdateRequestDto {
    String name;
    String description;
    Set<String> permissions;
}
