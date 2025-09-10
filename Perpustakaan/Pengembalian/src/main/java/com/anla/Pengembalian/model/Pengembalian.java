package com.anla.Pengembalian.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
public class Pengembalian {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate tanggalDikembalikan;
    private int terlambat; // dalam hari
    private BigDecimal denda;
    private Long peminjamanId; // ID dari service Peminjaman
}
