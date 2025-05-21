package com.chi.PhongTro.repository;

import com.chi.PhongTro.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, String>, JpaSpecificationExecutor<Transactions> {

    @Query("SELECT t FROM Transactions t WHERE t.user_id.user_id = :userId")
    List<Transactions> findAllByUserId(String userId);
}
