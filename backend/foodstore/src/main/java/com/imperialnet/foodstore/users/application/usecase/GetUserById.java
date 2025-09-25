package com.imperialnet.foodstore.users.application.usecase;

import com.imperialnet.foodstore.users.application.ports.in.GetUserByIdUsecase;
import com.imperialnet.foodstore.users.application.ports.out.UserRepositoryPort;
import com.imperialnet.foodstore.users.domain.exception.UserNotFoundException;
import com.imperialnet.foodstore.users.domain.model.User;
import com.imperialnet.foodstore.users.infrastructure.mapper.UserMapper;
import com.imperialnet.foodstore.users.infrastructure.web.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetUserById implements GetUserByIdUsecase {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public UserResponse execute(Long id) {
        MDC.put("action", "GET_USER_BY_ID");
        try {
            User userEntity = userRepositoryPort.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Usuario no encontrado con id={}", id);
                        return new UserNotFoundException("Usuario no encontrado con id: " + id);
                    });

            return UserMapper.toResponse(userEntity);
        } catch (UserNotFoundException ex) {
            throw ex; // lo captura el ControllerAdvice → 404
        } catch (Exception ex) {
            log.error("Error inesperado al obtener usuario con id={}", id, ex);
            throw ex; // lo captura el ControllerAdvice → 500
        } finally {
            MDC.clear();
        }
    }
}
