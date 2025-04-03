package org.ltt204.identityservice.domain.services.interfaces;

import org.ltt204.identityservice.domain.events.UserCreatedEvent;
import org.ltt204.identityservice.domain.events.UserDeletedEvent;
import org.ltt204.identityservice.domain.events.UserUpdatedEvent;
import org.springframework.stereotype.Component;

public interface IEventPublisher {
    void publishEvent(UserCreatedEvent event);

    void publishEvent(UserUpdatedEvent event);

    void publishEvent(UserDeletedEvent event);
}
