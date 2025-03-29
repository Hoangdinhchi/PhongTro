package com.chi.PhongTro.mapper;

import com.chi.PhongTro.dto.Response.InvoiceResponse;
import com.chi.PhongTro.entity.Invoices;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {InvoiceDetailMapper.class})
public interface InvoiceMapper {

    @Mapping(target = "ownerName", source = "owner.username")
    @Mapping(target = "ownerPhone", source = "owner.phone")
    @Mapping(target = "roomId", source = "room.roomId")
    @Mapping(target = "roomNumber", source = "room.roomNumber")
    @Mapping(target = "renterId", source = "renter.renterId")
    @Mapping(target = "renterFullName", source = "renter.fullName")
    InvoiceResponse toInvoiceResponse(Invoices invoice);
}