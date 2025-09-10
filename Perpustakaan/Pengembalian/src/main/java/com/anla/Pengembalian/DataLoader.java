package com.anla.Pengembalian;

import com.anla.Pengembalian.model.Pengembalian;
import com.anla.Pengembalian.repository.PengembalianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PengembalianRepository pengembalianRepository;

    @Override
    public void run(String... args) throws Exception {
        // Cek agar data hanya di-load jika database kosong
        if (pengembalianRepository.count() == 0) {
            System.out.println("Memuat data pengembalian awal...");
            
            Pengembalian pengembalian1 = new Pengembalian();
            pengembalian1.setTanggalDikembalikan(LocalDate.now());
            pengembalian1.setTerlambat(0);
            pengembalian1.setDenda(new BigDecimal("0"));
            pengembalian1.setPeminjamanId(1L);
            pengembalianRepository.save(pengembalian1);
            
            System.out.println("Data pengembalian berhasil dimuat.");
        }
    }
}
