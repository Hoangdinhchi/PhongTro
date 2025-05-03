package com.chi.PhongTro.service;


import com.chi.PhongTro.dto.Request.ElectricityRequest;
import com.chi.PhongTro.dto.Response.ElectricityResponse;
import com.chi.PhongTro.entity.Electricity;
import com.chi.PhongTro.mapper.ElectricityMapper;
import com.chi.PhongTro.repository.ElectricityRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ElectricityService {
    ElectricityRepository electricityRepository;

    ElectricityMapper electricityMapper;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ElectricityResponse update(ElectricityRequest request) {
        Electricity electricity = electricityMapper.toElectricity(request);
        electricity.setCreate_at(LocalDate.now());
        Electricity savedElectricity = electricityRepository.save(electricity);

        return electricityMapper.toElectricityResponse(savedElectricity);
    }

    public ElectricityResponse getCurrentPrices() {
        Electricity electricity = electricityRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("CAN NOT FIND CURRENT PRICES OF ELECTRICITY"));

        return electricityMapper.toElectricityResponse(electricity);
    }


}
