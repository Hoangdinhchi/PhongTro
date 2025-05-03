package com.chi.PhongTro.dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ElectricityResponse {
    Long id;
    double level_1;
    double level_2;
    double level_3;
    double level_4;
    double level_5;
    double level_6;
    LocalDate create_at;
}
