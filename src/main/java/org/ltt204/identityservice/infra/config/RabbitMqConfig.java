package org.ltt204.identityservice.infra.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RabbitMqConfig {
    /**
     * RabbitMQ configuration
     * <p>
     * Format:
     * exchange: {service-name}.{domain}.exchange
     * queue: {service-name}.{domain}.{event-type}.queue
     * routing key: {service-name}.{domain}.{event-type}
     * </p>
     */

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

    // Exchange
    private String getExchangeName() {
        return String.format("%s.%s.exchange", SERVICE_NAME, DOMAIN);
    }

    // Queue
    private String getQueueName(String eventType) {
        return String.format("%s.%s.%s.queue", SERVICE_NAME, DOMAIN, eventType);
    }

    // Routing key
    private String getRoutingKey(String eventType) {
        return String.format("%s.%s.%s", SERVICE_NAME, DOMAIN, eventType);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(getExchangeName());
    }

    @Bean
    public Queue userCreatedQueue() {
        return new Queue(getQueueName(EVENT_CREATED), true);
    }

    @Bean
    public Queue userUpdatedQueue() {
        return new Queue(getQueueName(EVENT_UPDATED), true);
    }

    @Bean
    public Queue userDeletedQueue() {
        return new Queue(getQueueName(EVENT_DELETED), true);
    }

    @Bean
    public Binding userCreatedBinding() {
        return BindingBuilder
                .bind(userCreatedQueue())
                .to(directExchange())
                .with(getRoutingKey(EVENT_CREATED));
    }

    @Bean
    public Binding userUpdatedBinding() {
        return BindingBuilder
                .bind(userUpdatedQueue())
                .to(directExchange())
                .with(getRoutingKey(EVENT_UPDATED));
    }

    @Bean
    public Binding userDeletedBinding() {
        return BindingBuilder
                .bind(userDeletedQueue())
                .to(directExchange())
                .with(getRoutingKey(EVENT_DELETED));
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
