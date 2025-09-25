package com.imperialnet.foodstore.users.application.usecase;

import com.imperialnet.foodstore.users.application.ports.in.CreateUserUsecase;
import com.imperialnet.foodstore.users.application.ports.out.UserRepositoryPort;
import com.imperialnet.foodstore.users.domain.model.User;
import com.imperialnet.foodstore.users.infrastructure.mapper.UserDtoMapper;
import com.imperialnet.foodstore.users.infrastructure.mapper.UserMapper;
import com.imperialnet.foodstore.users.infrastructure.web.dto.CreateUserRequest;
import com.imperialnet.foodstore.users.infrastructure.web.dto.CreateUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateUser implements CreateUserUsecase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CreateUserResponse execute(CreateUserRequest req, String createdBy) {

        MDC.put("action", "CREATE_USER");
        userRepository.findByEmail(req.email()).ifPresent(user -> {
            log.warn("El email {} ya está en uso", req.email());
            throw new IllegalArgumentException("El email ya está en uso");
        });
        try {
            User.validatePasswordComplexity(req.password());
        } catch (IllegalArgumentException ex) {
            log.warn("Error de validación de password: {}", ex.getMessage());
            throw ex; // lo captura el ControllerAdvice y devuelve 400
        }
        User user = UserDtoMapper.toDomain(req, createdBy , passwordEncoder.encode(req.password()));
        var savedUser = userRepository.save(user);

        MDC.clear();

        return UserDtoMapper.toResponse(savedUser);

    }
}
