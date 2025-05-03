package com.chi.PhongTro.repository;

import com.chi.PhongTro.entity.Water;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WaterRepository extends JpaRepository<Water, Long> {
}
