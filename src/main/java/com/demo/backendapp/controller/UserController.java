package com.demo.backendapp.controller;

import com.demo.backendapp.dto.request.UserCreationRequest;
import com.demo.backendapp.dto.request.UserUpdateRequest;
import com.demo.backendapp.dto.response.ApiResponse;
import com.demo.backendapp.dto.response.UserResponse;
import com.demo.backendapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request){
        ApiResponse<UserResponse> response = new ApiResponse<>();

        response.setResult(userService.addUser(request));

        return response;
    }

    @GetMapping("/myInfo")
    public ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.myInfo())
                .build();
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getUsers(){
        ApiResponse<List<UserResponse>> response = new ApiResponse<>();

        response.setResult(userService.getUsers());

        return response;
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable("id") int userID, @RequestBody @Valid UserUpdateRequest request){
        ApiResponse<UserResponse> response = new ApiResponse<>();

        response.setResult(userService.updateUser(userID, request));
        response.setMessage("user " + userID + " updated!");

        return response;
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUser(@PathVariable("id") int userId) {
        ApiResponse<UserResponse> response = new ApiResponse<>();

        response.setResult(userService.getUser(userId));

        return response;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable("id") int userId) {
        ApiResponse<String> response = new ApiResponse<>();

        userService.deleteUser(userId);

        response.setMessage("user " + userId + " deleted");

        return response;
    }
}
