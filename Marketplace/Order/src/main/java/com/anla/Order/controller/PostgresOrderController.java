package com.anla.Order.controller;

import com.anla.Order.model.Order;
import com.anla.Order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/order")
public class PostgresOrderController {

    @Autowired
    private OrderService orderService; // PostgreSQL Query Service

    @GetMapping("/postgres")
    public ResponseEntity<Map<String, Object>> getAllOrderFromPostgres() {
        log.info("Starting performance benchmark for PostgreSQL - getAllOrder");
        long startTime = System.currentTimeMillis();
        
        List<Order> orders = orderService.getAllOrder();
        
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        
        
        Map<String, Object> response = Map.of(
            "database", "PostgreSQL",
            "execution_time_ms", executionTime,
            "total_records", orders.size(),
            "data", orders,
            "timestamp", java.time.LocalDateTime.now()
        );
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/postgres/{id}")
    public ResponseEntity<Map<String, Object>> getOrderFromPostgresById(@PathVariable Long id) {
        log.info("Starting performance benchmark for PostgreSQL - getOrderById: {}", id);
        long startTime = System.currentTimeMillis();
        
        Order order = orderService.getOrderById(id);
        
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        
        if (order != null) {
            log.info("PostgreSQL getOrderById({}) completed in {} ms, order found", id, executionTime);
            Map<String, Object> response = Map.of(
                "database", "PostgreSQL", 
                "execution_time_ms", executionTime,
                "data", order,
                "timestamp", java.time.LocalDateTime.now()
            );
            return ResponseEntity.ok(response);
        } else {
            log.warn("PostgreSQL getOrderById({}) completed in {} ms, order not found", id, executionTime);
            Map<String, Object> response = Map.of(
                "database", "PostgreSQL",
                "execution_time_ms", executionTime, 
                "error", "Order not found",
                "timestamp", java.time.LocalDateTime.now()
            );
            return ResponseEntity.status(404).body(response);
        }
    }
}