package com.chi.PhongTro.repository;

import com.chi.PhongTro.entity.Reports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Reports, String> {

    @Query("SELECT r FROM Reports r WHERE r.reporter.phone = :phone")
    List<Reports> findAllByReporterPhone(String phone);

    @Query("SELECT r FROM Reports r WHERE r.toUser.phone = :phone")
    List<Reports> findAllByToUserPhone(String phone);
}
