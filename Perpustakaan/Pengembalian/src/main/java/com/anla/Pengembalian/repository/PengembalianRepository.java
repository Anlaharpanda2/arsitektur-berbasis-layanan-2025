package com.anla.Pengembalian.repository;

import com.anla.Pengembalian.model.Pengembalian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PengembalianRepository extends JpaRepository<Pengembalian, Long> {
}
