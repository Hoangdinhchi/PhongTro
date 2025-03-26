package com.chi.PhongTro.repository;

import com.chi.PhongTro.entity.Reports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Reports, String> {

}
