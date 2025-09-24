package com.imperialnet.foodstore.users.application.usecase;

import com.imperialnet.foodstore.users.application.ports.in.GetUserByIdUsecase;
import com.imperialnet.foodstore.users.application.ports.out.UserRepositoryPort;
import com.imperialnet.foodstore.users.domain.exception.UserNotFoundException;
import com.imperialnet.foodstore.users.domain.model.User;
import com.imperialnet.foodstore.users.infrastructure.mapper.UserMapper;
import com.imperialnet.foodstore.users.infrastructure.web.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserById implements GetUserByIdUsecase {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public UserResponse execute(Long id) {
        User userEntity =userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Ususario no encontrado con id: " + id));
        return UserMapper.toResponse(userEntity);
    }
}
