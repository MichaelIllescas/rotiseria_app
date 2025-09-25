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
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
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
    public void forgotPassword(@Valid @RequestBody ForgotPasswordRequest request)
    {
        MDC.put("action", "FORGOT_PASSWORD");
        log.info("Solicitud recibida de recuperación de contraseña para el email: {}", request.email());
        try {
            forgotPasswordUseCase.execute(request.email());
            log.info("Instrucciones de recuperación enviadas al correo electrónico: {}", request.email());
        } catch (Exception e)
        {
            log.warn("Error al procesar la solicitud de recuperación de contraseña para el email {}: {}", request.email(), e.getMessage());
            // No se lanza la excepción para evitar revelar si el email existe o no
        }
        finally {
            MDC.remove("action");
        }
    }

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
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest request)
    {
        String tokenPreview = request.token().substring(0, Math.min(6, request.token().length())) + "...";
        MDC.put("action", "RESET_PASSWORD");
        log.info("Solicitud recibida para restablecer la contraseña con el token: {}", tokenPreview);
        try {
            resetPasswordUseCase.execute(request.token(), request.newPassword());
            log.info("Contraseña restablecida exitosamente para el token: {}", tokenPreview);
        } catch (Exception e)
        {
            log.error("Error al restablecer la contraseña con el token {}: . Causa: {}", tokenPreview, e.getMessage());
            throw e;
        } finally {
            MDC.remove("action");
        }
    }
}
