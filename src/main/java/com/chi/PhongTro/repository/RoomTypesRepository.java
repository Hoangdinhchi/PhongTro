package com.chi.PhongTro.repository;

import com.chi.PhongTro.entity.RoomTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypesRepository extends JpaRepository<RoomTypes, String> {
}
