package com.chi.PhongTro.mapper;

import com.chi.PhongTro.dto.Response.InvoiceDetailResponse;
import com.chi.PhongTro.entity.InvoiceDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvoiceDetailMapper {

    @Mapping(target = "invoiceId", source = "invoice.invoiceId")
    InvoiceDetailResponse toInvoiceDetailResponse(InvoiceDetails invoiceDetail);
}