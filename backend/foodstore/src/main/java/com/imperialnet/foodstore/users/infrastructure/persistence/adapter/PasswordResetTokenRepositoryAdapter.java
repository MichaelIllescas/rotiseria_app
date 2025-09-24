package com.imperialnet.foodstore.users.infrastructure.persistence.adapter;


import com.imperialnet.foodstore.users.application.ports.out.PasswordResetTokenRepositoryPort;
import com.imperialnet.foodstore.users.domain.model.PasswordResetToken;
import com.imperialnet.foodstore.users.infrastructure.mapper.PasswordResetTokenMapper;
import com.imperialnet.foodstore.users.infrastructure.persistence.repository.PasswordResetTokenJpaRepository;
import com.imperialnet.foodstore.users.infrastructure.persistence.repository.UserJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
@RequiredArgsConstructor
@Component
public class PasswordResetTokenRepositoryAdapter implements PasswordResetTokenRepositoryPort {

    private final PasswordResetTokenJpaRepository jpaRepository;
    private final UserJPARepository userJpaRepository;


    @Override
    public void save(PasswordResetToken token, Long userId) {
        var userEntity = userJpaRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        jpaRepository.save(PasswordResetTokenMapper.toEntity(token, userEntity));
    }

    @Override
    public Optional<PasswordResetToken> findByToken(String token) {
        return jpaRepository.findByToken(token)
                .map(PasswordResetTokenMapper::toDomain);
    }

    @Override
    public void delete(PasswordResetToken token) {
        jpaRepository.deleteById(token.getToken());
    }
}
