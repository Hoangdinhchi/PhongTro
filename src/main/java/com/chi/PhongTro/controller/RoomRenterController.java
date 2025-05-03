package com.chi.PhongTro.controller;


import com.chi.PhongTro.dto.Request.ApiResponse;
import com.chi.PhongTro.dto.Request.RoomRenterCreationRequest;
import com.chi.PhongTro.dto.Request.RoomRenterUpdateRequest;
import com.chi.PhongTro.dto.Response.RoomRenterResponse;
import com.chi.PhongTro.service.RoomRenterService;
import jakarta.validation.Valid;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room-renter")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoomRenterController {

    RoomRenterService roomRenterService;


    @PostMapping
    public ApiResponse<String> cretaeRoomRenter(@RequestBody @Valid RoomRenterCreationRequest request) {

        roomRenterService.save(request);
        return ApiResponse.<String>builder()
                .code(1000)
                .result("Success")
                .build();
    }


    @DeleteMapping("/{roomRenterId}")
    public ApiResponse<String> deleteRoomRenter(@PathVariable String roomRenterId) {
        roomRenterService.delete(roomRenterId);
        return ApiResponse.<String>builder()
                .code(1000)
                .result("Success")
                .build();
    }

    @GetMapping("/{roomRenterId}")
    public ApiResponse<RoomRenterResponse> getRoomRenterById(@PathVariable String roomRenterId) {
        return ApiResponse.<RoomRenterResponse>builder()
                .code(1000)
                .result(roomRenterService.getRoomRenterById(roomRenterId))
                .build();

    }


    @GetMapping("/room/{roomId}")
    public ApiResponse<List<RoomRenterResponse>> getAllRoomRentersByRoomId(@PathVariable String roomId) {
        return ApiResponse.<List<RoomRenterResponse>>builder()
                .code(1000)
                .result(roomRenterService.getAllRoomRentersByRoomId(roomId))
                .build();
    }

    @GetMapping("/renter/{renterId}")
    public ApiResponse<List<RoomRenterResponse>> getAllRoomRenterByRenterId(@PathVariable String renterId) {
        return ApiResponse.<List<RoomRenterResponse>>builder()
                .code(1000)
                .result(roomRenterService.getAllRoomRentersByRenterId(renterId))
                .build();
    }


    @PutMapping("/{roomRenterId}")
    public ApiResponse<String> updateRoomRenter(@PathVariable String roomRenterId,
                                                @RequestBody @Valid RoomRenterUpdateRequest request) {
        roomRenterService.update(roomRenterId, request);
        return ApiResponse.<String>builder()
                .code(1000)
                .result("Success")
                .build();
    }

}
