package com.chi.PhongTro.controller;

import com.chi.PhongTro.dto.Request.ApiResponse;
import com.chi.PhongTro.dto.Request.ElectricityRequest;
import com.chi.PhongTro.dto.Request.WaterRequest;
import com.chi.PhongTro.dto.Response.ElectricityResponse;
import com.chi.PhongTro.dto.Response.WaterResponse;
import com.chi.PhongTro.service.ElectricityService;
import com.chi.PhongTro.service.WaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/waters")
@RequiredArgsConstructor
public class WaterController {
    private final WaterService waterService;

    @PutMapping("/update")
    public ApiResponse<WaterResponse> updatePrices(@RequestBody WaterRequest request) {
        return ApiResponse.<WaterResponse>builder()
                .code(1000)
                .result(waterService.update(request))
                .build();
    }

    @GetMapping("/current")
    public ApiResponse<WaterResponse> getCurrentPrices() {
        return ApiResponse.<WaterResponse>builder()
                .code(1000)
                .result(waterService.getCurrentPrices())
                .build();
    }
}