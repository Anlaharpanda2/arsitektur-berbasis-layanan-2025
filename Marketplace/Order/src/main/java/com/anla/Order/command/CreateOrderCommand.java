package com.anla.Order.command;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateOrderCommand {
    private String pelangganId;
    private String productId;
    private int jumlah;
    private BigDecimal total;
}
