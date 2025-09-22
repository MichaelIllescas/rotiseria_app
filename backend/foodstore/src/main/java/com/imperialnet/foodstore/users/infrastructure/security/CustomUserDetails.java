package com.imperialnet.foodstore.users.infrastructure.security;

import com.imperialnet.foodstore.users.domain.model.Role;
import com.imperialnet.foodstore.users.infrastructure.persistence.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Custom implementation of Spring Security's UserDetails.
 * Adapts our UserEntity to the UserDetails interface.
 */
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String name;
    private final String lastname;
    private final String email;
    private final String passwordHash;
    private final Role role;
    private final boolean active;

    public CustomUserDetails(Long id, String name, String lastname, String email, String passwordHash, Role role, boolean active) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.active = active;
    }

    /**
     * Factory method to build CustomUserDetails from UserEntity.
     */
    public static CustomUserDetails fromEntity(UserEntity userEntity) {
        return new CustomUserDetails(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getLastname(),
                userEntity.getEmail(),
                userEntity.getPasswordHash(),
                userEntity.getRole(),
                userEntity.isActive()
        );
    }

    // ---- Métodos adicionales propios ----

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return name + lastname ; // si después separás nombre/apellido, podés concatenarlos acá
    }

    public Role getRoleEnum() {
        return role;
    }

    // ---- Métodos de UserDetails ----

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email; // usamos email como identificador
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // podés agregar lógica si manejás vencimiento de cuentas
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // podés agregar lógica si hay bloqueo de cuentas
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // podés agregar lógica si expiran las credenciales
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    // ---- equals & hashCode para comparación segura ----

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomUserDetails that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
