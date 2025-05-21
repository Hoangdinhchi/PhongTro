package com.chi.PhongTro.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "withdrawal_request")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WithdrawalRequest {

    @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
            @JoinColumn(name = "owner_id", nullable = false)
    Users owner;

    Double amount;

    @Column(name = "bank_name")
    String bankName;

    @Column(name = "bank_account_number")
    String bankAccountNumber;

    @Column(name = "account_holder_name")
    String accountHolderName;

    String status;

    @Column(name = "request_time", nullable = false)
    LocalDateTime requestTime;

    @Column(name = "processed_time")
    LocalDateTime processedTime;

}
