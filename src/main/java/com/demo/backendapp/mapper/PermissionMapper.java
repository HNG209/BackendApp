package com.demo.backendapp.mapper;

import com.demo.backendapp.dto.request.PermissonCreationRequest;
import com.demo.backendapp.dto.response.PermissionResponse;
import com.demo.backendapp.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissonCreationRequest permissonCreationRequest);
    PermissionResponse toPermissionResponse(Permission permission);
}
