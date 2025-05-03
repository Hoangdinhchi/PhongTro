package com.chi.PhongTro.controller;

import com.chi.PhongTro.dto.Request.ApiResponse;
import com.chi.PhongTro.dto.Request.ElectricityRequest;
import com.chi.PhongTro.dto.Response.ElectricityResponse;
import com.chi.PhongTro.service.ElectricityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/electricity")
@RequiredArgsConstructor
public class ElectricityController {
    private final ElectricityService electricityService;

    @PutMapping("/update")
    public ApiResponse<ElectricityResponse> updatePrices(@RequestBody ElectricityRequest request) {
        return ApiResponse.<ElectricityResponse>builder()
                .code(1000)
                .result(electricityService.update(request))
                .build();
    }

    @GetMapping("/current")
   public ApiResponse<ElectricityResponse> getCurrentPrices() {
        return ApiResponse.<ElectricityResponse>builder()
                .code(1000)
                .result(electricityService.getCurrentPrices())
                .build();
    }
}