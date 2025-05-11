package com.chi.PhongTro.service;


import com.chi.PhongTro.dto.Request.RoomRenterCreationRequest;
import com.chi.PhongTro.dto.Request.RoomRenterUpdateRequest;
import com.chi.PhongTro.dto.Response.RoomRenterResponse;
import com.chi.PhongTro.entity.Renters;
import com.chi.PhongTro.entity.RoomRenters;
import com.chi.PhongTro.entity.Rooms;
import com.chi.PhongTro.exception.AppException;
import com.chi.PhongTro.exception.ErrorCode;
import com.chi.PhongTro.mapper.RoomRenterMapper;
import com.chi.PhongTro.repository.RenterRepository;
import com.chi.PhongTro.repository.RoomRenterRepository;
import com.chi.PhongTro.repository.RoomRepository;
import com.chi.PhongTro.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoomRenterService {

    RoomRenterRepository roomRenterRepository;

    RenterRepository renterRepository;

    RoomRepository roomRepository;

    RoomRenterMapper roomRenterMapper;

    UsersRepository usersRepository;

    @Transactional
    @PreAuthorize("@roomRenterService.checkPermission(#request.getRoomId(), #request.getRenterId(), authentication)")
    public void save(RoomRenterCreationRequest request){

        Renters renter = renterRepository.findById(request.getRenterId())
                .orElseThrow(() -> new AppException(ErrorCode.RENTER_NOT_FOUND));
        Rooms room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
        RoomRenters roomRenters = roomRenterMapper.toRoomRenter(request);
        room.setOccupants(room.getRoomRenters().size() + 1);
        room.setStatus(room.getOccupants() == 0 ? "available" : "occupied");
        roomRenters.setRenter(renter);
        roomRenters.setRoom(room);
        roomRenterRepository.save(roomRenters);
        roomRepository.save(room);
    }


    @Transactional
    @PreAuthorize("@roomRenterService.checkPermissionDelete(#roomRenterId, authentication)")
    public void update(String roomRenterId , RoomRenterUpdateRequest request){
        Renters renter = renterRepository.findById(request.getRenterId())
                .orElseThrow(() -> new AppException(ErrorCode.RENTER_NOT_FOUND));
        Rooms newRoom = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
        RoomRenters roomRenters = roomRenterRepository.findById(roomRenterId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_RENTER_NOT_FOUND));
        Rooms oldRoom = roomRenters.getRoom();
        if (!Objects.equals(oldRoom.getRoomId(), newRoom.getRoomId())) {
            oldRoom.setOccupants(oldRoom.getRoomRenters().size() - 1);
            oldRoom.setStatus(oldRoom.getOccupants() == 0 ? "available" : "occupied");
            roomRepository.save(oldRoom);
            newRoom.setOccupants(newRoom.getRoomRenters().size() + 1);
            newRoom.setStatus("occupied");
            roomRepository.save(newRoom);
        }
        roomRenters.setRenter(renter);
        roomRenters.setRoom(newRoom);
        roomRenterMapper.updateRoomRenter(roomRenters, request);
        roomRenterRepository.save(roomRenters);

    }

    @Transactional
    @PreAuthorize("@roomRenterService.checkPermissionDelete(#roomRenterId, authentication)")
    public void delete(String roomRenterId){
        RoomRenters roomRenters = roomRenterRepository.findById(roomRenterId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_RENTER_NOT_FOUND));
        Rooms room = roomRenters.getRoom();
        int remainingOccupants = Math.max(0, room.getRoomRenters().size() - 1);
        room.getRoomRenters().remove(roomRenters);
        room.setOccupants(remainingOccupants);
        room.setStatus(remainingOccupants == 0 ? "available" : "occupied");
        roomRepository.save(room);
        roomRenterRepository.delete(roomRenters);
    }

    @PreAuthorize("@roomRenterService.checkPermissionDelete(#roomRenterId, authentication)")
    public RoomRenterResponse getRoomRenterById(String roomRenterId){
        RoomRenters roomRenters = roomRenterRepository.findById(roomRenterId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_RENTER_NOT_FOUND));
        return new RoomRenterResponse(roomRenters);
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('OWNER')")
    public List<RoomRenterResponse> getAllRoomRentersByRoomId(String roomId){
        return roomRenterRepository.findAllByRoomId(roomId)
                .stream().map(RoomRenterResponse::new)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('OWNER')")
    public List<RoomRenterResponse> getAllRoomRentersByRenterId(String renterId){

        return roomRenterRepository.findAllByRenterId(renterId)
                .stream().map(RoomRenterResponse::new)
                .collect(Collectors.toList());
    }

    public List<RoomRenterResponse> getAllRoomRentersByRenter(){
        var context = SecurityContextHolder.getContext();
        if(renterRepository.findByPhone(context.getAuthentication().getName()).isEmpty())
            throw new AppException(ErrorCode.RENTER_NOT_FOUND);
        return roomRenterRepository.findAllByRenterPhone(context.getAuthentication().getName())
                .stream().map(RoomRenterResponse::new)
                .collect(Collectors.toList());
    }


    public boolean checkPermission(String roomId, String renterId, Authentication authentication){
        Rooms room = roomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
        Renters renter = renterRepository.findById(renterId)
                .orElseThrow(() -> new AppException(ErrorCode.RENTER_NOT_FOUND));
        String currentPhone = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isCreator = (renter.getUser().getPhone().equals(currentPhone) && room.getBuilding().getUser().getPhone().equals(currentPhone));

        return isAdmin || isCreator;
    }

    public boolean checkPermissionDelete(String roomRenterId, Authentication authentication){
        RoomRenters roomRenters = roomRenterRepository.findById(roomRenterId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_RENTER_NOT_FOUND));
        String currentPhone = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isCreator = (roomRenters.getRenter().getUser().getPhone().equals(currentPhone)
                && roomRenters.getRoom().getBuilding().getUser().getPhone().equals(currentPhone));
        boolean isRenter = (roomRenters.getRenter().getPhone().equals(currentPhone));
        return isAdmin || isCreator || isRenter;
    }

}
