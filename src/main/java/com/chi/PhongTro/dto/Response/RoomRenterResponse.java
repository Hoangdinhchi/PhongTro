package com.chi.PhongTro.dto.Response;


import com.chi.PhongTro.entity.RoomRenters;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomRenterResponse {
    Long roomRenterId;
    roomDTO room;
    renterDTO renter;
    LocalDate start_date;
    LocalDate end_date;

    public RoomRenterResponse(RoomRenters roomRenters) {
        this.roomRenterId = roomRenters.getId();
        this.room = new roomDTO(roomRenters.getRoom().getRoomId(),
                roomRenters.getRoom().getRoomNumber(),
                roomRenters.getRoom().getBuilding().getName(),
                roomRenters.getRoom().getBuilding().getUser().getUsername(),
                roomRenters.getRoom().getBuilding().getUser().getPhone(),
                roomRenters.getRoom().getBuilding().getStreet(),
                roomRenters.getRoom().getBuilding().getDistrict(),
                roomRenters.getRoom().getBuilding().getCity());
        this.renter = new renterDTO(roomRenters.getRenter().getRenterId(),
                roomRenters.getRenter().getFullName(),
                roomRenters.getRenter().getPhone());
        this.start_date = roomRenters.getStartDate();
        this.end_date = roomRenters.getEndDate();
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static class roomDTO{
        long room_id;
        String roomNumber;
        String buildingName;
        String owner_name;
        String owner_phone;
        String address;
        String district;
        String city;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static class renterDTO{
        long renter_id;
        String username;
        String phone;
    }
}
