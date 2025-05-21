package com.chi.PhongTro.specification;

import com.chi.PhongTro.entity.Renters;
import com.chi.PhongTro.entity.Users;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class RenterSpecification {
    public static Specification<Renters> filterRenter(String name, String phone, String userId){
        return (root, query, criteriaBuilder) ->{

            var predicates = new ArrayList<Predicate>();

            if (name != null && !name.isEmpty()){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), "%" + name.toLowerCase() + "%"));
            }

            if (phone != null && !phone.isEmpty()){
                predicates.add(criteriaBuilder.like(root.get("phone"), "%" + phone + "%"));
            }

            if (userId != null && !userId.isEmpty()){
                Join<Renters, Users> usersJoin = root.join("user");
                predicates.add(criteriaBuilder.equal(usersJoin.get("user_id"), userId));

            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
