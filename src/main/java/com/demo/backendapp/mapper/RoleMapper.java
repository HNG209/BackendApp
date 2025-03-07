package com.demo.backendapp.mapper;

import com.demo.backendapp.dto.request.RoleCreationRequest;
import com.demo.backendapp.dto.response.RoleResponse;
import com.demo.backendapp.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponse toRoleResponse(Role role);

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleCreationRequest request);
}
