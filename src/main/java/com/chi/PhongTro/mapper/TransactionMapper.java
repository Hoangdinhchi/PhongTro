package com.chi.PhongTro.mapper;

import com.chi.PhongTro.dto.Request.TransactionsRequest;
import com.chi.PhongTro.dto.Response.TransactionsResponse;
import com.chi.PhongTro.entity.Transactions;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "user_id", ignore = true)
    @Mapping(target = "invoice_id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Transactions toTransaction(TransactionsRequest request);

    @Mapping(target = "invoiceId", source = "invoice_id.invoiceId")
    @Mapping(target = "userId", source = "user_id.user_id")
    TransactionsResponse toTransactionResponse(Transactions transaction);
}
