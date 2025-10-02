package com.imperialnet.foodstore.business.domain.model;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Entidad que representa el negocio (rotisería) dueño de la aplicación.
 * Contiene los datos públicos y de contacto que pueden mostrarse al cliente
 * o utilizarse en notificaciones (ej. confirmaciones de pedido).
 */
public class Business {

    private final Long id;
    private String name;
    private String description;
    private String email;
    private String phone;
    private String address;
    private boolean active;

    // ==== Constructor con validaciones de invariantes ====
    public Business(Long id,
                    String name,
                    String description,
                    String email,
                    String phone,
                    String address,
                    boolean active) {

        validateName(name);
        validateEmail(email);
        validatePhone(phone);
        validateAddress(address);

        this.id = id;
        this.name = name.trim();
        this.description = (description != null) ? description.trim() : null;
        this.email = email.trim().toLowerCase();
        this.phone = phone.trim();
        this.address = address.trim();
        this.active = active;
    }

    // ==== Métodos de negocio (invariantes y comportamiento) ====

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del negocio es obligatorio.");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("El nombre del negocio no puede superar los 100 caracteres.");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email de contacto es obligatorio.");
        }
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (!Pattern.matches(regex, email)) {
            throw new IllegalArgumentException("El email de contacto no tiene un formato válido.");
        }
    }

    private void validatePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("El teléfono es obligatorio.");
        }
        if (phone.length() < 6 || phone.length() > 20) {
            throw new IllegalArgumentException("El teléfono debe tener entre 6 y 20 caracteres.");
        }
    }

    private void validateAddress(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("La dirección es obligatoria.");
        }
    }

    // ==== Métodos públicos de actualización con validación interna ====
    public void updateName(String name) {
        validateName(name);
        this.name = name.trim();
    }

    public void updateDescription(String description) {
        this.description = (description != null) ? description.trim() : null;
    }

    public void updateEmail(String email) {
        validateEmail(email);
        this.email = email.trim().toLowerCase();
    }

    public void updatePhone(String phone) {
        validatePhone(phone);
        this.phone = phone.trim();
    }

    public void updateAddress(String address) {
        validateAddress(address);
        this.address = address.trim();
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    // ==== Getters (entidad inmutable hacia afuera en sus campos base) ====
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public boolean isActive() { return active; }

    @Override
    public String toString() {
        return "Business{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", active=" + active +
                '}';
    }
}
