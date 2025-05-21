package com.chi.PhongTro.repository;

import com.chi.PhongTro.entity.Users;
import com.chi.PhongTro.entity.WithdrawalRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WithdrawalRequestRepository extends JpaRepository<WithdrawalRequest, String>, JpaSpecificationExecutor<WithdrawalRequest> {

        List<WithdrawalRequest> findAllByOwnerAndStatus(Users user, String status);

        List<WithdrawalRequest> findAllByOwner(Users user);
}
