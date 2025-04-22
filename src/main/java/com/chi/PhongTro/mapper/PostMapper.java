package com.chi.PhongTro.mapper;

import com.chi.PhongTro.dto.Request.PostCreationRequest;
import com.chi.PhongTro.dto.Request.PostUpdateRequest;
import com.chi.PhongTro.entity.Posts;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "utilities", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "view_count", ignore = true)
    @Mapping(target = "save_count", ignore = true)
    @Mapping(target = "media", ignore = true)
    Posts toPost(PostCreationRequest request);


    @Mapping(target = "post_id", ignore = true) // Không cho phép cập nhật ID
    @Mapping(target = "user", ignore = true) // Không cho phép cập nhật user
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "view_count", ignore = true)
    @Mapping(target = "save_count", ignore = true)
    @Mapping(target = "media", ignore = true)
    void updatePost(@MappingTarget Posts posts, PostUpdateRequest request);

   // PostResponse toPostResponse(Posts posts);
   // void updatePost(@MappingTarget Posts posts, PostUpdateRequest request);
}
