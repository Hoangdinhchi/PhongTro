package com.chi.PhongTro.controller;


import com.chi.PhongTro.dto.Request.ApiResponse;
import com.chi.PhongTro.dto.Request.InvoiceCreationRequest;
import com.chi.PhongTro.dto.Request.InvoiceDetailCreationRequest;
import com.chi.PhongTro.dto.Response.InvoiceDetailResponse;
import com.chi.PhongTro.dto.Response.InvoiceResponse;
import com.chi.PhongTro.service.InvoiceDetailService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoice_details")
public class InvoiceDetailController {

    InvoiceDetailService invoiceDetailService;

    @PostMapping
    ApiResponse<InvoiceDetailResponse> createInvoiceDetail(@RequestBody @Valid InvoiceDetailCreationRequest request) {
        return ApiResponse.<InvoiceDetailResponse>builder()
                .code(1000)
                .result(invoiceDetailService.createInvoiceDetail(request))
                .build();
    }

    @GetMapping("/{invoiceId}")
    ApiResponse<List<InvoiceDetailResponse>> getInvoiceDetailById(@PathVariable String invoiceId) {
        return ApiResponse.<List<InvoiceDetailResponse>>builder()
                .code(1000)
                .result(invoiceDetailService.getAllInvoiceDetails(invoiceId))
                .build();
    }



}
