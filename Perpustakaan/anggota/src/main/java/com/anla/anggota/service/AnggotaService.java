package com.anla.anggota.service;

import com.anla.anggota.model.Anggota;
import com.anla.anggota.repository.AnggotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnggotaService {

    @Autowired
    private AnggotaRepository anggotaRepository;

    public List<Anggota> getAllAnggota() {
        return anggotaRepository.findAll();
    }

    public Anggota getAnggotaById(Long id) {
        return anggotaRepository.findById(id).orElse(null);
    }

    public Anggota createAnggota(Anggota anggota) {
        return anggotaRepository.save(anggota);
    }

    public Anggota updateAnggota(Long id, Anggota anggotaDetails) {
        Anggota anggota = anggotaRepository.findById(id).orElse(null);
        if (anggota != null) {
            anggota.setNim(anggotaDetails.getNim());
            anggota.setNama(anggotaDetails.getNama());
            anggota.setAlamat(anggotaDetails.getAlamat());
            anggota.setJenis_kelamin(anggotaDetails.getJenis_kelamin());
            return anggotaRepository.save(anggota);
        }
        return null;
    }

    public void deleteAnggota(Long id) {
        anggotaRepository.deleteById(id);
    }
}
