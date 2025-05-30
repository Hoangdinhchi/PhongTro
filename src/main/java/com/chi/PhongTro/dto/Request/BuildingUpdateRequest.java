package com.chi.PhongTro.dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuildingUpdateRequest {
    @NotBlank(message = "NAME_BUILDING_BLANK")
    String name;
    String street;
    String district;
    String city;
    String description;
}