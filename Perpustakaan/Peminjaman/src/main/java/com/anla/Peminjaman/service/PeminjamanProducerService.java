package com.anla.Peminjaman.service;

import com.anla.Peminjaman.dto.PeminjamanMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PeminjamanProducerService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${app.rabbitmq.routing.key}")
    private String routingKey;

    @Autowired
    public PeminjamanProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendPeminjamanNotification(PeminjamanMessage message) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
        System.out.println("Sent Peminjaman Notification: " + message);
    }
}
