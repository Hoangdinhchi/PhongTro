package com.chi.PhongTro.mapper;

import com.chi.PhongTro.dto.Request.UserCreationRequest;
import com.chi.PhongTro.dto.Request.UserUpdateRequest;
import com.chi.PhongTro.dto.Response.UserResponse;
import com.chi.PhongTro.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    Users toUser(UserCreationRequest request);
    UserResponse toUserReponse(Users users);
    void updateUser(@MappingTarget Users users, UserUpdateRequest request);
}
