package com.chi.PhongTro.mapper;

import com.chi.PhongTro.dto.Response.RoomResponse;
import com.chi.PhongTro.entity.Rooms;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(target = "buildingId", source = "building.buildingId")
    @Mapping(target = "buildingName", source = "building.name")
    RoomResponse toRoomResponse(Rooms room);
}