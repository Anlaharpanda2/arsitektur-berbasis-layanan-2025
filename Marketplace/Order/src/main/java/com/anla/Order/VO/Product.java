package com.anla.Order.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private long id;
    private String nama;
    private String satuan;
    private String harga;
}
