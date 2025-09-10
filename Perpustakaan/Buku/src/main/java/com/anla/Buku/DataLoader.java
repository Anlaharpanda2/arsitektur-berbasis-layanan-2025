package com.anla.Buku;

import com.anla.Buku.model.Buku;
import com.anla.Buku.repository.BukuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private BukuRepository bukuRepository;

    @Override
    public void run(String... args) throws Exception {
        // Cek agar data hanya di-load jika database kosong
        if (bukuRepository.count() == 0) {
            System.out.println("Memuat data buku awal...");
            
            Buku buku1 = new Buku();
            buku1.setJudul("anla pria sigma");
            buku1.setPengarang("anla");
            buku1.setPenerbit("anla pictures");
            buku1.setTahun_terbit("2025");
            bukuRepository.save(buku1);
            
            System.out.println("Data buku berhasil dimuat.");
        }
    }
}
