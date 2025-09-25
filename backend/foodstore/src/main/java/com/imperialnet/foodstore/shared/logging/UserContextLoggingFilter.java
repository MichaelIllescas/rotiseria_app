package com.imperialnet.foodstore.shared.logging;


import com.imperialnet.foodstore.config.security.CustomUserDetails;
import jakarta.servlet.*;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(2) // después del RequestIdFilter
public class UserContextLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof CustomUserDetails userDetails) {
                MDC.put("userId", String.valueOf(userDetails.getId()));
                MDC.put("username", userDetails.getUsername());
            } else {
                MDC.put("userId", "anonymous");
            }

            chain.doFilter(request, response);
        } finally {
            MDC.remove("userId");
            MDC.remove("username");
        }
    }
}
