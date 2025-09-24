package com.imperialnet.foodstore.users.application.usecase;

import com.imperialnet.foodstore.users.application.ports.in.ResetPasswordUseCase;
import com.imperialnet.foodstore.users.application.ports.out.PasswordResetTokenRepositoryPort;
import com.imperialnet.foodstore.users.application.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ResetPasswordService implements ResetPasswordUseCase {

    private final PasswordResetTokenRepositoryPort tokenRepository;
    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void execute(String token, String newPassword) {
        var resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (resetToken.isExpired()) {
            throw new IllegalArgumentException("Token expired");
        }

        var user = userRepository.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.validatePasswordComplexity(newPassword);
        user.changePasswordHash(passwordEncoder.encode(newPassword), "SYSTEM-EMAIL");

        userRepository.save(user);

        tokenRepository.delete(resetToken);
    }
}
