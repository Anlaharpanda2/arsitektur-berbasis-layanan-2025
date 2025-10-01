package com.anla.Order.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdatedEvent implements Serializable {
    private String orderId;
    private String productId;
    private String pelangganId;
    private int jumlah;
    private String status;
    private BigDecimal total;
}
