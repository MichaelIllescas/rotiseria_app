package com.imperialnet.foodstore.users.infrastructure.web.dto;

import java.time.LocalDateTime;

/**
 * Outgoing payload after creating a user.
 */
public record CreateUserResponse(
        Long id,
        String name,
        String lastname,
        String email,
        String role,
        boolean active,
        LocalDateTime createdAt,
        String createdBy
) {}
