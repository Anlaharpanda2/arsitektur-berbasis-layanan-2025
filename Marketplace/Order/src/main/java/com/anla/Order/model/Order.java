package com.anla.Order.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String orderId; // Aggregate ID from the event
    private String productId;
    private String pelangganId;
    private int jumlah;
    private LocalDateTime tanggal;
    private String status;
    private BigDecimal total;
}
