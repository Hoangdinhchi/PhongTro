package com.chi.PhongTro.dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WaterResponse {
    Long id;
    double level_1;
    double level_2;
    double level_3;
    double level_4;
    LocalDate create_at;
}
