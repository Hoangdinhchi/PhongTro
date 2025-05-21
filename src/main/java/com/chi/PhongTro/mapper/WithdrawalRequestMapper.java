package com.chi.PhongTro.mapper;


import com.chi.PhongTro.dto.Request.WithdrawalRequestCreate;
import com.chi.PhongTro.entity.WithdrawalRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WithdrawalRequestMapper  {



    @Mapping(target = "requestTime", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "owner", ignore = true)
    WithdrawalRequest toWithdrawalRequest(WithdrawalRequestCreate request);


}
