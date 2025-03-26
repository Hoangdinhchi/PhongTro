package com.chi.PhongTro.repository;

import com.chi.PhongTro.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByPhone(String phone);
    void deleteByPhone(String phone);
}