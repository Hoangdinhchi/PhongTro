package com.chi.PhongTro.dto.Request;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionFilterRequest {
    String status;
    LocalDate fromDate;
    LocalDate toDate;

    int page = 0;
    int size = 10;
}
