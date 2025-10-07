package com.anla.anggota;

import com.anla.anggota.model.Anggota;
import com.anla.anggota.repository.AnggotaRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Loads initial data into the database.
 */
@Component
@SuppressWarnings({"PMD.AtLeastOneConstructor", "PMD.CommentRequired"})
public class DataLoader implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);

    /**
     * Repository for Anggota data.
     */
    @Autowired
    private AnggotaRepository anggotaRepository;

    @Override
    public void run(final String... args) throws Exception {
        // Cek agar data hanya di-load jika database kosong
        if (anggotaRepository.count() == 0) {
            LOGGER.info("Memuat data anggota awal...");
            
            final Anggota anggota1 = new Anggota();
            anggota1.setNim("2311083015");
            anggota1.setNama("Anla");
            anggota1.setAlamat("Padang");
            anggota1.setJenisKelamin("Laki-laki");
            anggota1.setEmail("anlaharpanda@gmail.com");
            anggotaRepository.save(anggota1);
            
            LOGGER.info("Data anggota berhasil dimuat.");
        }
    }
}