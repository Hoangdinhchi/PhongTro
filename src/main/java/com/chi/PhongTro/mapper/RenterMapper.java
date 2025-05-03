package com.chi.PhongTro.mapper;

import com.chi.PhongTro.dto.Response.RenterResponse;
import com.chi.PhongTro.entity.Renters;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RenterMapper {

    @Mapping(target = "userId", source = "user.user_id")
    @Mapping(target = "roomNames", ignore = true)
    RenterResponse toRenterResponse(Renters renter);
}