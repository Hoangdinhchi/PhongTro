package com.chi.PhongTro.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reports {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    Long reportId;

    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    Users reporter;

    @ManyToOne
    @JoinColumn(name = "to_user_id", nullable = false)
    Users toUser;

    @Column(name = "reported_id", nullable = false)
    Long reportedId;

    @Column(nullable = false)
    String reason;

    @Column(name = "content")
    String content;

    @ManyToOne
    @JoinColumn(name = "type_report_id", nullable = false)
    TypeReports typeReport;


    @Column(nullable = false)
    String status;

    @Column(name = "created_at", nullable = false)
    LocalDate createdAt;
}