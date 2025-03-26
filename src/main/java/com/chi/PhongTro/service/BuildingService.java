package com.chi.PhongTro.service;

import com.chi.PhongTro.dto.Request.BuildingCreationRequest;
import com.chi.PhongTro.dto.Request.BuildingUpdateRequest;
import com.chi.PhongTro.dto.Response.BuildingResponse;
import com.chi.PhongTro.entity.Buildings;
import com.chi.PhongTro.entity.Users;
import com.chi.PhongTro.exception.AppException;
import com.chi.PhongTro.exception.ErrorCode;
import com.chi.PhongTro.mapper.BuildingMapper;
import com.chi.PhongTro.repository.BuildingRepository;
import com.chi.PhongTro.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BuildingService {

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BuildingMapper buildingMapper;


    @Transactional
    @PreAuthorize("hasRole('ADMIN') || hasRole('OWNER')")
    public BuildingResponse createBuilding(BuildingCreationRequest request) {


        var context = SecurityContextHolder.getContext().getAuthentication().getName();

        Users user = usersRepository.findByPhone(context)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Tạo tòa nhà
        Buildings building = Buildings.builder()
                .user(user)
                .name(request.getName())
                .description(request.getDescription())
                .createdAt(LocalDate.now())
                .build();

        building = buildingRepository.save(building);
        return buildingMapper.toBuildingResponse(building);
    }


    @PreAuthorize("hasRole('ADMIN') || hasRole('OWNER')")
    public List<BuildingResponse> getAllMyBuildings() {

        var context = SecurityContextHolder.getContext().getAuthentication().getName();

        Users user = usersRepository.findByPhone(context)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<Buildings> buildings = buildingRepository.findAllByUserId(user.getUser_id());
        return buildings.stream()
                .map(buildingMapper::toBuildingResponse)
                .collect(Collectors.toList());
    }


    public BuildingResponse getBuildingById(String buildingId) {
        Buildings building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new AppException(ErrorCode.BUILDING_NOT_FOUND));
        return buildingMapper.toBuildingResponse(building);
    }


    @Transactional
    @PreAuthorize("@buildingService.checkBuildingPermission(#buildingId, authentication)")
    public BuildingResponse updateBuilding(String buildingId, BuildingUpdateRequest request) {
        Buildings building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new AppException(ErrorCode.BUILDING_NOT_FOUND));

        if (request.getName() != null && !request.getName().isEmpty()) {
            building.setName(request.getName());
        }

        if (request.getDescription() != null) {
            building.setDescription(request.getDescription());
        }

        building = buildingRepository.save(building);
        return buildingMapper.toBuildingResponse(building);
    }

    @Transactional
    @PreAuthorize("@buildingService.checkBuildingPermission(#buildingId, authentication)")
    public void deleteBuilding(String buildingId) {
        Buildings building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new AppException(ErrorCode.BUILDING_NOT_FOUND));
        if (!building.getRooms().isEmpty()) {
            throw new AppException(ErrorCode.BUILDING_HAS_ROOMS);
        }
        buildingRepository.delete(building);
    }

    public boolean checkBuildingPermission(String buildingId, Authentication authentication) {
        Buildings building = buildingRepository.findById(buildingId)
                .orElse(null);
        if (building == null) return false;

        String currentUserPhone = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isCreator = building.getUser().getPhone().equals(currentUserPhone);

        return isAdmin || isCreator;
    }
}