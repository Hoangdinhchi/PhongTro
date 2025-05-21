package com.chi.PhongTro.dto.Request;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RenterFilterRequest {
    String name;
    String phone;

    int page = 0;
    int size = 10;

}
