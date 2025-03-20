package com.chi.PhongTro.dto.Request;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostFilterRequest {
    String typeId = null;
    String city = null;
    String district = null;
    String address = null;
    Double minPrice;
    Double maxPrice;
    Double minArea;
    Double maxArea;
    int page = 0;
    int size = 3;
}
