package com.anla.anggota.controller;

import com.anla.anggota.model.Anggota;
import com.anla.anggota.service.AnggotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for Anggota.
 */
@RestController
@RequestMapping("/api/anggota")
@SuppressWarnings({"PMD.AtLeastOneConstructor", "PMD.ShortVariable"})
public class AnggotaController {

    /**
     * Service for Anggota.
     */
    @Autowired
    private AnggotaService anggotaService;

    /**
     * Get all Anggota.
     * @return List of Anggota.
     */
    @GetMapping
    public List<Anggota> getAllAnggota() {
        return anggotaService.getAllAnggota();
    }

    /**
     * Get Anggota by id.
     * @param id The id of the Anggota.
     * @return The Anggota object.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Anggota> getAnggotaById(@PathVariable final Long id) {
        final Anggota anggota = anggotaService.getAnggotaById(id);
        return anggota != null ? ResponseEntity.ok(anggota) : ResponseEntity.notFound().build();
    }

    /**
     * Create a new Anggota.
     * @param anggota The Anggota object to create.
     * @return The created Anggota object.
     */
    @PostMapping
    public Anggota createAnggota(@RequestBody final Anggota anggota) {
        return anggotaService.createAnggota(anggota);
    }

    /**
     * Update an Anggota.
     * @param id The id of the Anggota to update.
     * @param anggotaDetails The updated Anggota details.
     * @return The updated Anggota object.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Anggota> updateAnggota(@PathVariable final Long id, @RequestBody final Anggota anggotaDetails) {
        final Anggota updatedAnggota = anggotaService.updateAnggota(id, anggotaDetails);
        return updatedAnggota != null ? ResponseEntity.ok(updatedAnggota) : ResponseEntity.notFound().build();
    }

    /**
     * Delete an Anggota.
     * @param id The id of the Anggota to delete.
     * @return ResponseEntity with status code.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnggota(@PathVariable final Long id) {
        anggotaService.deleteAnggota(id);
        return ResponseEntity.ok().build();
    }
}
