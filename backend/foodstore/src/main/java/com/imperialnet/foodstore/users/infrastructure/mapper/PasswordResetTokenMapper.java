package com.imperialnet.foodstore.users.infrastructure.mapper;

// PasswordResetTokenMapper.java

import com.imperialnet.foodstore.users.domain.model.PasswordResetToken;
import com.imperialnet.foodstore.users.infrastructure.persistence.entity.PasswordResetTokenEntity;
import com.imperialnet.foodstore.users.infrastructure.persistence.entity.UserEntity;

public class PasswordResetTokenMapper {

    public static PasswordResetToken toDomain(PasswordResetTokenEntity entity) {
        return new PasswordResetToken(
                entity.getToken(),
                entity.getEmail(),
                entity.getExpiresAt()
        );
    }

    public static PasswordResetTokenEntity toEntity(PasswordResetToken domain, UserEntity userEntity) {
        PasswordResetTokenEntity entity = new PasswordResetTokenEntity();
        entity.setToken(domain.getToken());
        entity.setEmail(domain.getEmail());
        entity.setExpiresAt(domain.getExpiresAt());
        entity.setUser(userEntity); // 👈 asignar user
        return entity;
    }
}
