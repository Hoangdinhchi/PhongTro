package com.chi.PhongTro.dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomResponse {
    Long roomId;
    Long buildingId;
    String buildingName;
    String roomNumber;
    String status;
    Integer occupants;
    Double price;
    LocalDate createdAt;
}