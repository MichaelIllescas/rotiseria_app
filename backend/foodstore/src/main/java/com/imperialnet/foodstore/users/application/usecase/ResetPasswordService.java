package com.imperialnet.foodstore.users.application.usecase;

import com.imperialnet.foodstore.users.application.ports.in.ResetPasswordUseCase;
import com.imperialnet.foodstore.users.application.ports.out.PasswordResetTokenRepositoryPort;
import com.imperialnet.foodstore.users.application.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ResetPasswordService implements ResetPasswordUseCase {

    private final PasswordResetTokenRepositoryPort tokenRepository;
    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void execute(String token, String newPassword) {
        MDC.put("action", "RESET_PASSWORD");
        try {
            var resetToken = tokenRepository.findByToken(token)
                    .orElseThrow(() -> {
                        log.warn("Intento de reset con token inválido: {}", token);
                        return new IllegalArgumentException("Invalid token");
                    });

            if (resetToken.isExpired()) {
                log.warn("Intento de reset con token expirado: {}", token);
                throw new IllegalArgumentException("Token expired");
            }

            var user = userRepository.findByEmail(resetToken.getEmail())
                    .orElseThrow(() -> {
                        log.warn("No se encontró usuario asociado al token {}", token);
                        return new IllegalArgumentException("User not found");
                    });

            try {
                user.validatePasswordComplexity(newPassword);
            } catch (IllegalArgumentException ex) {
                log.warn("Nueva contraseña no cumple complejidad para usuario {}", user.getEmail());
                throw ex;
            }

            user.changePasswordHash(passwordEncoder.encode(newPassword), "SYSTEM-EMAIL");
            userRepository.save(user);

            tokenRepository.delete(resetToken);

        } catch (Exception ex) {
            if (!(ex instanceof IllegalArgumentException)) {
                log.error("Error inesperado al resetear contraseña con token={}", token, ex);
            }
            throw ex; // lo captura el ControllerAdvice
        } finally {
            MDC.clear();
        }
    }
}
