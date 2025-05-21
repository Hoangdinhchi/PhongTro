package com.chi.PhongTro.repository;

import com.chi.PhongTro.entity.OwnerRevenue;
import com.chi.PhongTro.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OwnerRevenueRepository extends JpaRepository<OwnerRevenue, String> {

    List<OwnerRevenue> findAllByOwnerAndStatus(Users user, String status);

    List<OwnerRevenue> findAllByOwner(Users user);
}
