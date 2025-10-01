package com.anla.Order.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent implements Serializable {
    private String orderId;
    private String pelangganId;
    private String productId;
    private int jumlah;
    private BigDecimal total;
    private LocalDateTime tanggal;
    private String status;
}
