package com.anla.Order.controller;

import com.anla.Order.command.CreateOrderCommand;
import com.anla.Order.model.Order;
import com.anla.Order.service.OrderCommandService;
import com.anla.Order.service.OrderQueryService;
import com.anla.Order.VO.ResponeTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.anla.Order.command.UpdateOrderCommand;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderQueryService orderQueryService;

    @Autowired
    private OrderCommandService orderCommandService;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody CreateOrderCommand command) {
        String orderId = orderCommandService.handleCreateOrder(command);
        return ResponseEntity.accepted().body("Order creation process started with ID: " + orderId);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Void> updateOrder(@PathVariable String orderId, @RequestBody UpdateOrderCommand command) {
        orderCommandService.handleUpdateOrder(orderId, command);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public List<Order> getAllOrder() {
        return orderQueryService.getAllOrder();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderQueryService.getOrderById(id);
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
}
