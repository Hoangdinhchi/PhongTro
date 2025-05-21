package com.chi.PhongTro.dto.Request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WithdrawalRequestCreate {

    @NotNull(message = "OWNER_NULL")
    String owner;

    @NotNull(message = "AMOUNT_NULL")
            @NotEmpty(message = "AMOUNT_EMPTY")
    Double amount;

    @NotNull(message = "BANK_NAME_NULL")
            @NotEmpty(message = "BANK_NAME_EMPTY")
    String bankName;

    @NotNull(message = "BANK_ACCOUNT_NUMBER_NULL")
            @NotEmpty(message = "BANK_ACCOUNT_NUMBER_EMPTY")
    String bankAccountNumber;

    @NotNull(message = "ACCOUNT_HOLDER_NAME_NULL")
            @NotEmpty(message = "ACCOUNT_HOLDER_NAME_EMPTY")
    String accountHolderName;
}
