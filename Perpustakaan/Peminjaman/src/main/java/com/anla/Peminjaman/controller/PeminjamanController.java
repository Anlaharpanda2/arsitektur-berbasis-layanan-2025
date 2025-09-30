package com.anla.Peminjaman.controller;

import com.anla.Peminjaman.VO.ResponseTemplateVO;
import com.anla.Peminjaman.dto.PeminjamanDto;
import com.anla.Peminjaman.model.Peminjaman;
import com.anla.Peminjaman.service.PeminjamanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/peminjaman")
public class PeminjamanController {

    @Autowired
    private PeminjamanService peminjamanService;

    @GetMapping
    public List<Peminjaman> getAllPeminjaman() {
        return peminjamanService.getAllPeminjaman();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Peminjaman> getPeminjamanById(@PathVariable Long id) {
        Peminjaman peminjaman = peminjamanService.getPeminjamanById(id);
        return peminjaman != null ? ResponseEntity.ok(peminjaman) : ResponseEntity.notFound().build();
    }

    @GetMapping("/denda/{id}")
    public ResponseEntity<PeminjamanDto> getPeminjamanWithDenda(@PathVariable Long id) {
        PeminjamanDto peminjamanDto = peminjamanService.getPeminjamanWithDenda(id);
        return peminjamanDto != null ? ResponseEntity.ok(peminjamanDto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Peminjaman createPeminjaman(@RequestBody Peminjaman peminjaman) {
        return peminjamanService.createPeminjaman(peminjaman);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Peminjaman> updatePeminjaman(@PathVariable Long id, @RequestBody Peminjaman peminjamanDetails) {
        Peminjaman updatedPeminjaman = peminjamanService.updatePeminjaman(id, peminjamanDetails);
        return updatedPeminjaman != null ? ResponseEntity.ok(updatedPeminjaman) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePeminjaman(@PathVariable Long id) {
        peminjamanService.deletePeminjaman(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseTemplateVO> getPeminjamanWithDetailById(@PathVariable Long id) {
        List<ResponseTemplateVO> results = peminjamanService.getPeminjamanWithDetailById(id);
        if (results.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(results.get(0));
    }
}
