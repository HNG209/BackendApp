package com.demo.backendapp.controller;

import com.demo.backendapp.dto.request.RoleCreationRequest;
import com.demo.backendapp.dto.request.RoleDeletionRequest;
import com.demo.backendapp.dto.response.ApiResponse;
import com.demo.backendapp.dto.response.RoleResponse;
import com.demo.backendapp.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping
    public ApiResponse<RoleResponse> create(@RequestBody RoleCreationRequest request) {
        RoleResponse response = roleService.createRole(request);

        return ApiResponse.<RoleResponse>builder()
                .result(response)
                .build();
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAll(){
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping
    public ApiResponse<String> delete(@RequestBody RoleDeletionRequest request) {
        roleService.deleteRole(request);

        return ApiResponse.<String>builder()
                .message("role [" + request.getName() + "] successfully deleted")
                .build();
    }
}
