package org.ltt204.identityservice.dto.response.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.ltt204.identityservice.entity.Role;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    String id;
    String username;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    Set<String> roles;
}
