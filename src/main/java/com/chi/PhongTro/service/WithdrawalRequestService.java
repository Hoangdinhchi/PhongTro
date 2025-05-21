package com.chi.PhongTro.service;


import com.chi.PhongTro.dto.Request.WithdrawalRequestCreate;
import com.chi.PhongTro.dto.Request.WithdrawalRequestFilter;
import com.chi.PhongTro.dto.Request.WithdrawalRequestUpdateStatus;
import com.chi.PhongTro.dto.Response.OwnerFinancialSummaryResponse;
import com.chi.PhongTro.dto.Response.PageResponse;
import com.chi.PhongTro.dto.Response.WithdrawalRequestResponse;
import com.chi.PhongTro.entity.Users;
import com.chi.PhongTro.entity.WithdrawalRequest;
import com.chi.PhongTro.exception.AppException;
import com.chi.PhongTro.exception.ErrorCode;
import com.chi.PhongTro.mapper.WithdrawalRequestMapper;
import com.chi.PhongTro.repository.UsersRepository;
import com.chi.PhongTro.repository.WithdrawalRequestRepository;
import com.chi.PhongTro.specification.WithdrawalRequestSpecification;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WithdrawalRequestService {

    WithdrawalRequestRepository withdrawalRequestRepository;
    WithdrawalRequestMapper withdrawalRequestMapper;

    OwnerRevenueService ownerRevenueService;

    UsersRepository usersRepository;


    @Transactional
    @PreAuthorize("hasRole('OWNER') || hasRole('ADMIN')")
    public void createWithdrawal(WithdrawalRequestCreate request){
        var context = SecurityContextHolder.getContext();
        var authentication = context.getAuthentication();


        String username = authentication.getName();
        log.info("username: {}", username);
        Users user = usersRepository.findByPhone(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        OwnerFinancialSummaryResponse ownerFinancialSummaryResponse =
                ownerRevenueService.getOwnerFinancialSummary(String.valueOf(user.getUser_id()));

        log.info("ownerFinancialSummaryResponse: {}", ownerFinancialSummaryResponse.getAvailableBalance());
        log.info("ownerFinancialSummaryResponse: {}", ownerFinancialSummaryResponse.getPendingWithdrawals());
        Double availableBalance = ownerFinancialSummaryResponse.getAvailableBalance() - ownerFinancialSummaryResponse.getPendingWithdrawals();

        log.info("availableBalance: {}", availableBalance);

        if(availableBalance < request.getAmount()){
            throw new AppException(ErrorCode.AMOUNT_IS_VALID);
        }
        WithdrawalRequest withdrawalRequest = withdrawalRequestMapper.toWithdrawalRequest(request);
        withdrawalRequest.setRequestTime(LocalDateTime.now());
        withdrawalRequest.setOwner(user);
        withdrawalRequest.setStatus("pending");
        withdrawalRequestRepository.save(withdrawalRequest);
        log.info("create withdrawal request success");
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void updateStatus(String withdrawalRequestId, WithdrawalRequestUpdateStatus request){

        if (!List.of("pending","approved","completed", "rejected").contains(request.getStatus())) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        WithdrawalRequest withdrawalRequest = withdrawalRequestRepository.findById(withdrawalRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));

        if (withdrawalRequest.getStatus().equals(request.getStatus()) || !withdrawalRequest.getStatus().equals("pending")){
            log.info("withdrawal request status is already {}", withdrawalRequest.getStatus());
            return;
        }

        if (request.getStatus().equals("completed")) {
            String ownerId = String.valueOf(withdrawalRequest.getOwner().getUser_id());
            ownerRevenueService.updateRevenueStatusForWithdrawal(ownerId, withdrawalRequest.getAmount());
            withdrawalRequest.setProcessedTime(LocalDateTime.now());

        }else{
            withdrawalRequest.setProcessedTime(LocalDateTime.now());
        }

        withdrawalRequest.setStatus(request.getStatus());
        withdrawalRequestRepository.save(withdrawalRequest);
        log.info("update withdrawal request status success");
    }


    @PreAuthorize("hasRole('ADMIN') || hasRole('OWNER')")
    public List<WithdrawalRequestResponse> getAllWithdrawalRequestsByOwner(String ownerId){
        Users user = usersRepository.findById(ownerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<WithdrawalRequest> withdrawalRequest = withdrawalRequestRepository.findAllByOwner(user);

        return withdrawalRequest.stream()
                .map(w -> WithdrawalRequestResponse.builder()
                        .id(w.getId())
                        .amount(w.getAmount())
                        .requestTime(w.getRequestTime())
                        .processedTime(w.getProcessedTime())
                        .ownerId(w.getOwner().getUser_id())
                        .ownerPhone(w.getOwner().getPhone())
                        .bankName(w.getBankName())
                        .accountHolderName(w.getAccountHolderName())
                        .bankAccountNumber(w.getBankAccountNumber())
                        .ownerName(w.getOwner().getUsername())
                        .status(w.getStatus())
                        .build())
                .toList();
    }



    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<WithdrawalRequestResponse> getAllWithdrawalRequests(WithdrawalRequestFilter request){
        Specification<WithdrawalRequest> specification = WithdrawalRequestSpecification.filterWithdrawalRequest(
                request.getStatus(),
                request.getPhone(),
                request.getOwnerName()
        );

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by("requestTime").descending()
        );

        Page<WithdrawalRequest> withdrawalRequestPage = withdrawalRequestRepository.findAll(specification, pageable);
        Page<WithdrawalRequestResponse> responsePage = withdrawalRequestPage.map(WithdrawalRequestResponse::new);
        return new PageResponse<>(responsePage);
    }

}
