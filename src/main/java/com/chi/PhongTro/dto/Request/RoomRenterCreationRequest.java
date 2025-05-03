package com.chi.PhongTro.dto.Request;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomRenterCreationRequest {

    @NotNull(message = "ROOM_ID_NULL")
    String roomId;

    @NotNull(message = "RENTER_ID_NULL")
    String renterId;

    LocalDate startDate;
    LocalDate endDate;

}
