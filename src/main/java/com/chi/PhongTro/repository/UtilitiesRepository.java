package com.chi.PhongTro.repository;

import com.chi.PhongTro.entity.Utilities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilitiesRepository extends JpaRepository<Utilities, String> {
}
