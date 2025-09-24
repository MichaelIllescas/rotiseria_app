package com.imperialnet.foodstore.users.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Aggregate root: User (dominio puro, sin anotaciones JPA).
 */
public class User {

    // Identidad: puede ser null mientras el agregado está "transitorio"
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String passwordHash;
    private Role role;
    private boolean active;

    // Auditoría (pueden ser manejadas por dominio o por la capa de persistencia)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy; // nombre y apellido
    private String updatedBy; // nombre y apellido

    // ---- Constructores privados: usá fábricas estáticas para mayor claridad ----
    private User(Long id,
                 String name,
                 String lastname,
                 String email,
                 String passwordHash,
                 Role role,
                 boolean active,
                 LocalDateTime createdAt,
                 LocalDateTime updatedAt,
                 String createdBy,
                 String updatedBy) {
        validateEmail(email);
        this.id = id; // puede venir null si es "nuevo"
        this.name = Objects.requireNonNull(name);
        this.lastname = Objects.requireNonNull(lastname);
        this.email = Objects.requireNonNull(email);
        this.passwordHash = Objects.requireNonNull(passwordHash);
        this.role = Objects.requireNonNull(role);
        this.active = active;

        this.createdAt  = createdAt;
        this.updatedAt  = updatedAt;
        this.createdBy  = createdBy;
        this.updatedBy  = updatedBy;
    }

    /** Fábrica para crear un agregado NUEVO (aún sin id).
     *  Si preferís que dominio maneje las fechas, las setea acá.
     */
    public static User createNew(String name, String lastname, String email, String passwordHash, Role role, String createdBy) {
        LocalDateTime now = LocalDateTime.now();
        validateEmail(email);
        return new User(
                null,                     // id aún no asignado
                name, lastname, email, passwordHash,
                role,
                true,                     // activo por defecto
                now, now,
                Objects.requireNonNull(createdBy),
                createdBy
        );
    }

    /** Fábrica para REHIDRATAR desde persistencia (DB → dominio). */
    public static User rehydrate(Long id,
                                 String name,
                                 String lastname,
                                 String email,
                                 String passwordHash,
                                 Role role,
                                 boolean active,
                                 LocalDateTime createdAt,
                                 LocalDateTime updatedAt,
                                 String createdBy,
                                 String updatedBy) {

        return new User(id, name, lastname, email, passwordHash, role, active, createdAt, updatedAt, createdBy, updatedBy);
    }

    // ---- Reglas de negocio ----
    public void deactivate(String updater) {
        if (!this.active) throw new IllegalStateException("User is already inactive");
        this.active = false;
        touch(updater);
    }

    public void activate(String updater) {
        if (this.active) throw new IllegalStateException("User is already active");
        this.active = true;
        touch(updater);
    }

    public void changeRole(Role newRole, String updater) {
        this.role = Objects.requireNonNull(newRole);
        touch(updater);
    }

    public void changeEmail(String newEmail, String updater) {
        if (newEmail == null || !newEmail.contains("@")) throw new IllegalArgumentException("Invalid email");
        this.email = newEmail;
        touch(updater);
    }
    public static void validateEmail(String email) {
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Invalid email");
    }

    public void changeName(String newName, String updater) {
        this.name = Objects.requireNonNull(newName);
        touch(updater);
    }
    public void changeLastname(String newLastname, String updater) {
        this.lastname = Objects.requireNonNull(newLastname, "Lastname cannot be null");
        touch(updater);
    }

    private void touch(String updater) {
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = Objects.requireNonNull(updater);
    }

    /** Método pensado para el repositorio al guardar por primera vez.
     *  Asegura inmutabilidad de identidad una vez asignada.
     */
    public void assignIdOnce(Long generatedId) {
        if (this.id != null) throw new IllegalStateException("Id already assigned");
        this.id = Objects.requireNonNull(generatedId);
    }

    // ---- Getters (no expongo setters "libres" para no romper invariantes) ----
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getLastname() { return lastname; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }
    public boolean isActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public String getCreatedBy() { return createdBy; }
    public String getUpdatedBy() { return updatedBy; }

    /**
     * Valida los requisitos mínimos de complejidad de una contraseña.
     *
     * @param rawPassword contraseña en texto plano
     * @throws IllegalArgumentException si no cumple complejidad
     */
    public static void validatePasswordComplexity(String rawPassword) {
        Objects.requireNonNull(rawPassword, "La contraseña no puede ser nula");

        String passwordPattern =
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._-])[A-Za-z\\d@$!%*?&._-]{8,}$";

        if (!rawPassword.matches(passwordPattern)) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos 8 caracteres, una mayúscula, " +
                            "una minúscula, un número y un caracter especial"
            );
        }
    }

    /**
     * Cambia la contraseña del usuario asignando directamente el hash.
     */
    public void changePasswordHash(String encodedPassword, String updatedBy) {
        this.passwordHash = Objects.requireNonNull(encodedPassword, "El hash no puede ser nulo");
        touch(updatedBy);
    }


}
