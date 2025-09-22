package com.imperialnet.foodstore.users.infrastructure.mapper;

import com.imperialnet.foodstore.users.domain.model.Role;
import com.imperialnet.foodstore.users.domain.model.User;
import com.imperialnet.foodstore.users.infrastructure.web.dto.CreateUserRequest;
import com.imperialnet.foodstore.users.infrastructure.web.dto.CreateUserResponse;

/**
 * Mapper para convertir entre DTOs y dominio User.
 */
public final class UserDtoMapper {

    private UserDtoMapper() {}

    public static User toDomain(CreateUserRequest req, String createdByFullName, String passwordHash) {


        return User.createNew(
                req.name().trim(),
                req.lastname().trim(),
                req.email().trim(),
                passwordHash,
                req.role(),
                createdByFullName
        );
    }

    public static CreateUserResponse toResponse(User user) {
        return new CreateUserResponse(
                user.getId(),
                user.getName(),
                user.getLastname(),
                user.getEmail(),
                user.getRole().name(),
                user.isActive(),
                user.getCreatedAt(),
                user.getCreatedBy()
        );
    }
}
