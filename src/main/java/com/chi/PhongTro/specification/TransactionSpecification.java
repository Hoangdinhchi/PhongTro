package com.chi.PhongTro.specification;

import com.chi.PhongTro.entity.Transactions;
import com.chi.PhongTro.entity.Users;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;

public class TransactionSpecification {
    public static Specification<Transactions> filterTransaction(String status, LocalDate fromDate, LocalDate toDate, String userId) {
        return (root, query, criteriaBuilder) -> {
            var predicates = new ArrayList<Predicate>();

            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (fromDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), fromDate));
            }
            if (toDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), toDate));
            }

            if (userId != null) {
                Join<Transactions, Users> userJoin = root.join("user_id");


                predicates.add(criteriaBuilder.equal(userJoin.get("user_id"), userId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }
}
