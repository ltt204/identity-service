package org.ltt204.identityservice.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequestDto {
    @Size(min = 8, message = "INVALID_PASSWORD")
    String password;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
}
