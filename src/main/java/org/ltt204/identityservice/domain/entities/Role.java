package org.ltt204.identityservice.domain.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
    int id;
    String name;
    String description;
    Set<Permission> permissions;
}
