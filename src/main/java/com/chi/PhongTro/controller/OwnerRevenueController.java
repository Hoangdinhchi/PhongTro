package com.chi.PhongTro.controller;


import com.chi.PhongTro.dto.Request.ApiResponse;
import com.chi.PhongTro.dto.Response.OwnerFinancialSummaryResponse;
import com.chi.PhongTro.service.OwnerRevenueService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/owner-revenue")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OwnerRevenueController {
    OwnerRevenueService ownerRevenueService;


    @GetMapping("/financial-summary/{ownerId}")
    public ApiResponse<OwnerFinancialSummaryResponse> getOwnerFinancialSummary(@PathVariable String ownerId){
        return ApiResponse.<OwnerFinancialSummaryResponse>builder()
                .code(1000)
                .result(ownerRevenueService.getOwnerFinancialSummary(ownerId))
                .build();
    }



}
