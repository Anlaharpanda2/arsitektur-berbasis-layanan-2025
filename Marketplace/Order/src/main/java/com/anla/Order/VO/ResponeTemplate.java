package com.anla.Order.VO;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Producer;

import com.anla.Order.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponeTemplate {
    private Order order;
    private Product produk;
    private Pelanggan pelanggan;
}