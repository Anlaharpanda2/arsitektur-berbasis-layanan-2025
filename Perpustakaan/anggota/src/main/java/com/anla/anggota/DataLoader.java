package com.anla.anggota;

import com.anla.anggota.model.Anggota;
import com.anla.anggota.repository.AnggotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private AnggotaRepository anggotaRepository;

    @Override
    public void run(String... args) throws Exception {
        // Cek agar data hanya di-load jika database kosong
        if (anggotaRepository.count() == 0) {
            System.out.println("Memuat data anggota awal...");
            
            Anggota anggota1 = new Anggota();
            anggota1.setNim("2311083015");
            anggota1.setNama("Anla");
            anggota1.setAlamat("Padang");
            anggota1.setJenis_kelamin("Laki-laki");
            anggotaRepository.save(anggota1);
            
            System.out.println("Data anggota berhasil dimuat.");
        }
    }
}
