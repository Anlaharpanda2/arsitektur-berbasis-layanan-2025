package com.anla.Order;

import com.anla.Order.model.Order;
import com.anla.Order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Memuat data order awal...");

        // Cek jika data sudah ada untuk menghindari duplikasi
        if (orderRepository.count() == 0) {
            // Data pertama yang disimpan akan mendapatkan ID 1 secara otomatis.
            Order order1 = new Order();
            order1.setPelangganId("1"); // FIX: Menggunakan String
            order1.setProductId("1");   // FIX: Menggunakan String
            order1.setJumlah(10);
            order1.setTanggal(LocalDateTime.now());
            order1.setStatus("PENDING");
            order1.setTotal(new BigDecimal("150000")); // FIX: Menggunakan BigDecimal
            orderRepository.save(order1);
            System.out.println("Data order untuk ID 1 berhasil dibuat.");
            
        } else {
            System.out.println("Data order sudah ada, tidak perlu memuat ulang.");
        }
    }
}
