package com.chi.PhongTro.controller;


import com.chi.PhongTro.dto.Request.ApiResponse;
import com.chi.PhongTro.dto.Request.BuildingCreationRequest;
import com.chi.PhongTro.dto.Request.BuildingUpdateRequest;
import com.chi.PhongTro.dto.Response.BuildingResponse;
import com.chi.PhongTro.service.BuildingService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/buildings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BuildingController {

    BuildingService buildingService;

    @PostMapping
    ApiResponse<BuildingResponse> createBuilding(@RequestBody @Valid BuildingCreationRequest request){
        return ApiResponse.<BuildingResponse>builder()
                .code(1000)
                .result(buildingService.createBuilding(request))
                .build();
    }

    @PutMapping("/{buildingId}")
    ApiResponse<BuildingResponse> updateBuilding(@PathVariable String buildingId , @RequestBody @Valid BuildingUpdateRequest request){
        return  ApiResponse.<BuildingResponse>builder()
                .code(1000)
                .result(buildingService.updateBuilding(buildingId, request))
                .build();
    }

    @DeleteMapping("/{buildingId}")
    ApiResponse<String> deleteBuilding(@PathVariable String buildingId){
        buildingService.deleteBuilding(buildingId);
        return ApiResponse.<String>builder()
                .code(1000)
                .result("Đã xóa dãy trọ")
                .build();
    }

    @GetMapping
    ApiResponse<List<BuildingResponse>> getAllMyBuildings(){
        return ApiResponse.<List<BuildingResponse>>builder()
                .code(1000)
                .result(buildingService.getAllMyBuildings())
                .build();
    }


}
