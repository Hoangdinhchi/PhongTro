package com.chi.PhongTro.dto.Request;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionsRequest {
    String invoice_id;
    Double amount;
    String transaction_code;
    String status;
    String response_code;
}
