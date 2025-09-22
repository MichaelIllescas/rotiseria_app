package com.imperialnet.foodstore.users.application.usecase;

import com.imperialnet.foodstore.users.application.ports.in.GetAllUsersUsecase;
import com.imperialnet.foodstore.users.application.ports.out.UserRepositoryPort;
import com.imperialnet.foodstore.users.infrastructure.mapper.UserDtoMapper;
import com.imperialnet.foodstore.users.infrastructure.mapper.UserMapper;
import com.imperialnet.foodstore.users.infrastructure.web.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetAllUsers implements GetAllUsersUsecase {
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public List<UserResponse> execute() {
        return userRepositoryPort.findAll().stream()
                .map(UserMapper::toResponse).toList();
    }
}
