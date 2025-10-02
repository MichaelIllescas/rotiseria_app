package com.imperialnet.foodstore.business.infrastructure.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO de entrada para crear/actualizar datos del negocio.
 * Se valida con anotaciones de Jakarta Validation.
 */
@Data
public class BusinessRequest {

    @NotBlank(message = "El nombre del negocio es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String name;

    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String description;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    @Size(max = 100, message = "El email no puede superar los 100 caracteres")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(min = 6, max = 20, message = "El teléfono debe tener entre 6 y 20 caracteres")
    private String phone;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 255, message = "La dirección no puede superar los 255 caracteres")
    private String address;

    private boolean active;
}
