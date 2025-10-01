package com.anla.Order.projection;

import com.anla.Order.config.RabbitMQConfig;
import com.anla.Order.event.OrderCreatedEvent;
import com.anla.Order.event.OrderUpdatedEvent;
import com.anla.Order.model.Order;
import com.anla.Order.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@RabbitListener(queues = RabbitMQConfig.QUEUE_NAME) // Anotasi di level class
public class OrderProjection {

    private final OrderRepository orderRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @RabbitHandler // Method ini akan dipanggil jika message berisi OrderCreatedEvent
    @SneakyThrows
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent for orderId: {}", event.getOrderId());

        Order orderReadModel = new Order();
        orderReadModel.setOrderId(event.getOrderId());
        orderReadModel.setPelangganId(event.getPelangganId());
        orderReadModel.setProductId(event.getProductId());
        orderReadModel.setJumlah(event.getJumlah());
        orderReadModel.setTanggal(event.getTanggal());
        orderReadModel.setStatus(event.getStatus());
        orderReadModel.setTotal(event.getTotal());
        Order savedOrder = orderRepository.save(orderReadModel);
        log.info("Order read model saved with ID: {}", savedOrder.getId());

        String redisKey = "order_detail:" + savedOrder.getId();
        String orderJson = objectMapper.writeValueAsString(savedOrder);
        redisTemplate.opsForValue().set(redisKey, orderJson);
        log.info("Order detail cached in Redis with key: {}", redisKey);
    }

    @RabbitHandler // Method ini akan dipanggil jika message berisi OrderUpdatedEvent
    @SneakyThrows
    public void handleOrderUpdatedEvent(OrderUpdatedEvent event) {
        log.info("Received OrderUpdatedEvent for orderId: {}", event.getOrderId());

        orderRepository.findByOrderId(event.getOrderId()).ifPresentOrElse(order -> {
            order.setProductId(event.getProductId());
            order.setPelangganId(event.getPelangganId());
            order.setJumlah(event.getJumlah());
            order.setStatus(event.getStatus());
            order.setTotal(event.getTotal());
            Order updatedOrder = orderRepository.save(order);
            log.info("Order read model ID {} was updated.", updatedOrder.getId());

            try {
                String redisKey = "order_detail:" + updatedOrder.getId();
                String orderJson = objectMapper.writeValueAsString(updatedOrder);
                redisTemplate.opsForValue().set(redisKey, orderJson);
                log.info("Order detail cache updated in Redis for key: {}", redisKey);
            } catch (Exception e) {
                log.error("Error updating Redis cache", e);
            }

        }, () -> log.warn("Order with orderId {} not found in read model for update.", event.getOrderId()));
    }
}