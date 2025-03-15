package com.chi.PhongTro.dto.Request;


import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostCreationRequest {

    @NotNull(message = "ROOM_TYPE_NOT_EXISTED")
    private Long typeId;

    @NotBlank(message = "POST_TITLE_BLANK")
    @Size(min = 10, message = "POST_TITLE_TOO_SHORT")
    private String title;

    @NotBlank(message = "POST_DESCRIPTION_BLANK")
    @Size(min = 20, message = "POST_DESCRIPTION_TOO_SHORT")
    private String description;

    @NotBlank(message = "POST_ADDRESS_BLANK")
    private String address;

    @NotBlank(message = "POST_CITY_BLANK")
    private String city;

    @NotBlank(message = "POST_DISTRICT_BLANK")
    private String district;

    @NotNull(message = "POST_PRICE_INVALID")
    @Positive(message = "POST_PRICE_INVALID")
    private Double price;

    @NotNull(message = "POST_AREA_INVALID")
    @Positive(message = "POST_AREA_INVALID")
    private Double area;

    @NotEmpty(message = "POST_UTILITY_IDS_EMPTY")
    private List<Long> utilityIds;

    @NotEmpty(message = "POST_MEDIA_EMPTY")
    private List<MultipartFile> media;



}
