package com.chi.PhongTro.service;

import com.chi.PhongTro.dto.Request.RoomCreationRequest;
import com.chi.PhongTro.dto.Request.RoomUpdateRequest;
import com.chi.PhongTro.dto.Response.RoomResponse;
import com.chi.PhongTro.entity.Buildings;
import com.chi.PhongTro.entity.Renters;
import com.chi.PhongTro.entity.Rooms;
import com.chi.PhongTro.entity.Users;
import com.chi.PhongTro.exception.AppException;
import com.chi.PhongTro.exception.ErrorCode;
import com.chi.PhongTro.mapper.RenterMapper;
import com.chi.PhongTro.mapper.RoomMapper;
import com.chi.PhongTro.repository.BuildingRepository;
import com.chi.PhongTro.repository.RenterRepository;
import com.chi.PhongTro.repository.RoomRepository;
import com.chi.PhongTro.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private RenterRepository renterRepository;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private RenterMapper renterMapper;

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private UsersRepository usersRepository;

    @Transactional
    @PreAuthorize("hasRole('ADMIN') || hasRole('OWNER')")
    public RoomResponse createRoom(RoomCreationRequest request) {

        Buildings building = buildingRepository.findById(request.getBuildingId())
                .orElseThrow(() -> new AppException(ErrorCode.BUILDING_NOT_FOUND));


        if (!List.of("available", "occupied").contains(request.getStatus())) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }


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
    @PreAuthorize("hasRole('ADMIN')")
    public List<RoomResponse> getAllRooms() {
        List<Rooms> rooms = roomRepository.findAll();
        return rooms.stream()
                .map(roomMapper::toRoomResponse)
                .collect(Collectors.toList());
    }


    //Lấy tất cả phòng của người dùng đang đăng nhập
    @PreAuthorize("hasRole('ADMIN') || hasRole('OWNER')")
    public List<RoomResponse> getAllRoomsOfUser() {
        var context = SecurityContextHolder.getContext().getAuthentication();
        Users user =  usersRepository.findByPhone(context.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        List<Rooms> rooms = roomRepository.findByUser(user.getPhone());
        return rooms.stream()
                .map(roomMapper::toRoomResponse)
                .collect(Collectors.toList());
    }


    // Lấy phòng theo ID (public)

    @PreAuthorize("@roomService.checkRoomPermission(#roomId, authentication)")
    public RoomResponse getRoomById(String roomId) {
        Rooms room = roomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
        RoomResponse response = roomMapper.toRoomResponse(room);
        return roomMapper.toRoomResponse(room);
    }

    @PreAuthorize("@buildingService.checkBuildingPermission(#buildingId, authentication)")
    public List<RoomResponse> getRoomByBuildingId(String buildingId){
        List<Rooms> rooms = roomRepository.finAllByBuildingId(buildingId);
        return rooms.stream()
                .map(roomMapper::toRoomResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @PreAuthorize("@roomService.checkRoomPermission(#roomId, authentication)")
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


    @Transactional
    @PreAuthorize("@roomService.checkRoomPermission(#roomId, authentication)")
    public void deleteRoom(String roomId) {
        Rooms room = roomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));

        if (!room.getRoomRenters().isEmpty() && !room.getStatus().equals("occupied"))
            throw new AppException(ErrorCode.ROOM_DO_NOT_DELETE);

        roomRepository.delete(room);
    }



    // Thêm người thuê vào phòng (Admin và Owner)
//    @Transactional
//    @PreAuthorize("@roomService.checkRoomPermission(#roomId, authentication)")
//    public RoomResponse addRenterToRoom(String roomId, String renterId) {
//        Rooms room = roomRepository.findById(roomId)
//                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
//
//        Renters renter = renterRepository.findById(renterId)
//                .orElseThrow(() -> new AppException(ErrorCode.RENTER_NOT_FOUND));
//
//        // Thêm người thuê vào phòng
//        room.getRenters().add(renter);
//        room.setOccupants(room.getRenters().size()); // Cập nhật số người trong phòng
//        if (room.getRenters().size() > 0) {
//            room.setStatus("occupied");
//        }
//
//        room = roomRepository.save(room);
//        return roomMapper.toRoomResponse(room);
//    }

    // Xóa người thuê khỏi phòng (Admin và Owner tạo room)
//    @Transactional
//    @PreAuthorize("@roomService.checkRoomPermission(#roomId, authentication)")
//    public RoomResponse removeRenterFromRoom(String roomId, String renterId) {
//        Rooms room = roomRepository.findById(roomId)
//                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
//
//        Renters renter = renterRepository.findById(renterId)
//                .orElseThrow(() -> new AppException(ErrorCode.RENTER_NOT_FOUND));
//
//        // Xóa người thuê khỏi phòng
//        room.getRenters().remove(renter);
//        room.setOccupants(room.getRenters().size()); // Cập nhật số người trong phòng
//        if (room.getRenters().isEmpty()) {
//            room.setStatus("available");
//        }
//
//        room = roomRepository.save(room);
//        return roomMapper.toRoomResponse(room);
//    }

    public boolean checkRoomPermission(String roomId, Authentication authentication) {
        Rooms room = roomRepository.findById(roomId)
                .orElse(null);
        if (room == null) return false;

        String currentUserPhone = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isCreator = room.getBuilding().getUser().getPhone().equals(currentUserPhone);

        return isAdmin || isCreator;
    }
}