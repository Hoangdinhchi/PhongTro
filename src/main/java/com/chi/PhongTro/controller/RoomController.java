package com.chi.PhongTro.controller;


import com.chi.PhongTro.dto.Request.*;
import com.chi.PhongTro.dto.Response.CommentResponse;
import com.chi.PhongTro.dto.Response.RoomResponse;
import com.chi.PhongTro.service.RoomService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoomController {

    RoomService roomService;

    @PostMapping
    public ApiResponse<RoomResponse> createRoom(@RequestBody @Valid RoomCreationRequest request) {
        RoomResponse response = roomService.createRoom(request);
        return ApiResponse.<RoomResponse>builder()
                .code(1000)
                .result(response)
                .build();
    }

    @PutMapping("/{roomId}")
    public ApiResponse<RoomResponse> updateRoom(@PathVariable String roomId ,@RequestBody @Valid RoomUpdateRequest request){
        return ApiResponse.<RoomResponse>builder()
                .code(1000)
                .result(roomService.updateRoom(roomId, request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<RoomResponse>> getAllRoom(){
        return ApiResponse.<List<RoomResponse>>builder()
                .code(1000)
                .result(roomService.getAllRooms())
                .build();
    }

    @GetMapping("/my-room")
    public ApiResponse<List<RoomResponse>> getAllMyRoom(){
        return ApiResponse.<List<RoomResponse>>builder()
                .code(1000)
                .result(roomService.getAllRoomsOfUser())
                .build();
    }

    @GetMapping("/{roomId}")
    public ApiResponse<RoomResponse> getRoomById(@PathVariable String roomId){
        return ApiResponse.<RoomResponse>builder()
                .code(1000)
                .result(roomService.getRoomById(roomId))
                .build();
    }


    @GetMapping("/buildings/{buildingId}")
    public ApiResponse<List<RoomResponse>> getRoomByBuildingId(@PathVariable String buildingId){
        return ApiResponse.<List<RoomResponse>>builder()
                .code(1000)
                .result(roomService.getRoomByBuildingId(buildingId))
                .build();
    }

    @DeleteMapping("/{roomId}")
    public ApiResponse<String> deleteRoom(@PathVariable String roomId){
        roomService.deleteRoom(roomId);
        return ApiResponse.<String>builder()
                .code(1000)
                .result("Đã xóa phòng")
                .build();
    }
//
//    @PostMapping("/{roomId}/renters/{renterId}")
//    public ApiResponse<RoomResponse> addRenterToRoom(
//            @PathVariable String roomId,
//            @PathVariable String renterId){
//        return ApiResponse.<RoomResponse>builder()
//                .code(1000)
//                .result(roomService.addRenterToRoom(roomId, renterId))
//                .build();
//    }

//    @DeleteMapping("/{roomId}/renters/{renterId}")
//    public ApiResponse<RoomResponse> removeRenterFromRoom(
//            @PathVariable String roomId,
//            @PathVariable String renterId){
//        return ApiResponse.<RoomResponse>builder()
//                .code(1000)
//                .result(roomService.removeRenterFromRoom(roomId, renterId))
//                .build();
//    }


}
