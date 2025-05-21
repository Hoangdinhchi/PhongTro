package com.chi.PhongTro.service;

import com.chi.PhongTro.dto.Request.TransactionFilterRequest;
import com.chi.PhongTro.dto.Request.TransactionsRequest;
import com.chi.PhongTro.dto.Response.PageResponse;
import com.chi.PhongTro.dto.Response.TransactionsResponse;
import com.chi.PhongTro.entity.Invoices;
import com.chi.PhongTro.entity.OwnerRevenue;
import com.chi.PhongTro.entity.Transactions;
import com.chi.PhongTro.entity.Users;
import com.chi.PhongTro.exception.AppException;
import com.chi.PhongTro.exception.ErrorCode;
import com.chi.PhongTro.mapper.TransactionMapper;
import com.chi.PhongTro.repository.InvoiceRepository;
import com.chi.PhongTro.repository.OwnerRevenueRepository;
import com.chi.PhongTro.repository.TransactionRepository;
import com.chi.PhongTro.repository.UsersRepository;
import com.chi.PhongTro.specification.TransactionSpecification;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionService {
    TransactionRepository transactionRepository;

    UsersRepository usersRepository;

    InvoiceRepository invoiceRepository;

    TransactionMapper transactionMapper;

    OwnerRevenueRepository ownerRevenueRepository;

    OwnerRevenueService ownerRevenueService;

    @Transactional
    public void createTransaction(TransactionsRequest request){

        var context = SecurityContextHolder.getContext();
        var authentication = context.getAuthentication();

        Users user = usersRepository.findByPhone(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Invoices invoices = invoiceRepository.findById(request.getInvoice_id())
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_FOUND));

        if (!List.of("pending","failed","completed").contains(request.getStatus())) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        if (request.getStatus().equals("completed")){
            ownerRevenueService.createOwnerRevenue(invoices.getOwner().getUser_id(), request.getInvoice_id(), request.getAmount());
        }

        Transactions transaction = transactionMapper.toTransaction(request);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUser_id(user);
        transaction.setInvoice_id(invoices);

        transactionRepository.save(transaction);
    }

    public List<TransactionsResponse> getAllTransactions(){
        var context = SecurityContextHolder.getContext();
        var authentication = context.getAuthentication();
        String phone = authentication.getName();
        Users user = usersRepository.findByPhone(phone)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return transactionRepository.findAllByUserId(String.valueOf(user.getUser_id()))
                .stream()
                .map(transactionMapper::toTransactionResponse)
                .collect(Collectors.toList());
    }

    public PageResponse<TransactionsResponse> getAllTransactions(TransactionFilterRequest request){
        var context = SecurityContextHolder.getContext();
        Users user = usersRepository.findByPhone(context.getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Specification<Transactions> specification = TransactionSpecification.filterTransaction(
                request.getStatus(),
                request.getFromDate(),
                request.getToDate(),
                String.valueOf(user.getUser_id())
        );

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by("createdAt").descending()
        );

        Page<Transactions> transactionsPage = transactionRepository.findAll(specification, pageable);
        Page<TransactionsResponse> responsePage = transactionsPage.map(transactionMapper::toTransactionResponse);
        return new PageResponse<>(responsePage);
    }

}

