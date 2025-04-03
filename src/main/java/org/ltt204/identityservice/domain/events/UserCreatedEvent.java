package org.ltt204.identityservice.domain.events;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserCreatedEvent extends BaseEvent {
    public static final String EVENT_TYPE = "UserCreatedEvent";

    String userId;
    String username;
    String firstname;
    String lastname;
    LocalDate dateOfBirth;

    @Builder
    public UserCreatedEvent(
            String id,
            String eventType,
            LocalDateTime timeStamp,
            String userId,
            String username,
            String firstname,
            String lastname,
            LocalDate dateOfBirth
    ) {
        super(id, eventType, timeStamp);
        this.userId = userId;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
    }
}
