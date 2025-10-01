package com.anla.Order.service;

import com.anla.Order.model.Order;
import com.anla.Order.repository.OrderRepository;
import com.anla.Order.VO.Pelanggan;
import com.anla.Order.VO.Product;
import com.anla.Order.VO.ResponeTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    @SneakyThrows
    public Order getOrderById(Long id) {
        // 1. Coba cari di cache (Redis) dulu
        String redisKey = "order_detail:" + id;
        String orderJson = redisTemplate.opsForValue().get(redisKey);

        if (orderJson != null) {
            log.info("Cache hit for key: {}", redisKey);
            return objectMapper.readValue(orderJson, Order.class);
        }

        // 2. Jika tidak ada di cache, cari di database (Postgres Read Model)
        log.info("Cache miss for key: {}. Fetching from database.", redisKey);
        Order order = orderRepository.findById(id).orElse(null);

        // 3. Jika ditemukan di DB, simpan ke cache untuk request berikutnya
        if (order != null) {
            String newOrderJson = objectMapper.writeValueAsString(order);
            redisTemplate.opsForValue().set(redisKey, newOrderJson, 10, TimeUnit.MINUTES); // Cache for 10 minutes
        }

        return order;
    }

    public List<ResponeTemplate> getOrderWithProdukById(Long id){
        List<ResponeTemplate> responseList = new ArrayList<>();
        Order order = getOrderById(id);
        if (order == null) {
            return responseList;
        }

        // Komunikasi sinkron ini tetap ada untuk saat ini sesuai rencana
        Product produk = restTemplate.getForObject("http://PRODUK-SERVICE/api/produk/"
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
