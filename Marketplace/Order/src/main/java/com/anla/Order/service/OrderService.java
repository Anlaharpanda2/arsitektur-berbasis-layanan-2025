package com.anla.Order.service;

import com.anla.Order.model.Order;
import com.anla.Order.repository.OrderRepository;
import com.anla.Order.VO.Pelanggan;
import com.anla.Order.VO.Product;
import com.anla.Order.VO.ResponeTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DiscoveryClient discoveryClient;
    
    @Autowired
    private RestTemplate restTemplate;

    public List<Order> getAllOrder() {
        log.info("Fetching all orders from PostgreSQL");
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        log.info("Fetching order by ID: {} from PostgreSQL", id);
        return orderRepository.findById(id).orElse(null);
    }

    public Order createOrder(Order order) {
        log.info("Creating new order in PostgreSQL");
        order.setTanggal(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);
        log.info("Order created in PostgreSQL with ID: {}", savedOrder.getId());
        return savedOrder;
    }

    public Order updateOrder(Long id, Order orderDetails) {
        log.info("Updating order ID: {} in PostgreSQL", id);
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setProductId(orderDetails.getProductId());
            order.setPelangganId(orderDetails.getPelangganId());
            order.setJumlah(orderDetails.getJumlah());
            order.setTanggal(orderDetails.getTanggal());
            order.setStatus(orderDetails.getStatus());
            order.setTotal(orderDetails.getTotal());
            Order updatedOrder = orderRepository.save(order);
            log.info("Order ID: {} updated successfully in PostgreSQL", updatedOrder.getId());
            return updatedOrder;
        }
        log.warn("Order ID: {} not found in PostgreSQL for update", id);
        return null;
    }

    public void deleteOrder(Long id) {
        log.info("Deleting order ID: {} from PostgreSQL", id);
        orderRepository.deleteById(id);
        log.info("Order ID: {} deleted from PostgreSQL", id);
    }

    public List<ResponeTemplate> getOrderWithProdukById(Long id){
        log.info("Fetching order with product details by ID: {} from PostgreSQL", id);
        List<ResponeTemplate> responseList = new ArrayList<>();
        Order order = getOrderById(id);
        if (order == null) {
            log.warn("Order ID: {} not found in PostgreSQL", id);
            return responseList;
        }
        
        log.info("Fetching product and customer data for order ID: {}", id);
        // Menggunakan @LoadBalanced RestTemplate dengan nama service dari Eureka
        Product produk = restTemplate.getForObject("http://PRODUK-SERVICE/api/products/"
                + order.getProductId(), Product.class);

        Pelanggan pelanggan = restTemplate.getForObject("http://PELANGGAN-SERVICE/api/pelanggan/"
                + order.getPelangganId(), Pelanggan.class);
        
        ResponeTemplate vo = new ResponeTemplate();
        vo.setOrder(order);
        vo.setProduk(produk);
        vo.setPelanggan(pelanggan);
        responseList.add(vo);
        log.info("Successfully fetched order with product details for ID: {}", id);
        return responseList;
    }
}
