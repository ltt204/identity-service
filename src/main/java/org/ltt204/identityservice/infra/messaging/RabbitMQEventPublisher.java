package org.ltt204.identityservice.infra.messaging;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.ltt204.identityservice.domain.events.UserCreatedEvent;
import org.ltt204.identityservice.domain.events.UserDeletedEvent;
import org.ltt204.identityservice.domain.events.UserUpdatedEvent;
import org.ltt204.identityservice.domain.services.interfaces.IEventPublisher;
import org.ltt204.identityservice.presentations.web.advices.AppException;
import org.ltt204.identityservice.presentations.web.advices.ErrorCode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RabbitMQEventPublisher implements IEventPublisher {
    @NonFinal
    @Value("${service.domain}")
    String DOMAIN;

    @NonFinal
    @Value("${service.name}")
    String SERVICE_NAME;

    @NonFinal
    @Value("${service.event.created}")
    String EVENT_CREATED;

    @NonFinal
    @Value("${service.event.updated}")
    String EVENT_UPDATED;

    @NonFinal
    @Value("${service.event.deleted}")
    String EVENT_DELETED;

    RabbitTemplate rabbitTemplate;

    @Override
    public void publishEvent(UserCreatedEvent event) {
        try {
            rabbitTemplate.convertAndSend(
                    getExchangeName(),
                    getRoutingKey(EVENT_CREATED),
                    event
            );
        } catch (Exception e) {
            throw new AppException(
                    ErrorCode.RABBITMQ_PUBLISHING_ERROR
                            .withErrorCode("Error while publishing event to RabbitMQ")
            );
        }
    }

    @Override
    public void publishEvent(UserUpdatedEvent event) {
        try {
            rabbitTemplate.convertAndSend(
                    getExchangeName(),
                    getRoutingKey(EVENT_UPDATED),
                    event
            );
        } catch (Exception e) {
            throw new AppException(
                    ErrorCode.RABBITMQ_PUBLISHING_ERROR
                            .withErrorCode("Error while publishing event to RabbitMQ")
            );
        }
    }

    @Override
    public void publishEvent(UserDeletedEvent event) {
        try {
            rabbitTemplate.convertAndSend(
                    getExchangeName(),
                    getRoutingKey(EVENT_DELETED),
                    event
            );
        } catch (Exception e) {
            throw new AppException(
                    ErrorCode.RABBITMQ_PUBLISHING_ERROR
                            .withErrorCode("Error while publishing event to RabbitMQ")
            );
        }
    }

    private String getRoutingKey(String eventType) {
        return String.format("%s.%s.%s", SERVICE_NAME, DOMAIN, eventType);
    }

    private String getExchangeName() {
        return String.format("%s.%s.exchange", SERVICE_NAME, DOMAIN);
    }
}
