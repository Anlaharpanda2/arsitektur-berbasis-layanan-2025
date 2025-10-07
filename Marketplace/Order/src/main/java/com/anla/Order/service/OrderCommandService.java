package com.anla.Order.service;

import com.anla.Order.command.CreateOrderCommand;
import com.anla.Order.command.UpdateOrderCommand;
import com.anla.Order.config.RabbitMQConfig;
import com.anla.Order.event.OrderCreatedEvent;
import com.anla.Order.event.OrderUpdatedEvent;
import com.anla.Order.event.store.OrderEvent;
import com.anla.Order.event.store.OrderEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderCommandService {

    private final OrderEventRepository orderEventRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper; // Spring Boot auto-configures this bean

    @Transactional
    @SneakyThrows 
    public String handleCreateOrder(CreateOrderCommand command) {
        String orderId = UUID.randomUUID().toString();

        // 1. Create the event
        OrderCreatedEvent eventPayload = new OrderCreatedEvent(
                orderId,
                command.getPelangganId(),
                command.getProductId(),
                command.getJumlah(),
                command.getTotal(),
                LocalDateTime.now(),
                "PENDING"
        );

        // 2. Create the event store record
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setAggregateId(orderId);
        orderEvent.setEventType("ORDER_CREATED");
        orderEvent.setPayload(objectMapper.writeValueAsString(eventPayload));

        // 3. Save the event to the Event Store (PostgreSQL)
        orderEventRepository.save(orderEvent);

        // 4. Publish the event to the message bus (RabbitMQ)
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_CREATED, eventPayload);

        return orderId;
    }

    @Transactional
    @SneakyThrows
    public void handleUpdateOrder(String orderId, UpdateOrderCommand command) {
        // TODO: Add validation here to ensure the order exists by checking the event store.

        // 1. Create the event
        OrderUpdatedEvent eventPayload = new OrderUpdatedEvent(
                orderId,
                command.getProductId(),
                command.getPelangganId(),
                command.getJumlah(),
                command.getStatus(),
                command.getTotal()
        );

        // 2. Create the event store record
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setAggregateId(orderId);
        orderEvent.setEventType("ORDER_UPDATED");
        orderEvent.setPayload(objectMapper.writeValueAsString(eventPayload));

        // 3. Save the event to the Event Store (PostgreSQL)
        orderEventRepository.save(orderEvent);

        // 4. Publish the event to the message bus (RabbitMQ)
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_UPDATED, eventPayload);
    }
}