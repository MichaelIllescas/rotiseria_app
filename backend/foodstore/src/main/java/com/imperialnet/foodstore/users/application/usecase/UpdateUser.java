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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUser implements UpdateUserUsecase {

    private final UserRepositoryPort userRepositoryPort;

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
        if (request.isActive()) {
            user.activate(updatedBy);
        } else {
            user.deactivate(updatedBy);
        }

        // 4. Persistir cambios
        User updated = userRepositoryPort.save(user);

        // 5. Retornar DTO
        return UserMapper.toResponse(updated);

    }
}
