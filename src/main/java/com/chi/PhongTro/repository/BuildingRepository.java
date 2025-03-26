package com.chi.PhongTro.repository;

import com.chi.PhongTro.entity.Buildings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingRepository extends JpaRepository<Buildings, String> {
    @Query("SELECT b FROM Buildings b WHERE b.user.user_id = :userId")
    List<Buildings> findAllByUserId(@Param("userId") long userId);
}