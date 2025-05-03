package com.chi.PhongTro.service;


import com.chi.PhongTro.dto.Request.WaterRequest;
import com.chi.PhongTro.dto.Response.WaterResponse;
import com.chi.PhongTro.entity.Water;
import com.chi.PhongTro.mapper.WaterMapper;
import com.chi.PhongTro.repository.WaterRepository;
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
public class WaterService {
    WaterRepository waterRepository;

    WaterMapper waterMapper;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public WaterResponse update(WaterRequest request) {
        Water water = waterMapper.toWater(request);
        water.setCreate_at(LocalDate.now());
        Water saveWater = waterRepository.save(water);

        return waterMapper.toWaterResponse(saveWater);
    }

    public WaterResponse getCurrentPrices() {
        Water water = waterRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("CAN NOT FIND CURRENT PRICES OF WATER"));

        return waterMapper.toWaterResponse(water);
    }


}
