package com.chi.PhongTro.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "transactions")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long transaction_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    Users user_id;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    Invoices invoice_id;

    Double amount;

    String transaction_code;

    String status;

    String response_code;

    @Column(name = "created_at")
    LocalDateTime createdAt;
}