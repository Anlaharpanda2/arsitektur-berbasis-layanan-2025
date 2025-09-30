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
    private LocalDate tanggalDikembalikan;
    private Integer terlambat;
    private Double denda;
    private Long peminjamanId;
}
