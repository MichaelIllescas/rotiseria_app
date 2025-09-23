package com.imperialnet.foodstore.users.application.ports.out;

import com.imperialnet.foodstore.users.domain.model.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenRepositoryPort {

    void save(PasswordResetToken token, Long userId);
    Optional<PasswordResetToken> findByToken(String token);
    void delete(PasswordResetToken token);
}
