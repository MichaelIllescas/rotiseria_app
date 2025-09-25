package com.imperialnet.foodstore.config.security;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Slf4j
public final class SecurityLogger {

    private SecurityLogger() {
        // Utility class
    }

    public static void logLoginSuccess(String username) {
        MDC.put("action", "LOGIN");
        MDC.put("username", username);
        log.info("Login successful for user {}", username);
        MDC.clear();
    }

    public static void logLoginFailure(String username, String reason) {
        MDC.put("action", "LOGIN");
        MDC.put("username", username);
        log.warn("Login failed for user {}: {}", username, reason);
        MDC.clear();
    }

    public static void logLogout(String username) {
        MDC.put("action", "LOGOUT");
        MDC.put("username", username);
        log.info("Logout successful for user {}", username);
        MDC.clear();
    }

    public static void logAccessDenied(String username, String resource) {
        MDC.put("action", "ACCESS_DENIED");
        MDC.put("username", username);
        log.warn("Access denied for user {} on resource {}", username, resource);
        MDC.clear();
    }

    public static void logRoleChange(Long userId, String performedBy, String oldRole, String newRole) {
        MDC.put("action", "CHANGE_ROLE");
        MDC.put("userId", String.valueOf(userId));
        MDC.put("username", performedBy);
        log.info("Role changed for userId={} from {} to {} by {}", userId, oldRole, newRole, performedBy);
        MDC.clear();
    }

    public static void logPasswordReset(String username) {
        MDC.put("action", "RESET_PASSWORD");
        MDC.put("username", username);
        log.info("Password reset successfully for user {}", username);
        MDC.clear();
    }

    public static void logPasswordResetFailure(String username, String reason) {
        MDC.put("action", "RESET_PASSWORD");
        MDC.put("username", username);
        log.warn("Password reset failed for user {}: {}", username, reason);
        MDC.clear();
    }
}
