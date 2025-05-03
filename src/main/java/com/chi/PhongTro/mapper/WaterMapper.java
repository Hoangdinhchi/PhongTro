package com.chi.PhongTro.mapper;

import com.chi.PhongTro.dto.Request.WaterRequest;
import com.chi.PhongTro.dto.Response.WaterResponse;
import com.chi.PhongTro.entity.Water;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WaterMapper {
    WaterResponse toWaterResponse(Water water);

    @Mapping(target = "create_at", ignore = true)
    Water toWater(WaterRequest waterRequest);
}