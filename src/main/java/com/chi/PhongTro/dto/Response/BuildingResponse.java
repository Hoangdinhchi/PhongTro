package com.chi.PhongTro.dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuildingResponse {
    Long buildingId;
    Long userId;
    String userPhone;
    String name;
    String description;
    LocalDate createdAt;
}