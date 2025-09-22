package com.imperialnet.foodstore.users.application.usecase;

import com.imperialnet.foodstore.users.application.ports.in.CreateUserUsecase;
import com.imperialnet.foodstore.users.application.ports.out.UserRepositoryPort;
import com.imperialnet.foodstore.users.domain.model.User;
import com.imperialnet.foodstore.users.infrastructure.mapper.UserDtoMapper;
import com.imperialnet.foodstore.users.infrastructure.mapper.UserMapper;
import com.imperialnet.foodstore.users.infrastructure.web.dto.CreateUserRequest;
import com.imperialnet.foodstore.users.infrastructure.web.dto.CreateUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUser implements CreateUserUsecase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CreateUserResponse execute(CreateUserRequest req, String createdBy) {
        userRepository.findByEmail(req.email()).ifPresent(user -> {
            throw new IllegalArgumentException("El email ya está en uso");
        });

        User user = UserDtoMapper.toDomain(req, createdBy , passwordEncoder.encode(req.password()));
        var savedUser = userRepository.save(user);
        return UserDtoMapper.toResponse(savedUser);

    }
}
