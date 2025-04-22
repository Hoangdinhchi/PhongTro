package com.chi.PhongTro.mapper;

import com.chi.PhongTro.dto.Response.UtilitiesResponse;
import com.chi.PhongTro.entity.Utilities;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UtilitiesMapper {

    UtilitiesResponse toUtilitiesResponse(Utilities utilities);
}