package com.imperialnet.foodstore.users.application.usecase;


import com.imperialnet.foodstore.users.application.ports.in.ForgotPasswordUseCase;
import com.imperialnet.foodstore.users.application.ports.out.EmailServicePort;
import com.imperialnet.foodstore.users.application.ports.out.UserRepositoryPort;
import com.imperialnet.foodstore.users.application.ports.out.PasswordResetTokenRepositoryPort;
import com.imperialnet.foodstore.users.domain.event.EmailEvent;
import com.imperialnet.foodstore.users.domain.model.PasswordResetToken;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ForgotPasswordService implements ForgotPasswordUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordResetTokenRepositoryPort tokenRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ForgotPasswordService(UserRepositoryPort userRepository,
                                 PasswordResetTokenRepositoryPort tokenRepository,
                                 ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        var token = new PasswordResetToken(user.getEmail());
        tokenRepository.save(token, user.getId());

        String link = "http://localhost:3000/reset-password?token=" + token.getToken();

        // 👇 en vez de llamar al servicio de email directamente
        eventPublisher.publishEvent(
                new EmailEvent(
                        user.getEmail(),
                        "Recuperación de contraseña",
                        "email/password-reset",
                        Map.of("resetLink", link)
                )
        );
    }
}