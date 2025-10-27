package com.anla.Order.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document(collection = "orders")
public class OrderReadModel {
    @Id
    private String id; // MongoDB ObjectId
    
    @org.springframework.data.mongodb.core.index.Indexed(unique = true)
    private String orderId; // Aggregate ID from the event
    
    private String productId;
    
    private String pelangganId;
    
    private int jumlah;
    private LocalDateTime tanggal;
    private String status;
    private BigDecimal total;
}