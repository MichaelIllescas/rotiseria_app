package com.imperialnet.foodstore.users.application.usecase;

import com.imperialnet.foodstore.users.application.ports.in.DeleteUserUseCase;
import com.imperialnet.foodstore.users.infrastructure.persistence.adapter.UserRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeleteUser implements DeleteUserUseCase {

    private final UserRepositoryAdapter userRepositoryAdapter;


    @Override
    public void execute(Long userId) {
        MDC.put("action", "DELETE_USER");
        try {
            userRepositoryAdapter.deleteById(userId);
        } catch (Exception ex) {
            log.warn("Intento de eliminar usuario inexistente con id={}", userId);
            throw ex; // lo captura el ControllerAdvice y devuelve 404
        } finally {
            MDC.clear();
        }
    }
}
