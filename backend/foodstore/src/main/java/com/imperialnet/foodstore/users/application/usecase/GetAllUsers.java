package com.imperialnet.foodstore.users.application.usecase;

import com.imperialnet.foodstore.users.application.ports.in.GetAllUsersUsecase;
import com.imperialnet.foodstore.users.application.ports.out.UserRepositoryPort;
import com.imperialnet.foodstore.users.infrastructure.mapper.UserMapper;
import com.imperialnet.foodstore.users.infrastructure.web.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetAllUsers implements GetAllUsersUsecase {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public List<UserResponse> execute() {
        MDC.put("action", "GET_ALL_USERS");
        try {
            return userRepositoryPort.findAll()
                    .stream()
                    .map(UserMapper::toResponse)
                    .toList();
        } catch (Exception ex) {
            log.error("Error inesperado al obtener todos los usuarios", ex);
            throw ex; // lo maneja el GlobalExceptionHandler → 500
        } finally {
            MDC.clear();
        }
    }
}
