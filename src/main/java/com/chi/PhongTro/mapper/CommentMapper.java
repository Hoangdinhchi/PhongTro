package com.chi.PhongTro.mapper;

import com.chi.PhongTro.dto.Response.CommentResponse;
import com.chi.PhongTro.entity.Comments;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "postId", source = "post.post_id")
    @Mapping(target = "userId", source = "user.user_id")
    @Mapping(target = "userName", source = "user.username")
    CommentResponse toCommentResponse(Comments comment);
}