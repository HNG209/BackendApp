package com.demo.backendapp.service;

import com.demo.backendapp.dto.request.PermissionDeletionRequest;
import com.demo.backendapp.dto.request.PermissonCreationRequest;
import com.demo.backendapp.dto.request.RoleDeletionRequest;
import com.demo.backendapp.dto.response.PermissionResponse;
import com.demo.backendapp.entity.Permission;
import com.demo.backendapp.entity.Role;
import com.demo.backendapp.exception.AppException;
import com.demo.backendapp.exception.ErrorCode;
import com.demo.backendapp.mapper.PermissionMapper;
import com.demo.backendapp.repository.PermissionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PermissionMapper permissionMapper;

    @PreAuthorize("hasRole(\"ADMIN\")")
    public PermissionResponse createPermission(PermissonCreationRequest request){
        Permission p = permissionMapper.toPermission(request);

        permissionRepository.save(p);

        return PermissionResponse.builder()
                .name(p.getName())
                .description(p.getDescription())
                .build();
    }

    @PreAuthorize("hasRole(\"ADMIN\")")
    public List<PermissionResponse> getAllPermission() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toPermissionResponse)
                .toList();
    }

    @PreAuthorize("hasRole(\"ADMIN\")")
    public Permission findById(String id) {
        return permissionRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
    }

    @Transactional
    @PreAuthorize("hasRole(\"ADMIN\")")
    public void deletePermission(PermissionDeletionRequest request) {
        Permission permission = findById(request.getName());

        permissionRepository.delete(permission);
    }
}
