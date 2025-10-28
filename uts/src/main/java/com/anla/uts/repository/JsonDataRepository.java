package com.anla.uts.repository;

import com.anla.uts.model.JsonData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JsonDataRepository extends JpaRepository<JsonData, Long> {
}
