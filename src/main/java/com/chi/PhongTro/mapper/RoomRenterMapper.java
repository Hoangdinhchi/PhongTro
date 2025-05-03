package com.chi.PhongTro.mapper;


import com.chi.PhongTro.dto.Request.RoomRenterCreationRequest;
import com.chi.PhongTro.dto.Request.RoomRenterUpdateRequest;
import com.chi.PhongTro.entity.RoomRenters;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoomRenterMapper {


    @Mapping(target = "room", ignore = true)
    @Mapping(target = "renter", ignore = true)
    RoomRenters toRoomRenter(RoomRenterCreationRequest request);

    @Mapping(target = "room", ignore = true)
    @Mapping(target = "renter", ignore = true)
    void updateRoomRenter(@MappingTarget RoomRenters roomRenters, RoomRenterUpdateRequest request);

}
