package com.anla.Peminjaman.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Anggota {
    private Long id;
    private String nama;
    private String alamat;
}
