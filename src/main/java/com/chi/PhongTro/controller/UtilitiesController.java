package com.chi.PhongTro.controller;

import com.chi.PhongTro.dto.Request.ApiResponse;
import com.chi.PhongTro.dto.Response.RoomTypeResponse;
import com.chi.PhongTro.dto.Response.UtilitiesResponse;
import com.chi.PhongTro.service.RoomTypeService;
import com.chi.PhongTro.service.UtilitiesService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/utilities")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UtilitiesController {

    UtilitiesService utilitiesService;

    @GetMapping
    ApiResponse<List<UtilitiesResponse>> getAllRoomType(){
        return ApiResponse.<List<UtilitiesResponse>>builder()
                .code(1000)
                .result(utilitiesService.getAllUilities())
                .build();
    }


}
