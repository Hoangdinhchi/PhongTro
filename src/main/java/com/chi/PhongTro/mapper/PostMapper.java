package com.chi.PhongTro.mapper;

import com.chi.PhongTro.dto.Request.PostCreationRequest;
import com.chi.PhongTro.dto.Response.PostResponse;
import com.chi.PhongTro.entity.Posts;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "utilities", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "created_at", ignore = true)
    @Mapping(target = "view_count", ignore = true)
    @Mapping(target = "save_count", ignore = true)
    @Mapping(target = "media", ignore = true)
    Posts toPost(PostCreationRequest request);

   // PostResponse toPostResponse(Posts posts);
   // void updatePost(@MappingTarget Posts posts, PostUpdateRequest request);
}
