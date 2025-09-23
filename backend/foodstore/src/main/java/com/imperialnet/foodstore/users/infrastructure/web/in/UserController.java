package com.imperialnet.foodstore.users.infrastructure.web.in;


import com.imperialnet.foodstore.users.application.ports.in.*;
import com.imperialnet.foodstore.users.infrastructure.security.CustomUserDetails;
import com.imperialnet.foodstore.users.infrastructure.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Operaciones relacionadas con la gestión de usuarios")
public class UserController {

    private final CreateUserUsecase createUserUsecase;
    private final GetAllUsersUsecase getAllUsersUsecase;
    private final GetUserByIdUsecase getUserByIdUsecase;
    private final UpdateUserUsecase updateUserUsecase;
    private final DeleteUserUseCase deleteUserUseCase;

    // --- Endpoint para crear usuario ---
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("register")
    @Operation(
            summary = "Registrar a un nuevo usuario",
            description = "Registra a un nuevo usuario en el sistema. Requiere autenticación."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Usuario creado exitosamente",
            content = @Content(schema = @Schema(implementation = CreateUserResponse.class))
    )
    public CreateUserResponse createUser( @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                      description = "Datos necesarios para crear un usuario",
                                                      required = true,
                                                      content = @Content(schema = @Schema(implementation = CreateUserRequest.class))
                                              ) @Valid @RequestBody CreateUserRequest req,
                                         Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String fullName = userDetails.getFullName();
        return createUserUsecase.execute(req, fullName);
    }

    // --- Endpoint para obtener todos ---
    @Operation(
            summary = "Obtener todos los usuarios",
            description = "Obtiene a todos los usuarios registrados en el sistema. Requiere autenticación."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de usuarios obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = UserResponse.class))
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("getAll")
    public List<UserResponse> getAllUsers() {
        return getAllUsersUsecase.execute();
    }

    // --- Endpoint para obtener por id ---
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getUser/{id}")
    @Operation(
            summary = "Obtener un usuario por ID",
            description = "Obtiene los detalles de un usuario específico mediante su ID. Requiere autenticación."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Usuario obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = UserResponse.class))
    )
    public UserResponse getUserById(@PathVariable Long id) {
        return getUserByIdUsecase.execute(id);
    }

    // --- Endpoint para actualizar usuario ---
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Actualizar usuario",
            description = "Permite actualizar los datos de un usuario existente. " +
                    "El campo `role` debe ser uno de: `DUENO`, `ENCARGADO`, `ATENCION`."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Usuario actualizado correctamente",
            content = @Content(schema = @Schema(implementation = UserResponse.class))
    )
    public UserResponse updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request,
            Authentication auth) {

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String updatedBy = userDetails.getFullName();

        return updateUserUsecase.execute(id, request, updatedBy);
    }

    // --- Endpoint para obtener los datos del usuario en sesion ---
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/me")
    @Operation(
            summary = "Obtener datos del usuario en sesión",
            description = "Obtiene los detalles del usuario actualmente autenticado."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Datos del usuario obtenidos exitosamente",
            content = @Content(schema = @Schema(implementation = UserResponse.class))
    )
    public UserResponse getCurrentUser(Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Long userId = userDetails.getId();
        return getUserByIdUsecase.execute(userId);
    }

    // --- Endpoint para eliminar a un usuario del sistema ---
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina un usuario del sistema mediante su ID. Requiere autenticación."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Usuario eliminado exitosamente"
    )
    public void deleteUser(@PathVariable Long id) {
        deleteUserUseCase.execute(id);
    }

    // --- Endpoint para desactivar a un usuario del sistema ---
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/deactivate/{id}")
    @Operation(
            summary = "Desactivar usuario",
            description = "Desactiva un usuario del sistema mediante su ID. Requiere autenticación."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Usuario desactivado exitosamente"
    )
    public void deactivateUser(@PathVariable Long id, Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String updatedBy = userDetails.getFullName();
        updateUserUsecase.desactivate(id, updatedBy);
    }

    // --- Endpoint para activar a un usuario del sistema ---
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/activate/{id}")
    @Operation(
            summary = "Activar usuario",
            description = "Activa un usuario del sistema mediante su ID. Requiere autenticación."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Usuario Actuvado exitosamente"
    )
    public void activateUser(@PathVariable Long id, Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String updatedBy = userDetails.getFullName();
        updateUserUsecase.activate(id, updatedBy);
    }

    // --- Endpoint cambiar contraseña ---
    @Operation (
            summary = "Cambiar la contraseña de un usuario",
            description = "Permite cambiar la contraseña de un usuario existente. Requiere autenticación."
    )
    @ApiResponse (
            responseCode = "204",
            description = "Contraseña cambiada exitosamente"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/changePassword/{id}")
    public void changePassword(@PathVariable Long id, @RequestBody ChangePasswordRequest newPassword, Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String updatedBy = userDetails.getFullName();
        updateUserUsecase.changePassword(id, newPassword.getNewPassword(), updatedBy);
    }


}
