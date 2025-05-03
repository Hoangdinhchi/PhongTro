package com.chi.PhongTro.repository;

import com.chi.PhongTro.entity.Renters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RenterRepository extends JpaRepository<Renters, String> {


    Optional<Renters> findByPhone(String phone);

    @Query("SELECT r FROM Renters r WHERE r.user.user_id = :userId")
    List<Renters> findByUserId(@Param("userId") long userId);


    @Query("SELECT EXISTS(SELECT 1 FROM Renters r WHERE r.user.user_id = :userId AND r.phone = :phone)")
    boolean existsByUserIdAndPhone(@Param("userId") long userId,@Param("phone") String phone);

    @Query("SELECT EXISTS(SELECT 1 FROM Renters r WHERE r.user.user_id = :userId AND r.email = :email)")
    boolean exitsByUserIdAndEmail(@Param("userId") long userId,@Param("email") String email);
}
