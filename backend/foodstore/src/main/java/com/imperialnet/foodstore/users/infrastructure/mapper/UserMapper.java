package com.imperialnet.foodstore.users.infrastructure.mapper;

import com.imperialnet.foodstore.users.domain.model.Role;
import com.imperialnet.foodstore.users.domain.model.User;
import com.imperialnet.foodstore.users.infrastructure.persistence.UserEntity;
import com.imperialnet.foodstore.users.infrastructure.web.dto.UserResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre UserEntity (JPA) y User (dominio).
 */
@Component
public class UserMapper {

    private UserMapper() {
        // Evitar instanciación
    }

    /**
     * Convierte un UserEntity (infraestructura) a un User (dominio).
     */
    public static User toDomain(UserEntity entity) {
        if (entity == null) return null;

        return User.rehydrate(
                entity.getId(),
                entity.getName(),
                entity.getLastname(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getRole(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getCreatedBy(),
                entity.getUpdatedBy()
        );
    }

    /**
     * Convierte un User (dominio) a un UserEntity (infraestructura).
     * - Si el User aún no tiene id, JPA lo generará al persistir.
     */
    public static UserEntity toEntity(User user) {
        if (user == null) return null;

        return UserEntity.builder()
                .id(user.getId()) // puede ser null (nuevo usuario)
                .name(user.getName())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .passwordHash(user.getPasswordHash())
                .role(user.getRole() != null ? user.getRole() : Role.ATENCION)
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .createdBy(user.getCreatedBy())
                .updatedBy(user.getUpdatedBy())
                .build();
    }
    /**
     * Convierte un UserEntity (infraestructura) a un UserResponse (infraestructura- dto).
     */
    public static UserResponse toResponse(User entity) {
        return new UserResponse(
                entity.getId(),
                entity.getName(),
                entity.getLastname(),
                entity.getEmail(),
                entity.getRole(),
                entity.isActive()
        );
    }
}
