package com.chi.PhongTro.specification;

import com.chi.PhongTro.entity.Users;
import com.chi.PhongTro.util.Role;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<Users> filterUser(String username, String email, String phone, Role role) {
        return (root, query, cb) -> {
            var predicates = new java.util.ArrayList<Predicate>();

            if (username != null && !username.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%"));
            }

            if (email != null && !email.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }

            if (phone != null && !phone.isEmpty()) {
                predicates.add(cb.like(root.get("phone"), "%" + phone + "%"));
            }

            if (role != null) {
                predicates.add(cb.equal(root.get("role"), role));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}