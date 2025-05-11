package com.chi.PhongTro.repository;


import com.chi.PhongTro.entity.RoomRenters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRenterRepository extends JpaRepository<RoomRenters, String> {


    @Query("SELECT r FROM RoomRenters r WHERE r.room.roomId = :roomId")
    List<RoomRenters> findAllByRoomId(String roomId);

    @Query("SELECT r FROM RoomRenters r WHERE r.renter.renterId = :renterId")
    List<RoomRenters> findAllByRenterId(String renterId);

    @Query("SELECT r FROM RoomRenters r WHERE r.renter.phone = :phone")
    List<RoomRenters> findAllByRenterPhone(String phone);

}
