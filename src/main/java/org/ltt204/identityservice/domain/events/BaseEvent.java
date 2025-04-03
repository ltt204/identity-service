package org.ltt204.identityservice.domain.events;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseEvent implements Serializable {
    String id;
    String eventType;
    LocalDateTime timeStamp;
}
