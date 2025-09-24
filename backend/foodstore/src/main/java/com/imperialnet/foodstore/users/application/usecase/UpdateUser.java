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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUser implements UpdateUserUsecase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse execute(Long id, UpdateUserRequest request, String updatedBy) {
        // 1. Buscar el usuario actual
        User user = userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        // 2. Validar que el email no pertenezca a otro usuario
        userRepositoryPort.findByEmail(request.getEmail())
                .filter(existing -> !existing.getId().equals(id)) // 👈 si es otro usuario con el mismo email
                .ifPresent(existing -> {
                    throw new BusinessException("El email ya está registrado en otro usuario");
                });
        // 3. Actualizar los campos del usuario
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


        // 4. Persistir cambios
        User updated = userRepositoryPort.save(user);

        // 5. Retornar DTO
        return UserMapper.toResponse(updated);

    }

    @Override
    public void desactivate(Long id, String updatedBy) {
        // 1. Buscar el usuario actual
        User user = userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        user.deactivate(updatedBy);
        userRepositoryPort.save(user);
    }

    @Override
    public void activate(Long id, String updatedBy) {
        // 1. Buscar el usuario actual
        User user = userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        user.activate(updatedBy);
        userRepositoryPort.save(user);
    }

    @Override
    public void changePassword(Long id, String newPassword, String updatedBy) {
        // 1. Buscar el usuario actual
        User user = userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        user.validatePasswordComplexity(newPassword);
        // 2. Validación de reglas de negocio (dominio)
        user.validatePasswordComplexity(newPassword);

        // 3. Hash en infraestructura
        String encodedPassword = passwordEncoder.encode(newPassword);

        // 4. Cambio efectivo en el dominio
        user.changePasswordHash(encodedPassword, updatedBy);

        userRepositoryPort.save(user);
    }


}
