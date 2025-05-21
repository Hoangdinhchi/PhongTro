package com.chi.PhongTro.dto.Response;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OwnerFinancialSummaryResponse {
    Double totalRevenue;
    Double totalWithdrawn;
    Double pendingWithdrawals;
    Double availableBalance;
}
