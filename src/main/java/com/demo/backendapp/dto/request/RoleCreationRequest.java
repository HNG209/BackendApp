package com.demo.backendapp.dto.request;

import com.demo.backendapp.entity.Permission;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleCreationRequest {
    String name;
    String description;

    List<String> permissions;
}
