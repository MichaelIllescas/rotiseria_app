package com.imperialnet.foodstore.users.infrastructure.web;


import com.imperialnet.foodstore.users.application.ports.in.CreateUserUsecase;
import com.imperialnet.foodstore.users.application.ports.in.GetAllUsersUsecase;
import com.imperialnet.foodstore.users.application.ports.in.GetUserByIdUsecase;
import com.imperialnet.foodstore.users.application.ports.in.UpdateUserUsecase;
import com.imperialnet.foodstore.users.infrastructure.security.CustomUserDetails;
import com.imperialnet.foodstore.users.infrastructure.web.dto.CreateUserRequest;
import com.imperialnet.foodstore.users.infrastructure.web.dto.CreateUserResponse;
import com.imperialnet.foodstore.users.infrastructure.web.dto.UpdateUserRequest;
import com.imperialnet.foodstore.users.infrastructure.web.dto.UserResponse;
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

    // --- Endpoint para actualizar ---
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



}
