package com.imperialnet.foodstore.users.application.usecase;

import com.imperialnet.foodstore.users.application.ports.in.UpdateUserUsecase;
import com.imperialnet.foodstore.users.application.ports.out.UserRepositoryPort;
import com.imperialnet.foodstore.users.domain.exception.BusinessException;
import com.imperialnet.foodstore.users.domain.exception.UserNotFoundException;
import com.imperialnet.foodstore.users.domain.model.User;
import com.imperialnet.foodstore.users.infrastructure.mapper.UserMapper;
import com.imperialnet.foodstore.users.infrastructure.web.dto.UpdateUserRequest;
import com.imperialnet.foodstore.users.infrastructure.web.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateUser implements UpdateUserUsecase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse execute(Long id, UpdateUserRequest request, String updatedBy) {
        MDC.put("action", "UPDATE_USER");
        try {
            User user = userRepositoryPort.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Intento de actualizar usuario inexistente id={}", id);
                        return new UserNotFoundException("Usuario no encontrado");
                    });

            userRepositoryPort.findByEmail(request.getEmail())
                    .filter(existing -> !existing.getId().equals(id))
                    .ifPresent(existing -> {
                        log.warn("Email {} ya registrado en otro usuario (id={})", request.getEmail(), existing.getId());
                        throw new BusinessException("El email ya está registrado en otro usuario");
                    });

            user.changeName(request.getName(), updatedBy);
            user.changeLastname(request.getLastname(), updatedBy);
            user.changeEmail(request.getEmail(), updatedBy);
            user.changeRole(request.getRole(), updatedBy);

            if (request.isActive() != user.isActive()) {
                if (request.isActive()) {
                    user.activate(updatedBy);
                } else {
                    user.deactivate(updatedBy);
                }
            }

            User updated = userRepositoryPort.save(user);
            return UserMapper.toResponse(updated);

        } catch (UserNotFoundException | BusinessException ex) {
            throw ex; // lo captura el ControllerAdvice → 404 o 400
        } catch (Exception ex) {
            log.error("Error inesperado al actualizar usuario id={}", id, ex);
            throw ex;
        } finally {
            MDC.clear();
        }
    }

    @Override
    public void desactivate(Long id, String updatedBy) {
        MDC.put("action", "DEACTIVATE_USER");
        try {
            User user = userRepositoryPort.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Intento de desactivar usuario inexistente id={}", id);
                        return new UserNotFoundException("Usuario no encontrado");
                    });
            user.deactivate(updatedBy);
            userRepositoryPort.save(user);
        } finally {
            MDC.clear();
        }
    }

    @Override
    public void activate(Long id, String updatedBy) {
        MDC.put("action", "ACTIVATE_USER");
        try {
            User user = userRepositoryPort.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Intento de activar usuario inexistente id={}", id);
                        return new UserNotFoundException("Usuario no encontrado");
                    });
            user.activate(updatedBy);
            userRepositoryPort.save(user);
        } finally {
            MDC.clear();
        }
    }

    @Override
    public void changePassword(Long id, String newPassword, String updatedBy) {
        MDC.put("action", "CHANGE_PASSWORD");
        try {
            User user = userRepositoryPort.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Intento de cambiar contraseña de usuario inexistente id={}", id);
                        return new UserNotFoundException("Usuario no encontrado");
                    });

            try {
                user.validatePasswordComplexity(newPassword);
            } catch (IllegalArgumentException ex) {
                log.warn("Password inválida para usuario id={}: {}", id, ex.getMessage());
                throw ex;
            }

            String encodedPassword = passwordEncoder.encode(newPassword);
            user.changePasswordHash(encodedPassword, updatedBy);

            userRepositoryPort.save(user);
        } finally {
            MDC.clear();
        }
    }
}
