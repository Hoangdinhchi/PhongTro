package com.chi.PhongTro.service;


import com.chi.PhongTro.dto.Request.RenterCreationRequest;
import com.chi.PhongTro.dto.Request.RenterUpdateRequest;
import com.chi.PhongTro.dto.Response.RenterResponse;
import com.chi.PhongTro.entity.Renters;
import com.chi.PhongTro.entity.Users;
import com.chi.PhongTro.exception.AppException;
import com.chi.PhongTro.exception.ErrorCode;
import com.chi.PhongTro.mapper.RenterMapper;
import com.chi.PhongTro.repository.RenterRepository;
import com.chi.PhongTro.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RenterService {

    RenterRepository renterRepository;

    UsersRepository usersRepository;
    RenterMapper renterMapper;

    @Transactional
    @PreAuthorize("hasRole('ADMIN') || hasRole('OWNER')")
    public RenterResponse createRenter(RenterCreationRequest request) {

        var context = SecurityContextHolder.getContext().getAuthentication().getName();

        Users user = usersRepository.findByPhone(context)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean has_account = false;

        if (usersRepository.existsByPhone(request.getPhone()))
            has_account = true;

        if(renterRepository.existsByUserIdAndPhone(user.getUser_id(), request.getPhone()))
            throw new AppException(ErrorCode.PHONE_EXISTED);
        if (renterRepository.exitsByUserIdAndEmail(user.getUser_id(), request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_EXISTED);

        Renters renter = Renters.builder()
                .user(user)
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .has_account(has_account)
                .createdAt(LocalDate.now())
                .build();

        renter = renterRepository.save(renter);
        RenterResponse response = renterMapper.toRenterResponse(renter);
        Optional.ofNullable(renter.getRoomRenters())
                .ifPresent(roomRenters -> {
                    List<String> roomNames = roomRenters.stream()
                            .map(roomRenter -> roomRenter.getRoom().getRoomNumber())
                            .collect(Collectors.toList());
                    response.setRoomNames(roomNames);
                });
        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<RenterResponse> getAllRenters() {
        List<Renters> renters = renterRepository.findAll();
        return renters.stream()
                .map(this::toRenterResponse)
                .collect(Collectors.toList());
    }

    public List<RenterResponse> getAllMyRenters() {
        var context = SecurityContextHolder.getContext();
        Users user = usersRepository.findByPhone(context.getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        List<Renters> renters = renterRepository.findByUserId(user.getUser_id());
        return renters.stream()
                .map(this::toRenterResponse)
                .collect(Collectors.toList());
    }


    @PreAuthorize("@renterService.checkRenterPermission(#renterId, authentication)")
    public RenterResponse getRenterById(String renterId) {
        Renters renter = renterRepository.findById(renterId)
                .orElseThrow(() -> new AppException(ErrorCode.RENTER_NOT_FOUND));
        RenterResponse response = renterMapper.toRenterResponse(renter);
        Optional.ofNullable(renter.getRoomRenters())
                .ifPresent(roomRenters -> {
                    List<String> roomNames = roomRenters.stream()
                            .map(roomRenter -> roomRenter.getRoom().getRoomNumber())
                            .collect(Collectors.toList());
                    response.setRoomNames(roomNames);
                });
        return response;
    }


    @Transactional
    @PreAuthorize("@renterService.checkRenterPermission(#renterId, authentication)")
    public RenterResponse updateRenter(String renterId, RenterUpdateRequest request) {
        Renters renter = renterRepository.findById(renterId)
                .orElseThrow(() -> new AppException(ErrorCode.RENTER_NOT_FOUND));

        if (request.getFullName() != null && !request.getFullName().isEmpty()) {
            renter.setFullName(request.getFullName());
        }

        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            if (renter.getPhone() != null && !renter.getPhone().equals(request.getPhone())) {
                if(renterRepository.existsByUserIdAndPhone(renter.getUser().getUser_id(), request.getPhone()))
                    throw new AppException(ErrorCode.PHONE_EXISTED);
            }
            renter.setPhone(request.getPhone());
        }

        if (request.getEmail() != null) {
            if (renter.getEmail() != null && !renter.getEmail().equals(request.getEmail())) {
                if (renterRepository.exitsByUserIdAndEmail(renter.getUser().getUser_id(), request.getEmail())){
                    throw new AppException(ErrorCode.EMAIL_EXISTED);
                }
            }
            renter.setEmail(request.getEmail());
        }


        renter = renterRepository.save(renter);
        RenterResponse response = renterMapper.toRenterResponse(renter);
        Optional.ofNullable(renter.getRoomRenters())
                .ifPresent(roomRenters -> {
                    List<String> roomNames = roomRenters.stream()
                            .map(roomRenter -> roomRenter.getRoom().getRoomNumber())
                            .collect(Collectors.toList());
                    response.setRoomNames(roomNames);
                });
        return response;
    }


    @Transactional
    @PreAuthorize("@renterService.checkRenterPermission(#renterId, authentication)")
    public void deleteRenter(String renterId) {
        Renters renter = renterRepository.findById(renterId)
                .orElseThrow(() -> new AppException(ErrorCode.RENTER_NOT_FOUND));

        renterRepository.delete(renter);
    }

    // Kiểm tra quyền chỉnh sửa/xóa người thuê
    public boolean checkRenterPermission(String renterId, Authentication authentication) {
        Renters renter = renterRepository.findById(renterId)
                .orElse(null);
        if (renter == null) return false;

        String currentUserPhone = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isCreator = renter.getUser().getPhone().equals(currentUserPhone);

        return isAdmin || isCreator;
    }


    private RenterResponse toRenterResponse(Renters renter) {
        RenterResponse response = renterMapper.toRenterResponse(renter);
        Optional.ofNullable(renter.getRoomRenters())
                .ifPresent(roomRenters -> {
                    List<String> roomNames = roomRenters.stream()
                            .map(roomRenter -> roomRenter.getRoom().getRoomNumber())
                            .collect(Collectors.toList());
                    response.setRoomNames(roomNames);
                });
        return response;

    }

}
