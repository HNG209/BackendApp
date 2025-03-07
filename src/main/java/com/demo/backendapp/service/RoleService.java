package com.demo.backendapp.service;

import com.demo.backendapp.dto.request.RoleCreationRequest;
import com.demo.backendapp.dto.request.RoleDeletionRequest;
import com.demo.backendapp.dto.response.RoleResponse;
import com.demo.backendapp.entity.Permission;
import com.demo.backendapp.entity.Role;
import com.demo.backendapp.exception.AppException;
import com.demo.backendapp.exception.ErrorCode;
import com.demo.backendapp.mapper.RoleMapper;
import com.demo.backendapp.repository.PermissionRepository;
import com.demo.backendapp.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Transactional
    @PreAuthorize("hasRole(\"ADMIN\")")
    public RoleResponse createRole(RoleCreationRequest request) {
        Role role = roleMapper.toRole(request);

        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());

        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);

        return roleMapper.toRoleResponse(role);
    }

    @PreAuthorize("hasRole(\"ADMIN\")")
    public List<RoleResponse> getAll() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    @PreAuthorize("hasRole(\"ADMIN\")")
    public Role findById(String id) {
        return roleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
    }

    @Transactional
    @PreAuthorize("hasRole(\"ADMIN\")")
    public void deleteRole(RoleDeletionRequest request) {
        Role role = findById(request.getName());

        roleRepository.delete(role);
    }
}
