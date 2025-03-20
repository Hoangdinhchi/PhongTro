package com.chi.PhongTro.specification;

import com.chi.PhongTro.entity.Posts;
import com.chi.PhongTro.entity.RoomTypes;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;


public class PostSpecification {
    public static Specification<Posts> filterPost(String typeId, String city, String district, String address,
                                           Double minPrice, Double maxPrice, Double minArea, Double maxArea) {

        return (root, query, cb) -> {
            var predicates = new java.util.ArrayList<Predicate>();


            if (typeId != null && !typeId.isEmpty()) {
                Join<Posts, RoomTypes> typeJoin = root.join("type");
                predicates.add(cb.equal(typeJoin.get("typeId"), typeId));
            }


            if (city != null && !city.isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("city")), city.toLowerCase()));
            }


            if (district != null && !district.isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("district")), district.toLowerCase()));
            }


            if (address != null && !address.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("address")), "%" + address.toLowerCase() + "%"));
            }


            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }


            if (minArea != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("area"), minArea));
            }
            if (maxArea != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("area"), maxArea));
            }


            predicates.add(cb.equal(root.get("status"), "display"));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
