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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

        Renters renter = Renters.builder()
                .user(user)
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .has_account(has_account)
                .createdAt(LocalDate.now())
                .build();

        renter = renterRepository.save(renter);
        return renterMapper.toRenterResponse(renter);
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('OWNER')")
    public List<RenterResponse> getAllRenters() {
        List<Renters> renters = renterRepository.findAll();
        return renters.stream()
                .map(renterMapper::toRenterResponse)
                .collect(Collectors.toList());
    }


    @PreAuthorize("hasRole('ADMIN') || hasRole('OWNER')")
    public RenterResponse getRenterById(String renterId) {
        Renters renter = renterRepository.findById(renterId)
                .orElseThrow(() -> new AppException(ErrorCode.RENTER_NOT_FOUND));
        return renterMapper.toRenterResponse(renter);
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
            renter.setPhone(request.getPhone());
        }

        if (request.getEmail() != null) {
            renter.setEmail(request.getEmail());
        }

        renter = renterRepository.save(renter);
        return renterMapper.toRenterResponse(renter);
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

}
