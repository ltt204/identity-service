package org.ltt204.identityservice.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.ltt204.identityservice.validator.DobConstraint;

import java.time.LocalDate;
import java.util.Set;

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
    @NotNull
    @DobConstraint(min = 18, message = "INVALID_DATE_OF_BIRTH")
    LocalDate dateOfBirth;
}
