package com.anla.anggota.controller;

import com.anla.anggota.model.Anggota;
import com.anla.anggota.service.AnggotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/anggota")
public class AnggotaController {

    @Autowired
    private AnggotaService anggotaService;

    @GetMapping
    public List<Anggota> getAllAnggota() {
        return anggotaService.getAllAnggota();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Anggota> getAnggotaById(@PathVariable Long id) {
        Anggota anggota = anggotaService.getAnggotaById(id);
        return anggota != null ? ResponseEntity.ok(anggota) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Anggota createAnggota(@RequestBody Anggota anggota) {
        return anggotaService.createAnggota(anggota);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Anggota> updateAnggota(@PathVariable Long id, @RequestBody Anggota anggotaDetails) {
        Anggota updatedAnggota = anggotaService.updateAnggota(id, anggotaDetails);
        return updatedAnggota != null ? ResponseEntity.ok(updatedAnggota) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnggota(@PathVariable Long id) {
        anggotaService.deleteAnggota(id);
        return ResponseEntity.ok().build();
    }
}