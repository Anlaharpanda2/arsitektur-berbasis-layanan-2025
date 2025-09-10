package com.anla.Order.service;

import com.anla.Order.model.Order;
import com.anla.Order.repository.OrderRepository;
import com.anla.Order.VO.Pelanggan;
import com.anla.Order.VO.Product;
import com.anla.Order.VO.ResponeTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DiscoveryClient discoveryClient;
    
    @Autowired
    private RestTemplate restTemplate;

    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Order createOrder(Order order) {
        order.setTanggal(LocalDateTime.now());
        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, Order orderDetails) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setProductId(orderDetails.getProductId());
            order.setPelangganId(orderDetails.getPelangganId());
            order.setJumlah(orderDetails.getJumlah());
            order.setTanggal(orderDetails.getTanggal());
            order.setStatus(orderDetails.getStatus());
            order.setTotal(orderDetails.getTotal());
            return orderRepository.save(order);
        }
        return null;
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public List<ResponeTemplate> getOrderWithProdukById(Long id){
        List<ResponeTemplate> responseList = new ArrayList<>();
        Order order = getOrderById(id);
        if (order == null) {
            return responseList;
        }
        
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
        return responseList;
    }
}
