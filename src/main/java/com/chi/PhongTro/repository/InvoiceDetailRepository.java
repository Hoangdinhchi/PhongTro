package com.chi.PhongTro.repository;

import com.chi.PhongTro.entity.InvoiceDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetails, String> {

    @Query("SELECT i FROM InvoiceDetails i WHERE i.invoice.invoiceId = :invoiceId")
    List<InvoiceDetails> findAllByInvoiceId(@Param("invoiceId") String invoiceId);
}