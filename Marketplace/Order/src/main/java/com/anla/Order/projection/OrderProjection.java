package com.anla.Order.projection;

import com.anla.Order.config.RabbitMQConfig;
import com.anla.Order.event.OrderCreatedEvent;
import com.anla.Order.event.OrderUpdatedEvent;
import com.anla.Order.model.Order;
import com.anla.Order.model.OrderReadModel;
import com.anla.Order.repository.OrderRepository;
import com.anla.Order.repository.mongo.OrderReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@RabbitListener(queues = RabbitMQConfig.QUEUE_NAME) // Anotasi di level class
public class OrderProjection {

    private final OrderReadRepository orderReadRepository; // MongoDB Repository
    private final OrderRepository orderRepository; // PostgreSQL Repository

    @RabbitHandler // Method ini akan dipanggil jika message berisi OrderCreatedEvent
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent for orderId: {}", event.getOrderId());

        // 1. Save to MongoDB Read Model
        OrderReadModel orderReadModel = new OrderReadModel();
        orderReadModel.setOrderId(event.getOrderId());
        orderReadModel.setPelangganId(event.getPelangganId());
        orderReadModel.setProductId(event.getProductId());
        orderReadModel.setJumlah(event.getJumlah());
        orderReadModel.setTanggal(event.getTanggal());
        orderReadModel.setStatus(event.getStatus());
        orderReadModel.setTotal(event.getTotal());
        
        OrderReadModel savedOrderMongo = orderReadRepository.save(orderReadModel);
        log.info("Order read model tersimpan ke MongoDB dengan ID: {}", savedOrderMongo.getId());

        // 2. Save to PostgreSQL Read Model (orders table)
        Order orderPostgres = new Order();
        orderPostgres.setOrderId(event.getOrderId());
        orderPostgres.setPelangganId(event.getPelangganId());
        orderPostgres.setProductId(event.getProductId());
        orderPostgres.setJumlah(event.getJumlah());
        orderPostgres.setTanggal(event.getTanggal());
        orderPostgres.setStatus(event.getStatus());
        orderPostgres.setTotal(event.getTotal());

        Order savedOrderPostgres = orderRepository.save(orderPostgres);
        log.info("Order read model tersimpan ke PostgreSQL dengan ID: {}", savedOrderPostgres.getId());
    }

    @RabbitHandler // Method ini akan dipanggil jika message berisi OrderUpdatedEvent
    public void handleOrderUpdatedEvent(OrderUpdatedEvent event) {
        log.info("Received OrderUpdatedEvent for orderId: {}", event.getOrderId());

        // 1. Update MongoDB Read Model
        orderReadRepository.findByOrderId(event.getOrderId()).ifPresentOrElse(order -> {
            order.setProductId(event.getProductId());
            order.setPelangganId(event.getPelangganId());
            order.setJumlah(event.getJumlah());
            order.setStatus(event.getStatus());
            order.setTotal(event.getTotal());
            OrderReadModel updatedOrderMongo = orderReadRepository.save(order);
            log.info("Order read model ID {} was updated in MongoDB.", updatedOrderMongo.getId());

        }, () -> log.warn("Order with orderId {} not found in MongoDB read model for update.", event.getOrderId()));

        // 2. Update PostgreSQL Read Model
        orderRepository.findByOrderId(event.getOrderId()).ifPresentOrElse(order -> {
            order.setProductId(event.getProductId());
            order.setPelangganId(event.getPelangganId());
            order.setJumlah(event.getJumlah());
            order.setStatus(event.getStatus());
            order.setTotal(event.getTotal());
            Order updatedOrderPostgres = orderRepository.save(order);
            log.info("Order read model ID {} was updated in PostgreSQL.", updatedOrderPostgres.getId());

        }, () -> log.warn("Order with orderId {} not found in PostgreSQL read model for update.", event.getOrderId()));
    }
}