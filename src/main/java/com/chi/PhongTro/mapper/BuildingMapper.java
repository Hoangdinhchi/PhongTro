package com.chi.PhongTro.mapper;

import com.chi.PhongTro.dto.Response.BuildingResponse;
import com.chi.PhongTro.entity.Buildings;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BuildingMapper {

    @Mapping(target = "userId", source = "user.user_id")
    @Mapping(target = "userPhone", source = "user.phone")
    @Mapping(target = "street", source = "street")
    @Mapping(target = "district", source = "district")
    @Mapping(target = "city", source = "city")
    BuildingResponse toBuildingResponse(Buildings building);
}