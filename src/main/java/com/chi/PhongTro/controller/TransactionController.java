package com.chi.PhongTro.controller;

import com.chi.PhongTro.dto.Request.ApiResponse;
import com.chi.PhongTro.dto.Request.TransactionFilterRequest;
import com.chi.PhongTro.dto.Request.TransactionsRequest;
import com.chi.PhongTro.dto.Response.PageResponse;
import com.chi.PhongTro.dto.Response.TransactionsResponse;
import com.chi.PhongTro.service.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionController {
    TransactionService transactionService;


    @PostMapping("/create")
    ApiResponse<String> createTransaction(@RequestBody TransactionsRequest request){
        transactionService.createTransaction(request);
        return ApiResponse.<String>builder()
                .code(1000)
                .result("Success")
                .build();
    }

    @GetMapping("/my-transactions")
    ApiResponse<PageResponse<TransactionsResponse>> getTransactionsByUserId(@ModelAttribute TransactionFilterRequest request){
        return ApiResponse.<PageResponse<TransactionsResponse>>builder()
                .code(1000)
                .result(transactionService.getAllTransactions(request))
                .build();
    }

}
