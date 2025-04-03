package org.ltt204.identityservice.domain.events;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDeletedEvent extends  BaseEvent {
    final String EVENT_TYPE = "UserDeletedEvent";

    String userId;

    @Builder
    public UserDeletedEvent(String id, String eventType, LocalDateTime timeStamp, String userId, String email, String firstname, String lastname) {
        super(id, eventType, timeStamp);
        this.userId = userId;
    }
}
