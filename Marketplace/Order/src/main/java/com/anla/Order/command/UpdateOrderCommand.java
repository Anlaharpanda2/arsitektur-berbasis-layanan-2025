package com.anla.Order.command;

import lombok.Data;

import java.math.BigDecimal;

// DTO ini membawa semua data yang bisa diubah untuk sebuah order
@Data
public class UpdateOrderCommand {
    private String productId;
    private String pelangganId;
    private int jumlah;
    private String status;
    private BigDecimal total;
}
