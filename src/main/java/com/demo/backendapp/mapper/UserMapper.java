package com.demo.backendapp.mapper;

import com.demo.backendapp.dto.request.UserCreationRequest;
import com.demo.backendapp.dto.request.UserUpdateRequest;
import com.demo.backendapp.dto.response.UserResponse;
import com.demo.backendapp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
