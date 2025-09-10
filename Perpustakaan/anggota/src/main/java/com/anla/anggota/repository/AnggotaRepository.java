package com.anla.anggota.repository;

import com.anla.anggota.model.Anggota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnggotaRepository extends JpaRepository<Anggota, Long> {
}
