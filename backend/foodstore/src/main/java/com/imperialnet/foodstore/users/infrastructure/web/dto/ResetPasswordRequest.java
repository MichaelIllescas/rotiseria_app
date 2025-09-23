// ResetPasswordRequest.java
package com.imperialnet.foodstore.users.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @NotBlank String token,
        @NotBlank String newPassword
) {}
