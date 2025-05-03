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
                roomRenters.getRoom().getBuilding().getName());
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
