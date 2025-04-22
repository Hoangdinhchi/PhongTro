package com.chi.PhongTro.service;


import com.chi.PhongTro.dto.Response.RoomTypeResponse;
import com.chi.PhongTro.dto.Response.UtilitiesResponse;
import com.chi.PhongTro.entity.RoomTypes;
import com.chi.PhongTro.entity.Utilities;
import com.chi.PhongTro.mapper.RoomTypeMapper;
import com.chi.PhongTro.mapper.UtilitiesMapper;
import com.chi.PhongTro.repository.RoomTypesRepository;
import com.chi.PhongTro.repository.UtilitiesRepository;
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
public class UtilitiesService {

    UtilitiesRepository utilitiesRepository;

    UtilitiesMapper utilitiesMapper;

    public List<UtilitiesResponse> getAllUilities(){

        List<Utilities> utilities = utilitiesRepository.findAll();

        return utilities.stream()
                .map(utilitiesMapper::toUtilitiesResponse)
                .collect(Collectors.toList());
    }

}
