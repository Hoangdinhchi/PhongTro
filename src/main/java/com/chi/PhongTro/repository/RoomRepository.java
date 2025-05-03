package com.chi.PhongTro.repository;

import com.chi.PhongTro.entity.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Rooms, String> {

    @Query("SELECT r FROM Rooms r WHERE r.building.buildingId = :buildingId")
    List<Rooms> finAllByBuildingId(@Param("buildingId") String buildingId);

    @Query("SELECT r FROM Rooms r WHERE r.building.user.phone = :phone")
    List<Rooms> findByUser(@Param("phone") String phone);
}