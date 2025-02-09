package org.ltt204.identityservice.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequestDto {
    @Size(min = 3, message = "INVALID_USERNAME")
    String username;
    @Size(min = 8, message = "INVALID_PASSWORD")
    String password;
    @NotBlank(message = "FIRSTNAME_REQUIRED")
    String firstName;
    @NotBlank(message = "LASTNAME_REQUIRED")
    String lastName;
    LocalDate dateOfBirth;
}
