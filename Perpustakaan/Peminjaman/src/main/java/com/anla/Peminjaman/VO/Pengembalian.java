package com.anla.Peminjaman.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pengembalian {
    private Long id;
    private LocalDate tanggal_kembali;
    private Double denda;
    private Long peminjamanId;
}
