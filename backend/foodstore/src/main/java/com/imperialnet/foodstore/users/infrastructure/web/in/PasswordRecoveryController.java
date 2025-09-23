package com.imperialnet.foodstore.users.infrastructure.web.in;


import com.imperialnet.foodstore.users.application.ports.in.ForgotPasswordUseCase;
import com.imperialnet.foodstore.users.application.ports.in.ResetPasswordUseCase;
import com.imperialnet.foodstore.users.infrastructure.web.dto.ForgotPasswordRequest;
import com.imperialnet.foodstore.users.infrastructure.web.dto.ResetPasswordRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name = "Recuperación de Contraseña", description = "Operaciones relacionadas con la recuperación y restablecimiento de contraseñas")
public class PasswordRecoveryController {

    private final ForgotPasswordUseCase forgotPasswordUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;

    @Operation (
            summary = "Iniciar el proceso de recuperación de contraseña",
            description = "Permite a un usuario iniciar el proceso de recuperación de contraseña proporcionando su correo electrónico."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody (
            description = "Datos necesarios para iniciar la recuperación de contraseña",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ForgotPasswordRequest.class))
    )
    @ApiResponse
    (
            responseCode = "200",
            description = "Instrucciones de recuperación enviadas al correo electrónico si el usuario existe"
    )

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/forgot-password")
    public void forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        forgotPasswordUseCase.execute(request.email());
    }

    //documentacion para swagger
    @Operation (
            summary = "Restablecer la contraseña del usuario",
            description = "Permite a un usuario restablecer su contraseña utilizando un token de restablecimiento válido."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody (
            description = "Datos necesarios para restablecer la contraseña",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ResetPasswordRequest.class))
    )
    @ApiResponse (
            responseCode = "200",
            description = "Contraseña restablecida exitosamente"
    )
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reset-password")
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        resetPasswordUseCase.execute(request.token(), request.newPassword());
    }
}
