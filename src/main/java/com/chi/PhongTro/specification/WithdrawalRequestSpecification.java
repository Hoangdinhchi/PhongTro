package com.chi.PhongTro.specification;

import com.chi.PhongTro.entity.Users;
import com.chi.PhongTro.entity.WithdrawalRequest;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;


public class WithdrawalRequestSpecification {
    public static Specification<WithdrawalRequest> filterWithdrawalRequest(String status, String phone, String ownerName) {
        return (root, query, criteriaBuilder) -> {

            var predicate = new ArrayList<Predicate>();

            if (status != null && !status.isEmpty()) {
                predicate.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (phone != null && !phone.isEmpty()) {
                Join<WithdrawalRequest, Users> usersJoin = root.join("owner");

                predicate.add(criteriaBuilder.equal(usersJoin.get("phone"), phone));
            }

            if (ownerName != null && !ownerName.isEmpty()) {

                Join<WithdrawalRequest, Users> usersJoin = root.join("owner");
                predicate.add(criteriaBuilder.equal(usersJoin.get("username"), ownerName));
            }

            return criteriaBuilder.and(predicate.toArray(new Predicate[0]));
        };
    }

}
