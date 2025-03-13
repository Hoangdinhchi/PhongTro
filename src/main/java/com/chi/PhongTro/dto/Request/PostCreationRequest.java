package com.chi.PhongTro.dto.Request;


import com.chi.PhongTro.util.TypeMedia;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostCreationRequest {
    long userId;
    long typeId;
    String title;
    String description;
    String address;
    String city;
    String district;
    Double price;
    Double area;
    Set<Long> utilityIds;
    List<MediaRequest> media;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class MediaRequest {
        MultipartFile file;
    }
}
