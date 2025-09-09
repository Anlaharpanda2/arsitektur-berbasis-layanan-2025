package com.anla.Order.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pelanggan {
    private long id;
    private String kode;
    private String nama;
    private String alamat;
}