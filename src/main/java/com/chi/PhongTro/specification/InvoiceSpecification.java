package com.chi.PhongTro.specification;

import com.chi.PhongTro.entity.Invoices;
import com.chi.PhongTro.entity.Renters;
import com.chi.PhongTro.entity.Rooms;
import com.chi.PhongTro.entity.Users;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;

public class InvoiceSpecification {
    public static Specification<Invoices> filterInvoice(String status, String roomName,
                                                        String renterName, String renterPhone,
                                                        LocalDate fromDate, LocalDate toDate,
                                                        String ownerId, String renterPhoneId) {


        return (root, query, criteriaBuilder) -> {
            var predicates  = new ArrayList<Predicate>();

            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (roomName != null && !roomName.isEmpty()) {
                Join<Invoices, Rooms> roomJoin = root.join("room");
                predicates.add(criteriaBuilder.like(roomJoin.get("roomNumber"), "%" + roomName + "%"));
            }

            if (renterName != null && !renterName.isEmpty()) {
                Join<Invoices, Renters> renterJoin = root.join("renter");
                predicates.add(criteriaBuilder.like(renterJoin.get("fullName"), "%" + renterName + "%"));
            }
            if (renterPhone != null && !renterPhone.isEmpty()) {
                Join<Invoices, Renters> renterJoin = root.join("renter");
                predicates.add(criteriaBuilder.like(renterJoin.get("phone"), "%" + renterPhone + "%"));
            }

            if (fromDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), fromDate));
            }
            if (toDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), toDate));
            }

            if (ownerId != null) {
                Join<Invoices, Users> userJoin = root.join("owner");
                predicates.add(criteriaBuilder.equal(userJoin.get("user_id"), ownerId));
            }
            if (renterPhoneId != null) {
                Join<Invoices, Renters> userJoin = root.join("renter");
                predicates.add(criteriaBuilder.equal(userJoin.get("phone"), renterPhoneId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };


    }

}
