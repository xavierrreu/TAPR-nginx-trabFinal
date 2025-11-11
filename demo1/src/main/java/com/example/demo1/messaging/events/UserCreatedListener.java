package com.example.demo1.messaging.events;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.demo1.messaging.RabbitConfig;

@Component
public class UserCreatedListener {

    @RabbitListener(queues = RabbitConfig.NOTIFICATION_QUEUE)
    public void onUserCreated(UserCreatedEvent event) {
        System.out.println("Recebemos o usuário da fila de notificação: " + event);
    }
}
