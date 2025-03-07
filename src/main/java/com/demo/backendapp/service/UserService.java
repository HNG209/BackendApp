package com.demo.backendapp.service;

import com.demo.backendapp.dto.request.UserCreationRequest;
import com.demo.backendapp.dto.request.UserUpdateRequest;
import com.demo.backendapp.dto.response.UserResponse;
import com.demo.backendapp.entity.Role;
import com.demo.backendapp.entity.User;
import com.demo.backendapp.exception.AppException;
import com.demo.backendapp.exception.ErrorCode;
import com.demo.backendapp.mapper.UserMapper;
import com.demo.backendapp.repository.RoleRepository;
import com.demo.backendapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper mapper;

    public UserResponse addUser(UserCreationRequest request){
        if(userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USERNAME_EXISTED);

        User user = mapper.toUser(request);

        List<Role> roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        userRepository.save(user);
        return mapper.toUserResponse(user);
    }

    public UserResponse myInfo(){
        Authentication context = SecurityContextHolder.getContext().getAuthentication();
        String username = context.getName();

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent())
            return mapper.toUserResponse(user.get());
        return new UserResponse();
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(int id) {
        log.info("enter");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return mapper.toUserResponse(user);
    }

    @Transactional
    public UserResponse updateUser(int id, UserUpdateRequest request){
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        mapper.updateUser(user, request);

        List<Role> roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        userRepository.save(user);

        return mapper.toUserResponse(user);
    }

    @Transactional
    @PreAuthorize("hasRole(\"ADMIN\")")
    public void deleteUser(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userRepository.delete(user);
    }

    @PreAuthorize("hasRole(\"ADMIN\")")
    public List<UserResponse> getUsers(){

        return userRepository.findAll()
                .stream()
                .map(mapper::toUserResponse)
                .toList();
    }

    @Transactional
    public void createAdminUser() {
        Role role = roleRepository.findById("ADMIN").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        if(userRepository.findByUsername("admin").isEmpty()){
            userRepository.save(User.builder()
                    .username("admin")
                    .password("admin")
                    .roles(Set.of(role))
                    .build());
        }
    }
}
