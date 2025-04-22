package com.chi.PhongTro.service;


import com.chi.PhongTro.dto.Response.RoomTypeResponse;
import com.chi.PhongTro.entity.RoomTypes;
import com.chi.PhongTro.mapper.RoomTypeMapper;
import com.chi.PhongTro.repository.RoomTypesRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoomTypeService {

    RoomTypesRepository roomTypesRepository;

    RoomTypeMapper roomTypeMapper;

    public List<RoomTypeResponse> getAllRoomType(){

        List<RoomTypes> roomTypes = roomTypesRepository.findAll();

        return roomTypes.stream()
                .map(roomTypeMapper::toRoomTypeResponse)
                .collect(Collectors.toList());
    }

}
