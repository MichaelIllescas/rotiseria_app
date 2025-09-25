package com.imperialnet.foodstore.shared.logging;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestIdFilterLogging extends  OncePerRequestFilter {

    private static final String REQUEST_ID = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            // Generamos un UUID único por request
            String requestId = UUID.randomUUID().toString();

            // Lo guardamos en el MDC para que Logback pueda imprimirlo en los logs
            MDC.put(REQUEST_ID, requestId);

            // Continuamos con el request
            filterChain.doFilter(request, response);
        } finally {
            // Limpieza al terminar
            MDC.clear();
        }
    }
}
