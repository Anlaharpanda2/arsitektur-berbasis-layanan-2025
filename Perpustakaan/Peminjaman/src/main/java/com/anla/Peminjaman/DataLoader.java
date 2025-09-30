package com.anla.Peminjaman;

import com.anla.Peminjaman.model.Peminjaman;
import com.anla.Peminjaman.repository.PeminjamanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PeminjamanRepository peminjamanRepository;

    @Override
    public void run(String... args) throws Exception {
        if (peminjamanRepository.count() == 0) {
            System.out.println("Memuat data peminjaman awal...");
            
            Peminjaman peminjaman1 = new Peminjaman();
            peminjaman1.setTanggal_pinjam(LocalDate.now());
            peminjaman1.setTanggalDikembalikan(LocalDate.now().plusDays(10));
            peminjaman1.setTanggal_batas(LocalDate.now().plusDays(7));
            peminjaman1.setAnggotaId(1L); // ID Anggota contoh
            peminjaman1.setBukuId(1L); // ID Buku contoh
            peminjamanRepository.save(peminjaman1);
            
            System.out.println("Data peminjaman berhasil dimuat.");
        }
    }
}
