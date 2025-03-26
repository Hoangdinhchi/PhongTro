package com.chi.PhongTro.dto.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomUpdateRequest {
    String buildingId;
    String roomNumber;
    String status;
    Integer occupants;
    Double price;
}