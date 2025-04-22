package com.chi.PhongTro.dto.Response;

import com.chi.PhongTro.entity.Comments;
import com.chi.PhongTro.entity.Posts;
import com.chi.PhongTro.entity.Utilities;
import com.chi.PhongTro.util.TypeMedia;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;
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
    String phone;
    String avatar;
    String type_name;
    String title;
    String description;
    String address;
    String city;
    String district;
    Double latitude;
    Double longitude;

    Double price;
    Double area;


    List<String> utilities;
    List<MediaDTO> mediaDTO;
    String status;
    LocalDate created_at;
    int view_count;
    int save_count;

    List<CommentDTO> comments;

    public PostResponse(Posts post) {
        this.post_id = post.getPost_id();
        this.user_name = post.getUser().getUsername();
        this.phone = post.getUser().getPhone();
        this.avatar = post.getUser().getAvatar();
        this.type_name = post.getType().getType_name();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.address = post.getAddress();
        this.city = post.getCity();
        this.district = post.getDistrict();
        this.latitude = post.getLatitude();
        this.longitude = post.getLongitude();
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
        this.created_at = post.getCreatedAt();
        this.view_count = post.getView_count();
        this.save_count = post.getSave_count();
        this.comments = post.getComments() != null
                ? post.getComments().stream()
                .map(comment -> new CommentDTO(comment.getUser().getUsername(), comment.getContent(), comment.getCreatedAt()))
                .collect(Collectors.toList()) :
                List.of();
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static class MediaDTO {
        String fileUrl;
        TypeMedia fileType;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static class CommentDTO {
        String userName;
        String content;
        LocalDate create_at;
    }
}

