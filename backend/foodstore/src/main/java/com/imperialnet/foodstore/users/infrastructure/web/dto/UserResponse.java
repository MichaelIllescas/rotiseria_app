package com.imperialnet.foodstore.users.infrastructure.web.dto;

import com.imperialnet.foodstore.users.domain.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String lastname;
    private String email;
    private Role role;
    private boolean active;
}
