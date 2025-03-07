package com.demo.backendapp.controller;

import com.demo.backendapp.dto.request.PermissionDeletionRequest;
import com.demo.backendapp.dto.request.PermissonCreationRequest;
import com.demo.backendapp.dto.response.ApiResponse;
import com.demo.backendapp.dto.response.PermissionResponse;
import com.demo.backendapp.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @PostMapping
    public ApiResponse<PermissionResponse> create(@RequestBody PermissonCreationRequest request) {
        PermissionResponse response = permissionService.createPermission(request);

        return ApiResponse.<PermissionResponse>builder()
                .result(response)
                .build();
    }

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAll() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAllPermission())
                .build();
    }

    @DeleteMapping
    public ApiResponse<String> delete(@RequestBody PermissionDeletionRequest request) {
        permissionService.deletePermission(request);

        return ApiResponse.<String>builder()
                .message("permission [" + request.getName() + "] successfully deleted")
                .build();
    }
}
