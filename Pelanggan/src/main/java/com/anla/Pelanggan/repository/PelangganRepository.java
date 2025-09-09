package com.anla.Pelanggan.repository;

import com.anla.Pelanggan.model.Pelanggan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PelangganRepository extends JpaRepository<Pelanggan, Long> {
}
