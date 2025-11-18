package com.anla.pelanggan.controller;

import com.anla.pelanggan.model.Pelanggan;
import com.anla.pelanggan.service.PelangganService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for Pelanggan.
 */
@RestController
@RequestMapping("/api/pelanggan")
@SuppressWarnings({"PMD.AtLeastOneConstructor", "PMD.ShortVariable", "PMD.UnnecessaryConstructor"})
public class PelangganController {

    /**
     * Service for Pelanggan.
     */
    @Autowired
    private PelangganService pelangganService;

    /**
     * Default constructor.
     */
    public PelangganController() {
        // Default constructor
    }

    /**
     * Get all Pelanggan.
     * @return List of Pelanggan.
     */
    @GetMapping
    public List<Pelanggan> getAllPelanggan() {
        return pelangganService.getAllPelanggan();
    }

    /**
     * Get Pelanggan by id.
     * @param id The id of the Pelanggan.
     * @return The Pelanggan object.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pelanggan> getPelangganById(@PathVariable final Long id) {
        final Pelanggan pelanggan = pelangganService.getPelangganById(id);
        return pelanggan != null ? ResponseEntity.ok(pelanggan) : ResponseEntity.notFound().build();
    }

    /**
     * Create a new Pelanggan.
     * @param pelanggan The Pelanggan object to create.
     * @return The created Pelanggan object.
     */
    @PostMapping
    public Pelanggan createPelanggan(@RequestBody final Pelanggan pelanggan) {
        return pelangganService.createPelanggan(pelanggan);
    }

    /**
     * Update an existing Pelanggan.
     * @param id The id of the Pelanggan to update.
     * @param pelangganDetails The updated Pelanggan details.
     * @return The updated Pelanggan object.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Pelanggan> updatePelanggan(@PathVariable final Long id, @RequestBody final Pelanggan pelangganDetails) {
        final Pelanggan updatedPelanggan = pelangganService.updatePelanggan(id, pelangganDetails);
        return updatedPelanggan != null ? ResponseEntity.ok(updatedPelanggan) : ResponseEntity.notFound().build();
    }

    /**
     * Delete a Pelanggan.
     * @param id The id of the Pelanggan to delete.
     * @return ResponseEntity with status code.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePelanggan(@PathVariable final Long id) {
        pelangganService.deletePelanggan(id);
        return ResponseEntity.ok().build();
    }
}