package com.chi.PhongTro.mapper;

import com.chi.PhongTro.dto.Response.RoomTypeResponse;
import com.chi.PhongTro.entity.RoomTypes;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomTypeMapper {

    RoomTypeResponse toRoomTypeResponse(RoomTypes roomTypes);
}