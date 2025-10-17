package com.anla.genericservice.command.cqrs;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${app.messaging.exchange.name:events}")
    private String exchangeName;

    public void publish(Event event) {
        rabbitTemplate.convertAndSend(exchangeName, event.getClass().getSimpleName(), event);
    }
}
