package com.anla.Buku.repository;

import com.anla.Buku.model.Buku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BukuRepository extends JpaRepository<Buku, Long> {
}
