package org.ltt204.identityservice.presentations.web.dtos.requests.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.ltt204.identityservice.infra.persist.jpa.entities.JpaRole;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSignInRequestDto {
    @Size(min = 3, message = "INVALID_USERNAME")
    @NotBlank
    String username;
    @NotBlank
    @Size(min = 8, message = "INVALID_PASSWORD")
    String password;
}
