package org.ltt204.identityservice.application.dtos.user;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.ltt204.identityservice.infra.persist.jpa.entities.JpaRole;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    String id;
    String username;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    Set<JpaRole> roles;
}
