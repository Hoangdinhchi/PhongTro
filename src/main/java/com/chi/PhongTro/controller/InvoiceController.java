package com.chi.PhongTro.controller;

import com.chi.PhongTro.dto.Request.ApiResponse;
import com.chi.PhongTro.dto.Request.InvoiceCreationRequest;
import com.chi.PhongTro.dto.Request.InvoiceUpdateStatusRequest;
import com.chi.PhongTro.dto.Response.InvoiceResponse;
import com.chi.PhongTro.entity.Invoices;
import com.chi.PhongTro.service.DocumentGenerationService;
import com.chi.PhongTro.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvoiceController {

    InvoiceService invoiceService;

    DocumentGenerationService documentGenerationService;

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadInvoicePdf(@PathVariable String id) throws Exception {
        Invoices invoice = invoiceService.getInvoiceId(id);
        byte[] pdfBytes = documentGenerationService.generatePdfInvoice(invoice);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "invoice-" + id + ".pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/{id}/word")
    public ResponseEntity<byte[]> downloadInvoiceWord(@PathVariable String id) throws Exception {
        Invoices invoice = invoiceService.getInvoiceId(id);
        byte[] wordBytes = documentGenerationService.generateWordInvoice(invoice);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("filename", "invoice-" + id + ".docx");

        return new ResponseEntity<>(wordBytes, headers, HttpStatus.OK);
    }


    @PostMapping
    ApiResponse<InvoiceResponse> createInvoice(@RequestBody @Valid InvoiceCreationRequest request) {
        return ApiResponse.<InvoiceResponse>builder()
                .code(1000)
                .result(invoiceService.createInvoice(request))
                .build();

    }

    @PatchMapping("/{invoiceId}")
    ApiResponse<InvoiceResponse> updateStatusInvoice(@PathVariable String invoiceId,
                                               @RequestBody @Valid InvoiceUpdateStatusRequest request){
        return ApiResponse.<InvoiceResponse>builder()
                .code(1000)
                .result(invoiceService.updateStatus(invoiceId, request))
                .build();
    }

    @GetMapping("/my-invoices")
    ApiResponse<List<InvoiceResponse>> getAllInvoicesByOwner(){
        return ApiResponse.<List<InvoiceResponse>>builder()
                .code(1000)
                .result(invoiceService.getAllInvoicesByOwner())
                .build();
    }


    @GetMapping("/renter-invoices")
    ApiResponse<List<InvoiceResponse>> getAllInvoicesByRenter(){
        return ApiResponse.<List<InvoiceResponse>>builder()
                .code(1000)
                .result(invoiceService.getAllInvoicesByRenter())
                .build();
    }

    @GetMapping("/{roomId}/{renterPhone}")
    ApiResponse<List<InvoiceResponse>> getAllInvoicesByRoomIdAndRenterPhone(@PathVariable String roomId,
                                                                            @PathVariable String renterPhone){
        return ApiResponse.<List<InvoiceResponse>>builder()
                .code(1000)
                .result(invoiceService.getInvoicesByRoomIdAndRenterPhone(roomId,renterPhone))
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
