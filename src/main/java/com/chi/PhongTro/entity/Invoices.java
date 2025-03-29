package com.chi.PhongTro.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Invoices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    Long invoiceId;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    Users owner;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    Rooms room;

    @ManyToOne
    @JoinColumn(name = "renter_id", nullable = false)
    Renters renter;

    @Column(name = "total_amount", nullable = false)
    Double totalAmount;

    @Column(nullable = false)
    String status;

    @Column(name = "due_date", nullable = false)
    LocalDate dueDate;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    List<InvoiceDetails> details = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    LocalDate createdAt;
}