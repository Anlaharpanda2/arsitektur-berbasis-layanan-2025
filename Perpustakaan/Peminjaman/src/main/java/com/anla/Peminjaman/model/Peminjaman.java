package com.anla.Peminjaman.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
public class Peminjaman {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate tanggal_pinjam;
    private LocalDate tanggalDikembalikan;
    private LocalDate tanggal_batas;
    private Long anggotaId;
    private Long bukuId;
}
