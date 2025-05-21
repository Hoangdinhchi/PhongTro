package com.chi.PhongTro.service;


import com.chi.PhongTro.dto.Response.OwnerFinancialSummaryResponse;
import com.chi.PhongTro.entity.Invoices;
import com.chi.PhongTro.entity.OwnerRevenue;
import com.chi.PhongTro.entity.Users;
import com.chi.PhongTro.entity.WithdrawalRequest;
import com.chi.PhongTro.exception.AppException;
import com.chi.PhongTro.exception.ErrorCode;
import com.chi.PhongTro.repository.InvoiceRepository;
import com.chi.PhongTro.repository.OwnerRevenueRepository;
import com.chi.PhongTro.repository.UsersRepository;
import com.chi.PhongTro.repository.WithdrawalRequestRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OwnerRevenueService {
    OwnerRevenueRepository ownerRevenueRepository;

    WithdrawalRequestRepository withdrawalRequestRepository;

    InvoiceRepository invoiceRepository;

    UsersRepository usersRepository;


    @Transactional
    public void createOwnerRevenue(Long owner_id, String invoice_id, Double amount){
        Users owner = usersRepository.findById(String.valueOf(owner_id))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Invoices invoice = invoiceRepository.findById(invoice_id)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_FOUND));
        OwnerRevenue ownerRevenue = OwnerRevenue.builder()
                .owner(owner)
                .invoice(invoice)
                .amount(amount)
                .status("available")
                .create_at(LocalDateTime.now())
                .build();
        ownerRevenueRepository.save(ownerRevenue);
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('OWNER')")
    public OwnerFinancialSummaryResponse getOwnerFinancialSummary(String ownerId){
        Users user = usersRepository.findById(ownerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));


        Double totalRevenue = ownerRevenueRepository.findAllByOwner(user).stream()
                .mapToDouble(OwnerRevenue::getAmount)
                .sum();
        Double totalWithdrawn = withdrawalRequestRepository.findAllByOwnerAndStatus(user, "completed").stream()
                .mapToDouble(WithdrawalRequest::getAmount)
                .sum();
        Double pendingWithdrawals = withdrawalRequestRepository.findAllByOwnerAndStatus(user, "pending").stream()
                .mapToDouble(WithdrawalRequest::getAmount)
                .sum();
        Double availableBalance = totalRevenue - totalWithdrawn;

        return OwnerFinancialSummaryResponse.builder()
                .availableBalance(availableBalance)
                .totalRevenue(totalRevenue)
                .totalWithdrawn(totalWithdrawn)
                .pendingWithdrawals(pendingWithdrawals)
                .build();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void updateRevenueStatusForWithdrawal(String ownerId, Double amount){
        Users user = usersRepository.findById(ownerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        List<OwnerRevenue> availableOwnerRevenue = ownerRevenueRepository.findAllByOwnerAndStatus(user, "available")
                .stream()
                .sorted(Comparator.comparing(OwnerRevenue::getCreate_at))
                .toList();

        Double remainingAmount = amount;
        for (OwnerRevenue ownerRevenue : availableOwnerRevenue) {
            if (remainingAmount <= 0) break;
            if (remainingAmount >= ownerRevenue.getAmount()){
                ownerRevenue.setStatus("paid");
                remainingAmount -= ownerRevenue.getAmount();
            }else{

               Double usedAmount = remainingAmount;
               Double remainingRevenueAmount = ownerRevenue.getAmount() - usedAmount;
               ownerRevenue.setAmount(usedAmount);
               ownerRevenue.setStatus("paid");

               OwnerRevenue newRevenue = OwnerRevenue.builder()
                       .owner(user)
                       .invoice(ownerRevenue.getInvoice())
                       .amount(remainingRevenueAmount)
                       .status("available")
                       .create_at(LocalDateTime.now())
                       .build();
                ownerRevenueRepository.save(newRevenue);

               remainingAmount = 0.0;

            }
            ownerRevenueRepository.save(ownerRevenue);
        }

        if (remainingAmount > 0){
            throw new AppException(ErrorCode.AMOUNT_IS_VALID);
        }

    }





}
