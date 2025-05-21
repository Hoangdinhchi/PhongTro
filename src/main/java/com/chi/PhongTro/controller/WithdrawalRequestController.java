package com.chi.PhongTro.controller;


import com.chi.PhongTro.dto.Request.ApiResponse;
import com.chi.PhongTro.dto.Request.WithdrawalRequestCreate;
import com.chi.PhongTro.dto.Request.WithdrawalRequestFilter;
import com.chi.PhongTro.dto.Request.WithdrawalRequestUpdateStatus;
import com.chi.PhongTro.dto.Response.PageResponse;
import com.chi.PhongTro.dto.Response.WithdrawalRequestResponse;
import com.chi.PhongTro.service.WithdrawalRequestService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/withdrawal-requests")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WithdrawalRequestController {
    WithdrawalRequestService withdrawalRequestService;


    @PostMapping("/create")
    public ApiResponse<String> createWithdrawalRequest(@RequestBody WithdrawalRequestCreate request){
        withdrawalRequestService.createWithdrawal(request);
        return ApiResponse.<String>builder()
                .code(1000)
                .result("Success")
                .build();
    }


    @PatchMapping("/update-status/{withdrawalRequestId}")
    public ApiResponse<String> updateStatusWithdrawalRequest(@PathVariable String withdrawalRequestId,
                                                             @RequestBody WithdrawalRequestUpdateStatus request){
        withdrawalRequestService.updateStatus(withdrawalRequestId,request);

        return ApiResponse.<String>builder()
                .code(1000)
                .result("Update status withdrawal request successes")
                .build();

    }


    @GetMapping("/owner/{ownerId}")
    public ApiResponse<List<WithdrawalRequestResponse>> getAllWithdrawalRequests(@PathVariable String ownerId){
        return ApiResponse.<List<WithdrawalRequestResponse>>builder()
                .code(1000)
                .result(withdrawalRequestService.getAllWithdrawalRequestsByOwner(ownerId))
                .build();

    }

    @GetMapping("/admin")
    public ApiResponse<PageResponse<WithdrawalRequestResponse>> getAllWithdrawalRequestsByPage(@ModelAttribute WithdrawalRequestFilter request){
        return ApiResponse.<PageResponse<WithdrawalRequestResponse>>builder()
                .code(1000)
                .result(withdrawalRequestService.getAllWithdrawalRequests(request))
                .build();

    }

}
