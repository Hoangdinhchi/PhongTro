package com.chi.PhongTro.repository;

import com.chi.PhongTro.entity.Invoices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoices, String> {

    @Query("SELECT i FROM Invoices i WHERE i.owner.phone = :phone")
    List<Invoices> findAllByOwnerPhone(@Param("phone") String phone);
}
