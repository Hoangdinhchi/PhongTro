package com.chi.PhongTro.service;

import com.chi.PhongTro.dto.Request.RoomCreationRequest;
import com.chi.PhongTro.dto.Request.RoomUpdateRequest;
import com.chi.PhongTro.dto.Response.RoomResponse;
import com.chi.PhongTro.entity.Buildings;
import com.chi.PhongTro.entity.Rooms;
import com.chi.PhongTro.exception.AppException;
import com.chi.PhongTro.exception.ErrorCode;
import com.chi.PhongTro.mapper.RoomMapper;
import com.chi.PhongTro.repository.BuildingRepository;
import com.chi.PhongTro.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private RoomMapper roomMapper;

    @Transactional
    @PreAuthorize("hasRole('ADMIN') || hasRole('OWNER')")
    public RoomResponse createRoom(RoomCreationRequest request) {

        Buildings building = buildingRepository.findById(request.getBuildingId())
                .orElseThrow(() -> new AppException(ErrorCode.BUILDING_NOT_FOUND));

        // Kiểm tra trạng thái hợp lệ
        if (!List.of("available", "occupied").contains(request.getStatus())) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        // Tạo phòng
        Rooms room = Rooms.builder()
                .building(building)
                .roomNumber(request.getRoomNumber())
                .status(request.getStatus())
                .occupants(request.getOccupants() != null ? request.getOccupants() : 0)
                .price(request.getPrice())
                .createdAt(LocalDate.now())
                .build();

        room = roomRepository.save(room);
        return roomMapper.toRoomResponse(room);
    }

    // Lấy tất cả phòng (public)
    public List<RoomResponse> getAllRooms() {
        List<Rooms> rooms = roomRepository.findAll();
        return rooms.stream()
                .map(roomMapper::toRoomResponse)
                .collect(Collectors.toList());
    }

    // Lấy phòng theo ID (public)
    public RoomResponse getRoomById(String roomId) {
        Rooms room = roomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
        return roomMapper.toRoomResponse(room);
    }

    // Cập nhật phòng (chỉ Admin)
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RoomResponse updateRoom(String roomId, RoomUpdateRequest request) {
        Rooms room = roomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));

        if (request.getRoomNumber() != null && !request.getRoomNumber().isEmpty()) {
            room.setRoomNumber(request.getRoomNumber());
        }

        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
            if (!List.of("available", "occupied", "maintenance").contains(request.getStatus())) {
                throw new AppException(ErrorCode.INVALID_STATUS);
            }
            room.setStatus(request.getStatus());
        }

        if (request.getOccupants() != null) {
            room.setOccupants(request.getOccupants());
        }

        if (request.getPrice() != null) {
            room.setPrice(request.getPrice());
        }

        room = roomRepository.save(room);
        return roomMapper.toRoomResponse(room);
    }

    // Xóa phòng (chỉ Admin)
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteRoom(String roomId) {
        Rooms room = roomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
        roomRepository.delete(room);
    }
}