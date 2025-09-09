package com.anla.Order.service;

import com.anla.Order.model.Order;
import com.anla.Order.repository.OrderRepository;
import com.anla.Order.VO.Pelanggan;
import com.anla.Order.VO.Product;
import com.anla.Order.VO.ResponeTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

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

    public ResponeTemplate getOrderWithProdukById(Long id) {
        ResponeTemplate vo = new ResponeTemplate();
        Order order = getOrderById(id);
        Product produk = restTemplate.getForObject(
                "http://localhost:8081/api/products/" + order.getProductId(),
                Product.class
        );
        Pelanggan pelanggan = restTemplate.getForObject(
                "http://localhost:8082/api/pelanggan/" + order.getPelangganId(),
                Pelanggan.class
        );
        vo.setOrder(order);
        vo.setProduk(produk);
        vo.setPelanggan(pelanggan);
        return vo;
    }
}
