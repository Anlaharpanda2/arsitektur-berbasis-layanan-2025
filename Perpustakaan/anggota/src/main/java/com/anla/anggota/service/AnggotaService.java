package com.anla.anggota.service;

import com.anla.anggota.model.Anggota;
import com.anla.anggota.repository.AnggotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for Anggota.
 */
@Service
@SuppressWarnings({"PMD.AtLeastOneConstructor", "PMD.ShortVariable"})
public class AnggotaService {

    /**
     * Repository for Anggota data.
     */
    @Autowired
    private AnggotaRepository anggotaRepository;

    /**
     * Get all Anggota.
     * @return List of Anggota.
     */
    public List<Anggota> getAllAnggota() {
        return anggotaRepository.findAll();
    }

    /**
     * Get Anggota by id.
     * @param id The id of the Anggota.
     * @return The Anggota object.
     */
    public Anggota getAnggotaById(final Long id) {
        return anggotaRepository.findById(id).orElse(null);
    }

    /**
     * Create a new Anggota.
     * @param anggota The Anggota object to create.
     * @return The created Anggota object.
     */
    public Anggota createAnggota(final Anggota anggota) {
        return anggotaRepository.save(anggota);
    }

    /**
     * Update an Anggota.
     * @param id The id of the Anggota to update.
     * @param anggotaDetails The updated Anggota details.
     * @return The updated Anggota object.
     */
    public Anggota updateAnggota(final Long id, final Anggota anggotaDetails) {
        final Anggota anggota = anggotaRepository.findById(id).orElse(null);
        Anggota updatedAnggota = null;
        if (anggota != null) {
            anggota.setNim(anggotaDetails.getNim());
            anggota.setNama(anggotaDetails.getNama());
            anggota.setAlamat(anggotaDetails.getAlamat());
            anggota.setJenisKelamin(anggotaDetails.getJenisKelamin());
            updatedAnggota = anggotaRepository.save(anggota);
        }
        return updatedAnggota;
    }

    /**
     * Delete an Anggota.
     * @param id The id of the Anggota to delete.
     */
    public void deleteAnggota(final Long id) {
        anggotaRepository.deleteById(id);
    }
}