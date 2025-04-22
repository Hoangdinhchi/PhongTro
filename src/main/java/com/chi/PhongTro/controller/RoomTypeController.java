package com.chi.PhongTro.controller;

import com.chi.PhongTro.dto.Request.ApiResponse;
import com.chi.PhongTro.dto.Request.RenterCreationRequest;
import com.chi.PhongTro.dto.Request.RenterUpdateRequest;
import com.chi.PhongTro.dto.Response.RenterResponse;
import com.chi.PhongTro.dto.Response.RoomTypeResponse;
import com.chi.PhongTro.service.RenterService;
import com.chi.PhongTro.service.RoomTypeService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room-type")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoomTypeController {

    RoomTypeService roomTypeService;

    @GetMapping
    ApiResponse<List<RoomTypeResponse>> getAllRoomType(){
        return ApiResponse.<List<RoomTypeResponse>>builder()
                .code(1000)
                .result(roomTypeService.getAllRoomType())
                .build();
    }


}
