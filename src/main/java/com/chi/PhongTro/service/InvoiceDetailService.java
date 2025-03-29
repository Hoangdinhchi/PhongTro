package com.chi.PhongTro.service;

import com.chi.PhongTro.dto.Request.InvoiceDetailCreationRequest;
import com.chi.PhongTro.dto.Request.InvoiceDetailUpdateRequest;
import com.chi.PhongTro.dto.Response.InvoiceDetailResponse;
import com.chi.PhongTro.entity.InvoiceDetails;
import com.chi.PhongTro.entity.Invoices;
import com.chi.PhongTro.exception.AppException;
import com.chi.PhongTro.exception.ErrorCode;
import com.chi.PhongTro.mapper.InvoiceDetailMapper;
import com.chi.PhongTro.repository.InvoiceDetailRepository;
import com.chi.PhongTro.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceDetailService {

    @Autowired
    private InvoiceDetailRepository invoiceDetailRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceDetailMapper invoiceDetailMapper;

    @Autowired
    private InvoiceService invoiceService;

    // Tạo chi tiết hóa đơn (Admin và Owner)
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    public InvoiceDetailResponse createInvoiceDetail(InvoiceDetailCreationRequest request) {
        Invoices invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_FOUND));

        // Kiểm tra quyền: Chỉ Admin hoặc người tạo hóa đơn được phép
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!invoiceService.checkInvoicePermission(request.getInvoiceId(), authentication)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        InvoiceDetails detail = InvoiceDetails.builder()
                .invoice(invoice)
                .description(request.getDescription())
                .amount(request.getAmount())
                .build();

        detail = invoiceDetailRepository.save(detail);

        // Cập nhật totalAmount của hóa đơn
        updateInvoiceTotalAmount(invoice);

        return invoiceDetailMapper.toInvoiceDetailResponse(detail);
    }


    public List<InvoiceDetailResponse> getAllInvoiceDetails(String invoiceId) {
        List<InvoiceDetails> details = invoiceDetailRepository.findAllByInvoiceId(invoiceId);
        return details.stream()
                .map(invoiceDetailMapper::toInvoiceDetailResponse)
                .collect(Collectors.toList());
    }


    public InvoiceDetailResponse getInvoiceDetailById(String detailId) {
        InvoiceDetails detail = invoiceDetailRepository.findById(detailId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_DETAIL_NOT_FOUND));
        return invoiceDetailMapper.toInvoiceDetailResponse(detail);
    }


    @Transactional
    @PreAuthorize("@invoiceDetailService.checkInvoiceDetailPermission(#detailId, authentication)")
    public InvoiceDetailResponse updateInvoiceDetail(String detailId, InvoiceDetailUpdateRequest request) {
        InvoiceDetails detail = invoiceDetailRepository.findById(detailId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_DETAIL_NOT_FOUND));

        if (request.getDescription() != null && !request.getDescription().isEmpty()) {
            detail.setDescription(request.getDescription());
        }

        if (request.getAmount() != null) {
            detail.setAmount(request.getAmount());
        }

        detail = invoiceDetailRepository.save(detail);


        updateInvoiceTotalAmount(detail.getInvoice());

        return invoiceDetailMapper.toInvoiceDetailResponse(detail);
    }

    // Xóa chi tiết hóa đơn (Admin hoặc người tạo)
    @Transactional
    @PreAuthorize("@invoiceDetailService.checkInvoiceDetailPermission(#detailId, authentication)")
    public void deleteInvoiceDetail(String detailId) {
        InvoiceDetails detail = invoiceDetailRepository.findById(detailId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_DETAIL_NOT_FOUND));

        Invoices invoice = detail.getInvoice();
        invoiceDetailRepository.delete(detail);


        updateInvoiceTotalAmount(invoice);
    }

    // Cập nhật totalAmount của hóa đơn dựa trên các chi tiết
    private void updateInvoiceTotalAmount(Invoices invoice) {
        double totalAmount = invoice.getDetails().stream()
                .mapToDouble(InvoiceDetails::getAmount)
                .sum();
        invoice.setTotalAmount(totalAmount);
        invoiceRepository.save(invoice);
    }


    public boolean checkInvoiceDetailPermission(String detailId, Authentication authentication) {
        InvoiceDetails detail = invoiceDetailRepository.findById(detailId)
                .orElse(null);
        if (detail == null) return false;

        return invoiceService.checkInvoicePermission(String.valueOf(detail.getInvoice().getInvoiceId()), authentication);
    }
}