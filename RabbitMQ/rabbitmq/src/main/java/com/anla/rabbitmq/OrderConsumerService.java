package com.anla.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderConsumerService {

    private static final Logger log = LoggerFactory.getLogger(OrderConsumerService.class);
    private final OrderRepository orderRepository;
    private final EmailService emailService; // Inject EmailService

    public OrderConsumerService(OrderRepository orderRepository, EmailService emailService) { // Modify constructor
        this.orderRepository = orderRepository;
        this.emailService = emailService; // Initialize EmailService
    }

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    @Transactional
    public void receiveOrder(@Payload Order order) {
        try {
            log.info("Order received from RabbitMQ: {}", order);
            
            // Update status order
            order.setStatus(Order.OrderStatus.PROCESSING);
            orderRepository.save(order);
            
            // Simulasi proses bisnis
            processOrder(order);
            
            // Update status setelah selesai diproses
            order.setStatus(Order.OrderStatus.COMPLETED);
            order.setProcessedAt(java.time.LocalDateTime.now());
            orderRepository.save(order);
            
            log.info("Order processed successfully: {}", order.getId());
            
        } catch (Exception e) {
            log.error("Error processing order: {}, Error: {}", order != null ? order.getId() : "null", e.getMessage());
            log.error("Exception details:", e);
            
            // Update status jika gagal
            if (order != null) {
                order.setStatus(Order.OrderStatus.FAILED);
                orderRepository.save(order);
            }
            
            // We are not re-throwing the exception here to prevent the message
            // from being endlessly requeued, which could happen depending on the broker configuration.
            // In a real-world scenario, you would have a more robust dead-lettering strategy.
        }
    }

    private void processOrder(Order order) {
        // Simulasi proses bisnis
        log.info("Processing order: {}", order.getId());
        
        // Mengirim email konfirmasi
        String emailSubject = "Order Confirmation " + order.getId();
        String emailBody = "Thank you for your order!\n\n" +
                           "Product: " + order.getProductName() + "\n" +
                           "Quantity: " + order.getQuantity() + "\n" +
                           "Price: " + order.getPrice();
        emailService.sendEmail(order.getCustomerEmail(), emailSubject, emailBody);
        
        // Simulasi delay processing
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        log.info("Order processing completed: {}", order.getId());
    }
}
