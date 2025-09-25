package com.imperialnet.foodstore.users.infrastructure.web.in;


import com.imperialnet.foodstore.users.application.ports.in.*;
import com.imperialnet.foodstore.config.security.CustomUserDetails;
import com.imperialnet.foodstore.users.infrastructure.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
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
                                              ) @Valid @RequestBody CreateUserRequest req,Authentication auth)
    {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String fullName = userDetails.getFullName();
        MDC.put("action", "CREATE_USER");
        log.info("Usuario: {} está creando un nuevo usuario con email: {}", fullName, req.email());
        try {
             CreateUserResponse newUser= createUserUsecase.execute(req, fullName);
             log.info("Usuario: {} ha creado un nuevo usuario con ID: {} y email: {}", fullName, newUser.id(), newUser.email());
            return newUser;
        } catch (Exception e) {
            log.error("Error al crear usuario con email: {} por parte de: {} . Causa: {}",req.email(), fullName, e.getMessage());
            throw e;
        }finally {
            MDC.remove("action");
        }
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
    public List<UserResponse> getAllUsers()
    {
        MDC.put("action", "GET_ALL_USERS");
        log.debug("Obteniendo todos los usuarios del sistema");
        try{
            List<UserResponse>usersList = getAllUsersUsecase.execute();
            log.debug("Se han obtenido: {} usuarios del sistema", usersList.size());
            return usersList;
        }
        catch (Exception e){
            log.error("Error al obtener todos los usuarios. Causa: {}", e.getMessage(), e);
            throw e;
        }finally {
            MDC.remove("action");
        }
    }

    // --- Endpoint para obtener por ID ---
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
    public UserResponse getUserById(@PathVariable Long id)
    {
        MDC.put("action", "GET_USER_BY_ID");
        log.debug("Buscando usuario con id={}", id);
        try{
            UserResponse userFinded= getUserByIdUsecase.execute(id);
            log.debug("Usuario con id: {} encontrado: {}", id, userFinded.getEmail());
            return userFinded;
        } catch (Exception e){
            log.error("Error al obtener el usuario con id: {}. Causa: {}", id, e.getMessage(), e);
            throw e;
        }finally {
            MDC.remove("action");
        }
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
    public UserResponse updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request, Authentication auth)
    {

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String updatedBy = userDetails.getFullName();

        MDC.put("action", "UPDATE_USER");
        log.info("Usuario: {} está actualizando al usuario con ID: {}", updatedBy, id);
         try {
                UserResponse updatedUser = updateUserUsecase.execute(id, request, updatedBy);
                log.info("Usuario: {} ha actualizado al usuario con ID: {}", updatedBy, id);
                return updatedUser;
            } catch (Exception e) {
                log.error("Error al actualizar el usuario con ID: {} por parte de: {}. Causa: {}", id, updatedBy, e.getMessage());
                throw e;
         }finally {
            MDC.remove("action");
         }
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
    public UserResponse getCurrentUser(Authentication auth)
    {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Long userId = userDetails.getId();
        MDC.put("action", "GET_CURRENT_USER");
        log.debug("Obteniendo datos del usuario en sesión con ID: {}", userId);
        try {
            UserResponse userResponse= getUserByIdUsecase.execute(userId);
            log.debug("Usuario en sesión correctamente: {} con ID: {}", userResponse.getEmail(), userResponse.getId());
            return userResponse;
        } catch (Exception e) {
            log.error("Error al obtener los datos del usuario en sesión. Causa: {}", e.getMessage(), e);
            throw e;
        }finally {
            MDC.remove("action");
        }
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
    public void deleteUser(@PathVariable Long id)
    {
        MDC.put("action", "DELETE_USER");
        log.info("Eliminando usuario con ID: {}", id);
        try {
            deleteUserUseCase.execute(id);
            log.info("Usuario con ID: {} eliminado exitosamente", id);
        } catch (Exception e) {
            log.error("Error al eliminar el usuario con ID: {}. Causa: {}", id, e.getMessage());
            throw e;
        } finally {
            MDC.remove("action");
        }
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
    public void deactivateUser(@PathVariable Long id, Authentication auth)
    {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String updatedBy = userDetails.getFullName();
        MDC.put("action", "DEACTIVATE_USER");
        log.info("EL usuario: {} , esta intentando desactivar al usuario con ID: {}",updatedBy, id);
        try {
            updateUserUsecase.desactivate(id, updatedBy);
            log.info("Usuario con ID: {} desactivado exitosamente por: {}", id, updatedBy);
        } catch (Exception e) {
            log.error("Error al desactivar el usuario con ID: {} por {}. Causa: {}", id,updatedBy ,e.getMessage());
            throw e;
        }finally {
            MDC.remove("action");
        }
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
    public void activateUser(@PathVariable Long id, Authentication auth)
    {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String updatedBy = userDetails.getFullName();
        MDC.put("action", "ACTIVATE_USER");
        log.info("EL usuario: {} , esta intentando activar al usuario con ID: {}",updatedBy, id);
        try {
            updateUserUsecase.activate(id, updatedBy);
            log.info("Usuario con ID: {} activado exitosamente por: {}", id, updatedBy);
        } catch (Exception e) {
            log.error("Error al activar el usuario con ID: {} por {}. Causa: {}", id,updatedBy,e.getMessage());
            throw e;
        }finally {
            MDC.remove("action");
        }
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
    public void changePassword(@PathVariable Long id, @RequestBody ChangePasswordRequest newPassword, Authentication auth)
    {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String updatedBy = userDetails.getFullName();
        MDC.put("action", "CHANGE_PASSWORD");
        log.info("El usuario: {}, esta intentando cambiar la contraseña del usuario con ID: {}", updatedBy, id);
        try {
            updateUserUsecase.changePassword(id, newPassword.getNewPassword(), updatedBy);
            log.info("El usuario: {}, ha cambiado la contraseña del usuario con ID: {}", updatedBy, id);
        } catch (Exception e) {
            log.error("Error al cambiar la contraseña del usuario con ID: {} por {}. Causa: {}", id, updatedBy, e.getMessage());
            throw e;
        }finally {
            MDC.remove("action");
        }
    }
}
