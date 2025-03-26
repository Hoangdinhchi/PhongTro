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
    @JoinColumn(name = "reported_user_id", nullable = false)
    Users reportedUser;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    Posts post;

    @Column(nullable = false)
    String reason;

    @Column(nullable = false)
    String status;

    @Column(name = "created_at", nullable = false)
    LocalDate createdAt;
}