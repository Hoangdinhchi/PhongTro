package com.chi.PhongTro.dto.Response;


import com.chi.PhongTro.entity.WithdrawalRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WithdrawalRequestResponse {
    long id;
    long ownerId;
    String ownerName;
    String ownerPhone;
    double amount;
    String bankName;
    String bankAccountNumber;
    String accountHolderName;
    String status;
    LocalDateTime requestTime;
    LocalDateTime processedTime;

    public WithdrawalRequestResponse(WithdrawalRequest withdrawalRequest){
        this.id = withdrawalRequest.getId();
        this.ownerId = withdrawalRequest.getOwner().getUser_id();
        this.ownerName = withdrawalRequest.getOwner().getUsername();
        this.ownerPhone = withdrawalRequest.getOwner().getPhone();
        this.amount = withdrawalRequest.getAmount();
        this.bankName = withdrawalRequest.getBankName();
        this.bankAccountNumber = withdrawalRequest.getBankAccountNumber();
        this.accountHolderName = withdrawalRequest.getAccountHolderName();
        this.status = withdrawalRequest.getStatus();
        this.requestTime = withdrawalRequest.getRequestTime();
        this.processedTime = withdrawalRequest.getProcessedTime();
    }

}
