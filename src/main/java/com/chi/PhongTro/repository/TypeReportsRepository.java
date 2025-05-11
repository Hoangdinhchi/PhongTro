package com.chi.PhongTro.repository;

import com.chi.PhongTro.entity.TypeReports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeReportsRepository extends JpaRepository<TypeReports, String> {

}
