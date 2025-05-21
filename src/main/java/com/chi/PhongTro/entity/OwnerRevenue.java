package com.chi.PhongTro.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "owner_revenue")
public class OwnerRevenue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    Users owner;

    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    Invoices invoice;

    Double amount;

    String status;

    LocalDateTime create_at;
}
