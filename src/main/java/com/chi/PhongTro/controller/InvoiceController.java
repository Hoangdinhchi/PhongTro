package com.chi.PhongTro.controller;

import com.chi.PhongTro.dto.Request.ApiResponse;
import com.chi.PhongTro.dto.Request.InvoiceCreationRequest;
import com.chi.PhongTro.dto.Response.InvoiceResponse;
import com.chi.PhongTro.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvoiceController {

    InvoiceService invoiceService;

    @PostMapping
    ApiResponse<InvoiceResponse> createInvoice(@RequestBody @Valid InvoiceCreationRequest request) {
        return ApiResponse.<InvoiceResponse>builder()
                .code(1000)
                .result(invoiceService.createInvoice(request))
                .build();

    }

    @GetMapping("/my-invoices")
    ApiResponse<List<InvoiceResponse>> getAllInvoicesByOwner(){
        return ApiResponse.<List<InvoiceResponse>>builder()
                .code(1000)
                .result(invoiceService.getAllInvoicesByOwner())
                .build();
    }

    @GetMapping("/{invoiceId}")
    ApiResponse<InvoiceResponse> getInvoiceById(@PathVariable String invoiceId){
        return ApiResponse.<InvoiceResponse>builder()
                .code(1000)
                .result(invoiceService.getInvoiceById(invoiceId))
                .build();
    }

    
}
