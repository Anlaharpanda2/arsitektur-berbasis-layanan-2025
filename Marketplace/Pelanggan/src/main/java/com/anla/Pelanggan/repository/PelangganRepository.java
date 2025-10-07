package com.anla.pelanggan.repository;

import com.anla.pelanggan.model.Pelanggan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Pelanggan entity.
 */
@Repository
public interface PelangganRepository extends JpaRepository<Pelanggan, Long> {
}
