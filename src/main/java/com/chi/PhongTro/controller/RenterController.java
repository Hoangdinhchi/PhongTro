package com.chi.PhongTro.controller;

import com.chi.PhongTro.dto.Request.ApiResponse;
import com.chi.PhongTro.dto.Request.RenterCreationRequest;
import com.chi.PhongTro.dto.Request.RenterUpdateRequest;
import com.chi.PhongTro.dto.Response.RenterResponse;
import com.chi.PhongTro.service.RenterService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/renters")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RenterController {
    RenterService renterService;

    @PostMapping
    ApiResponse<RenterResponse> createRenter(@RequestBody @Valid RenterCreationRequest request){
        return ApiResponse.<RenterResponse>builder()
                .code(1000)
                .result(renterService.createRenter(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RenterResponse>> getAllMyRenter(){
        return ApiResponse.<List<RenterResponse>>builder()
                .code(1000)
                .result(renterService.getAllRenters())
                .build();
    }

    @GetMapping("/{renterId}")
    ApiResponse<RenterResponse> getRenterById(@PathVariable String renterId){
        return ApiResponse.<RenterResponse>builder()
                .code(1000)
                .result(renterService.getRenterById(renterId))
                .build();
    }

    @PutMapping("/{renterId}")
    ApiResponse<RenterResponse> updateRenter(@PathVariable String renterId,@RequestBody @Valid RenterUpdateRequest request){
        return ApiResponse.<RenterResponse>builder()
                .code(1000)
                .result(renterService.updateRenter(renterId, request))
                .build();
    }

    @DeleteMapping("/{renterId}")
    ApiResponse<String> deleteRenter(@PathVariable String renterId){

        renterService.deleteRenter(renterId);
        return ApiResponse.<String>builder()
                .code(1000)
                .result("Đã xóa người thuê")
                .build();
    }


}
