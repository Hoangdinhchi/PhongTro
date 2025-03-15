package com.chi.PhongTro.dto.Response;

import com.chi.PhongTro.entity.Posts;
import com.chi.PhongTro.entity.Utilities;
import com.chi.PhongTro.util.TypeMedia;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {
    Long post_id;
    String user_name;
    String type_name;
    String title;
    String description;
    String address;
    String city;
    String district;
    Double price;
    Double area;
    List<String> utilities;
    List<MediaDTO> mediaDTO;
    String status;
    LocalDate created_at;
    int view_count;
    int save_count;

    public PostResponse(Posts post) {
        this.post_id = post.getPost_id();
        this.user_name = post.getUser().getUsername();
        this.type_name = post.getType().getType_name();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.address = post.getAddress();
        this.city = post.getCity();
        this.district = post.getDistrict();
        this.price = post.getPrice();
        this.area = post.getArea();
        this.utilities = post.getUtilities()
                .stream()
                .map(Utilities::getName)
                .collect(Collectors.toList());
        this.mediaDTO = post.getMedia() != null
                ? post.getMedia().stream()
                .map(media -> new MediaDTO(media.getFile_url(), media.getFile_type()))
                .collect(Collectors.toList()) :
                List.of();
        this.status = post.getStatus();
        this.created_at = post.getCreated_at();
        this.view_count = post.getView_count();
        this.save_count = post.getSave_count();
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static class MediaDTO {
        String fileUrl;
        TypeMedia fileType;
    }
}

