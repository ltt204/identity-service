package org.ltt204.identityservice.domain.events;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseEvent implements Serializable {
    String id;
    String eventType;
    LocalDateTime timeStamp;
}
