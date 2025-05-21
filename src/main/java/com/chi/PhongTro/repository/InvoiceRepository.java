package com.chi.PhongTro.repository;

import com.chi.PhongTro.entity.Invoices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoices, String>, JpaSpecificationExecutor<Invoices> {

    @Query("SELECT i FROM Invoices i WHERE i.owner.phone = :phone")
    List<Invoices> findAllByOwnerPhone(@Param("phone") String phone);

    @Query("SELECT i FROM Invoices i WHERE i.renter.phone = :phone")
    List<Invoices> findAllByRenterPhone(String phone);

    @Query("SELECT i FROM Invoices i WHERE i.room.roomId = :roomId")
    List<Invoices> findAllByRoomId(String roomId);

    @Query("SELECT i FROM Invoices i WHERE i.room.roomId = :roomId AND i.renter.phone = :renterPhone")
    List<Invoices> findAllByRoomIdAndRenterPhone(String roomId, String renterPhone);
}
