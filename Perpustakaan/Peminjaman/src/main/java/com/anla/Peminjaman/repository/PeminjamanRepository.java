package com.anla.Peminjaman.repository;

import com.anla.Peminjaman.model.Peminjaman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeminjamanRepository extends JpaRepository<Peminjaman, Long> {
}
