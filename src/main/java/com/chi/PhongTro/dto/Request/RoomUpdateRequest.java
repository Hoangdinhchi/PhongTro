package com.chi.PhongTro.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomUpdateRequest {
    @NotNull(message = "BUILDING_NOT_NULL")
    String buildingId;

    @NotBlank(message = "ROOM_NUMBER_BLANK")
    String roomNumber;

    @NotNull(message = "INVALID_STATUS")
    String status;

//    @Size(min = 0, message = "OCCUPANTS_INVALID")
    Integer occupants;
    Double price;
}