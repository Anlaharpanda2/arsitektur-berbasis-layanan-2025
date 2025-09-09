package com.anla.Pelanggan.controller;

import com.anla.Pelanggan.model.Pelanggan;
import com.anla.Pelanggan.service.PelangganService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pelanggan")
public class PelangganController {

    @Autowired
    private PelangganService pelangganService;

    @GetMapping
    public List<Pelanggan> getAllPelanggan() {
        return pelangganService.getAllPelanggan();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pelanggan> getPelangganById(@PathVariable Long id) {
        Pelanggan pelanggan = pelangganService.getPelangganById(id);
        return pelanggan != null ? ResponseEntity.ok(pelanggan) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Pelanggan createPelanggan(@RequestBody Pelanggan pelanggan) {
        return pelangganService.createPelanggan(pelanggan);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pelanggan> updatePelanggan(@PathVariable Long id, @RequestBody Pelanggan pelangganDetails) {
        Pelanggan updatedPelanggan = pelangganService.updatePelanggan(id, pelangganDetails);
        return updatedPelanggan != null ? ResponseEntity.ok(updatedPelanggan) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePelanggan(@PathVariable Long id) {
        pelangganService.deletePelanggan(id);
        return ResponseEntity.ok().build();
    }
}