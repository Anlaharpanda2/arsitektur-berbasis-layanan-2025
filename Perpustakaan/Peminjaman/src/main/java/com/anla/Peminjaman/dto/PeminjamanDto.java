package com.anla.Peminjaman.dto;

import com.anla.Peminjaman.model.Peminjaman;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
public class PeminjamanDto {
    private Peminjaman peminjaman;
    private Double denda;

    public PeminjamanDto(Peminjaman peminjaman) {
        this.peminjaman = peminjaman;
        this.denda = calculateDenda();
    }

    private Double calculateDenda() {
        if (peminjaman.getTanggalDikembalikan() != null && peminjaman.getTanggal_batas() != null) {
            if (peminjaman.getTanggalDikembalikan().isAfter(peminjaman.getTanggal_batas())) {
                long daysOverdue = ChronoUnit.DAYS.between(peminjaman.getTanggal_batas(), peminjaman.getTanggalDikembalikan());
                return daysOverdue * 1000.0;
            }
        }
        return 0.0;
    }
}
