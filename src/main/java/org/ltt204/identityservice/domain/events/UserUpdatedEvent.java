package org.ltt204.identityservice.domain.events;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdatedEvent extends BaseEvent {
    final String EVENT_TYPE = "UserUpdatedEvent";

    String userId;
    String username;
    String firstname;
    String lastname;
    LocalDate dateOfBirth;

    @Builder
    public UserUpdatedEvent(
            String id,
            String eventType,
            LocalDateTime timeStamp,
            String userId,
            String username,
            String firstname,
            String lastname,
            LocalDate dateOfBirth) {
        super(id, eventType, timeStamp);
        this.userId = userId;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
    }
}
