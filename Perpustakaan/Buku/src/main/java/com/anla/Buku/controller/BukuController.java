package com.anla.Buku.controller;

import com.anla.Buku.model.Buku;
import com.anla.Buku.service.BukuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buku")
public class BukuController {

    @Autowired
    private BukuService bukuService;

    @GetMapping
    public List<Buku> getAllBuku() {
        return bukuService.getAllBuku();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Buku> getBukuById(@PathVariable Long id) {
        Buku buku = bukuService.getBukuById(id);
        return buku != null ? ResponseEntity.ok(buku) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Buku createBuku(@RequestBody Buku buku) {
        return bukuService.createBuku(buku);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Buku> updateBuku(@PathVariable Long id, @RequestBody Buku bukuDetails) {
        Buku updatedBuku = bukuService.updateBuku(id, bukuDetails);
        return updatedBuku != null ? ResponseEntity.ok(updatedBuku) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBuku(@PathVariable Long id) {
        bukuService.deleteBuku(id);
        return ResponseEntity.ok().build();
    }
}
