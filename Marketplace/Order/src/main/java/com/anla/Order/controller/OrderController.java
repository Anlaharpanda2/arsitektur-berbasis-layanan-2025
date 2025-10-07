package com.anla.Order.controller;

import com.anla.Order.command.CreateOrderCommand;
import com.anla.Order.model.Order;
import com.anla.Order.service.OrderCommandService;
import com.anla.Order.service.OrderQueryService;
import com.anla.Order.service.OrderService;
import com.anla.Order.VO.ResponeTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import com.anla.Order.command.UpdateOrderCommand;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderQueryService orderQueryService; // MongoDB Query Service

    @Autowired
    private OrderService orderService; // PostgreSQL Query Service (Legacy)

    @Autowired
    private OrderCommandService orderCommandService;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody CreateOrderCommand command) {
        String orderId = orderCommandService.handleCreateOrder(command);
        return ResponseEntity.accepted().body("Order nyo terkirim dengan ID : " + orderId);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Void> updateOrder(@PathVariable String orderId, @RequestBody UpdateOrderCommand command) {
        orderCommandService.handleUpdateOrder(orderId, command);
        return ResponseEntity.accepted().build();
    }

    // READ FROM MONGODB (Primary Read Model)
    @GetMapping
    public List<Order> getAllOrder() {
        return orderQueryService.getAllOrder();
    }

    @GetMapping("/debug/mongo")
    public ResponseEntity<?> debugMongoData() {
        try {
            List<Order> orders = orderQueryService.getAllOrder();
            return ResponseEntity.ok(Map.of(
                "total_orders", orders.size(),
                "orders", orders
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "error", e.getMessage(),
                "message", "Error fetching from MongoDB"
            ));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderQueryService.getOrderById(id);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
    }

    @GetMapping("/uuid/{orderId}")
    public ResponseEntity<Order> getOrderByOrderId(@PathVariable String orderId) {
        Order order = orderQueryService.getOrderByOrderId(orderId);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponeTemplate> getOrderWithProdukById(@PathVariable Long id) {
        List<ResponeTemplate> results = orderQueryService.getOrderWithProdukById(id);
        if (results.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(results.get(0));
    }

    // READ FROM POSTGRESQL (Secondary Read Model)
    @GetMapping("/postgres")
    public List<Order> getAllOrderFromPostgres() {
        return orderService.getAllOrder();
    }

    @GetMapping("/postgres/{id}")
    public ResponseEntity<Order> getOrderFromPostgresById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
    }

    @GetMapping("/postgres/detail/{id}")
    public ResponseEntity<ResponeTemplate> getOrderWithProdukFromPostgresById(@PathVariable Long id) {
        List<ResponeTemplate> results = orderService.getOrderWithProdukById(id);
        if (results.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(results.get(0));
    }
}
