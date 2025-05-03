package com.chi.PhongTro.mapper;

import com.chi.PhongTro.dto.Request.ElectricityRequest;
import com.chi.PhongTro.dto.Response.ElectricityResponse;
import com.chi.PhongTro.entity.Electricity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ElectricityMapper {


    ElectricityResponse toElectricityResponse(Electricity electricity);

    @Mapping(target = "create_at", ignore = true)
    Electricity toElectricity(ElectricityRequest electricityRequest);
}