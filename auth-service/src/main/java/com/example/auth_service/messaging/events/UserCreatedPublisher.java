package com.example.auth_service.messaging.events;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.example.auth_service.messaging.RabbitConfig;

@Service
public class UserCreatedPublisher {
    private final RabbitTemplate template;

    public UserCreatedPublisher(RabbitTemplate template) {
        this.template = template;
    }

    public void publish(UserCreatedEvent event) {
        template.convertAndSend(
            RabbitConfig.EXCHANGE,
            RabbitConfig.USER_CREATED_ROUTING_KEY,
            event
        );
    }
}
