package com.imperialnet.foodstore.users.infrastructure.web.dto;

import com.imperialnet.foodstore.users.domain.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Datos para actualizar un usuario existente")
public class UpdateUserRequest {

    @Schema(example = "Juan", description = "Nombre del usuario")
    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @Schema(example = "Pérez", description = "Apellido del usuario")
    @NotBlank(message = "El apellido no puede estar vacío")
    private String lastname;

    @Schema(example = "juan.perez@mail.com", description = "Correo electrónico único del usuario")
    @Email(message = "El correo electrónico no es válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @Schema(
            description = "Rol del usuario dentro del sistema. Valores permitidos: DUENO, ENCARGADO, ATENCION",
            example = "ENCARGADO"
    )
    @NotNull(message = "El rol es obligatorio")
    private Role role;

    @Schema(example = "true", description = "Indica si el usuario está activo o no")
    private boolean active;


}
