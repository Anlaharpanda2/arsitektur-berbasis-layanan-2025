package com.anla.Buku.service;

import com.anla.Buku.model.Buku;
import com.anla.Buku.repository.BukuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BukuService {

    @Autowired
    private BukuRepository bukuRepository;

    public List<Buku> getAllBuku() {
        return bukuRepository.findAll();
    }

    public Buku getBukuById(Long id) {
        return bukuRepository.findById(id).orElse(null);
    }

    public Buku createBuku(Buku buku) {
        return bukuRepository.save(buku);
    }

    public Buku updateBuku(Long id, Buku bukuDetails) {
        Buku buku = bukuRepository.findById(id).orElse(null);
        if (buku != null) {
            buku.setJudul(bukuDetails.getJudul());
            buku.setPengarang(bukuDetails.getPengarang());
            buku.setPenerbit(bukuDetails.getPenerbit());
            buku.setTahun_terbit(bukuDetails.getTahun_terbit());
            return bukuRepository.save(buku);
        }
        return null;
    }

    public void deleteBuku(Long id) {
        bukuRepository.deleteById(id);
    }
}
