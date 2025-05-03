package com.chi.PhongTro.repository;

import com.chi.PhongTro.entity.Electricity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ElectricityRepository extends JpaRepository<Electricity, Long> {
}
