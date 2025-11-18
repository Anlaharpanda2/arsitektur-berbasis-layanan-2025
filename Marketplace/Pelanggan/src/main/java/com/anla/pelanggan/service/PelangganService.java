package com.anla.pelanggan.service;

import com.anla.pelanggan.model.Pelanggan;
import com.anla.pelanggan.repository.PelangganRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for Pelanggan.
 */
@Service
@SuppressWarnings({"PMD.AtLeastOneConstructor", "PMD.LongVariable", "PMD.ShortVariable", "PMD.UnnecessaryConstructor"})
public class PelangganService {

    /**
     * Repository for Pelanggan data.
     */
    @Autowired
    private PelangganRepository pelangganRepository;

    /**
     * Default constructor.
     */
    public PelangganService() {
        // Default constructor
    }

    /**
     * Get all Pelanggan.
     * @return List of Pelanggan.
     */
    public List<Pelanggan> getAllPelanggan() {
        return pelangganRepository.findAll();
    }

    /**
     * Get Pelanggan by id.
     * @param id The id of the Pelanggan.
     * @return The Pelanggan object.
     */
    public Pelanggan getPelangganById(final Long id) {
        return pelangganRepository.findById(id).orElse(null);
    }

    /**
     * Create a new Pelanggan.
     * @param pelanggan The Pelanggan object to create.
     * @return The created Pelanggan object.
     */
    public Pelanggan createPelanggan(final Pelanggan pelanggan) {
        return pelangganRepository.save(pelanggan);
    }

    /**
     * Update an existing Pelanggan.
     * @param id The id of the Pelanggan to update.
     * @param pelangganDetails The updated Pelanggan details.
     * @return The updated Pelanggan object.
     */
    public Pelanggan updatePelanggan(final Long id, final Pelanggan pelangganDetails) {
        final Pelanggan pelanggan = pelangganRepository.findById(id).orElse(null);
        Pelanggan updatedPelanggan = null;
        if (pelanggan != null) {
            pelanggan.setKode(pelangganDetails.getKode());
            pelanggan.setNama(pelangganDetails.getNama());
            pelanggan.setAlamat(pelangganDetails.getAlamat());
            updatedPelanggan = pelangganRepository.save(pelanggan);
        }
        return updatedPelanggan;
    }

    /**
     * Delete a Pelanggan.
     * @param id The id of the Pelanggan to delete.
     */
    public void deletePelanggan(final Long id) {
        pelangganRepository.deleteById(id);
    }
}