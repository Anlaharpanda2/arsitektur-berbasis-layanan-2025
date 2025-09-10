package com.anla.Pengembalian.controller;

import com.anla.Pengembalian.model.Pengembalian;
import com.anla.Pengembalian.service.PengembalianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pengembalian")
public class PengembalianController {

    @Autowired
    private PengembalianService pengembalianService;

    @GetMapping
    public List<Pengembalian> getAllPengembalian() {
        return pengembalianService.getAllPengembalian();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pengembalian> getPengembalianById(@PathVariable Long id) {
        Pengembalian pengembalian = pengembalianService.getPengembalianById(id);
        return pengembalian != null ? ResponseEntity.ok(pengembalian) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Pengembalian createPengembalian(@RequestBody Pengembalian pengembalian) {
        return pengembalianService.createPengembalian(pengembalian);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pengembalian> updatePengembalian(@PathVariable Long id, @RequestBody Pengembalian pengembalianDetails) {
        Pengembalian updatedPengembalian = pengembalianService.updatePengembalian(id, pengembalianDetails);
        return updatedPengembalian != null ? ResponseEntity.ok(updatedPengembalian) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePengembalian(@PathVariable Long id) {
        pengembalianService.deletePengembalian(id);
        return ResponseEntity.ok().build();
    }
}
