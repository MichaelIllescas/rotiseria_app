package com.imperialnet.foodstore.users.domain.model;


import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Aggregate root para el proceso de recuperación de contraseña.
 * Representa un token temporal asociado a un email de usuario.
 */
public class PasswordResetToken {

    private final String token;
    private final String email;
    private final LocalDateTime expiresAt;

    /**
     * Crea un nuevo token asociado a un email.
     * Expira en 30 minutos desde su creación.
     */
    public PasswordResetToken(String email) {
        this.token = UUID.randomUUID().toString();
        this.email = email;
        this.expiresAt = LocalDateTime.now().plusMinutes(30);
    }

    /**
     * Constructor usado para reconstruir el objeto desde persistencia.
     */
    public PasswordResetToken(String token, String email, LocalDateTime expiresAt) {
        this.token = token;
        this.email = email;
        this.expiresAt = expiresAt;
    }

    // Getters
    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    /**
     * Verifica si el token ya expiró.
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}
