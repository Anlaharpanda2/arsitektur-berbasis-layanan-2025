package com.anla.Order;

import com.anla.Order.model.Order;
import com.anla.Order.model.OrderReadModel;
import com.anla.Order.repository.OrderRepository;
import com.anla.Order.repository.mongo.OrderReadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.UUID;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private OrderRepository orderRepository; // PostgreSQL untuk write model (event store tetap)

    @Autowired
    private OrderReadRepository orderReadRepository; // MongoDB untuk read model

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Memuat data order awal...");

        // Cek jika data sudah ada untuk menghindari duplikasi
        if (orderRepository.count() == 0 && orderReadRepository.count() == 0) {
            String orderId = UUID.randomUUID().toString();
            
            // Data untuk PostgreSQL (Write Model - Event Store tetap menggunakan ini)
            Order order1 = new Order();
            order1.setOrderId(orderId);
            order1.setPelangganId("1");
            order1.setProductId("1");
            order1.setJumlah(10);
            order1.setTanggal(LocalDateTime.now());
            order1.setStatus("PENDING");
            order1.setTotal(new BigDecimal("150000"));
            orderRepository.save(order1);
            System.out.println("Data order untuk PostgreSQL berhasil dibuat dengan ID: " + order1.getId());

            // Data untuk MongoDB (Read Model)
            OrderReadModel orderReadModel = new OrderReadModel();
            orderReadModel.setOrderId(orderId);
            orderReadModel.setPelangganId("1");
            orderReadModel.setProductId("1");
            orderReadModel.setJumlah(10);
            orderReadModel.setTanggal(LocalDateTime.now());
            orderReadModel.setStatus("PENDING");
            orderReadModel.setTotal(new BigDecimal("150000"));
            orderReadRepository.save(orderReadModel);
            System.out.println("Data order untuk MongoDB berhasil dibuat dengan ID: " + orderReadModel.getId());
            
        } else {
            System.out.println("Data order sudah ada, tidak perlu memuat ulang.");
        }
    }
}
