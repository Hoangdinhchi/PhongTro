package com.chi.PhongTro.dto.Response;

import com.chi.PhongTro.entity.Invoices;
import com.chi.PhongTro.entity.Users;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionsResponse {
    Long transaction_id;

    Long userId;

    Long invoiceId;

    Double amount;

    String transaction_code;

    String status;

    String response_code;

    LocalDateTime createdAt;
}
