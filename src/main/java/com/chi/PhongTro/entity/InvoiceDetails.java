package com.chi.PhongTro.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "invoice_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    Long detailId;

    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    Invoices invoice;

    @Column(nullable = false)
    String description;

    @Column(nullable = false)
    Double amount;
}