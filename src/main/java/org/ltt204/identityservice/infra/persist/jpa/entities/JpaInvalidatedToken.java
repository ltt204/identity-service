package org.ltt204.identityservice.infra.persist.jpa.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisHash;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RedisHash
public class JpaInvalidatedToken {
    String token;
    Date expirationTime;
}
