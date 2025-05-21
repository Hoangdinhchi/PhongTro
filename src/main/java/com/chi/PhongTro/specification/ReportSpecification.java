package com.chi.PhongTro.specification;

import com.chi.PhongTro.entity.Reports;
import com.chi.PhongTro.entity.TypeReports;
import com.chi.PhongTro.entity.Users;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;


public class ReportSpecification {
    public static Specification<Reports> filterReport(String typeId, String status,
                                                      String phoneReporter, String reason,
                                                      String toUserId, String reporter) {

        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();


            if (typeId != null && !typeId.isEmpty()) {
                Join<Reports, TypeReports> typeReportsJoin = root.join("typeReport");
                predicates.add(cb.equal(typeReportsJoin.get("id"), typeId));
            }

            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("status")), status.toLowerCase()));
            }

            if (phoneReporter != null && !phoneReporter.isEmpty()) {
                Join<Reports, Users> usersJoin = root.join("reporter");
                predicates.add(cb.equal(usersJoin.get("phone"), phoneReporter));
            }

            if (reason != null && !reason.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("reason")), "%" + reason.toLowerCase() + "%"));
            }

            if (toUserId != null) {
                Join<Reports, Users> toUserJoin = root.join("toUser");
                predicates.add(cb.equal(toUserJoin.get("user_id"), toUserId));
            }

            if( reporter != null && !reporter.isEmpty() ){
                Join<Reports, Users> reporterJoin = root.join("reporter");
                predicates.add(cb.equal(reporterJoin.get("user_id"), reporter));
            }


            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
