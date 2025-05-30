package com.chi.PhongTro.repository;

import com.chi.PhongTro.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, String>, JpaSpecificationExecutor<Users> {

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    Optional<Users> findByPhone(String phone);

}
