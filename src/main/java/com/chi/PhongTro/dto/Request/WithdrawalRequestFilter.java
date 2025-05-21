package com.chi.PhongTro.dto.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WithdrawalRequestFilter {
    String status;
    String phone;
    String ownerName;
    LocalDate requestDate;
    int page = 0;
    int size = 10;
}
