package com.imperialnet.foodstore.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration.
 */
@Slf4j
@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    // Password hashing
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authentication provider (uses our CustomUserDetailsService + BCrypt)
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // AuthenticationManager → central auth object for Spring Security
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Security rules
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,  CustomAccessDeniedHandler accessDeniedHandler,  CustomAuthenticationEntryPoint authenticationEntryPoint) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // ⚠️ ojo: si tu formulario tiene CSRF token activalo
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // registro y demás públicos
                        .requestMatchers("/login").permitAll()   // tu login personalizado
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // recursos estáticos
                        .requestMatchers("/auth/reset-password", "/auth/forgot-password").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")            // GET para renderizar login.html
                        .loginProcessingUrl("/login")   // POST que procesa login
                        .successHandler((request, response, authentication) -> {
                            String username = authentication.getName();
                            // ✅ Usamos SecurityLogger
                            SecurityLogger.logLoginSuccess(username);
                            response.sendRedirect("http://localhost:5173");
                        })
                        .failureHandler((request, response, exception) -> {
                            String username = request.getParameter("username");
                            String clientIp = request.getRemoteAddr();
                            // ✅ Usamos SecurityLogger
                            SecurityLogger.logLoginFailure(username, "from ip=" + clientIp + " | " + exception.getMessage());
                            response.sendRedirect("/login?error=true");
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            if (authentication != null) {
                                SecurityLogger.logLogout(authentication.getName());
                            }
                            response.sendRedirect("/login?logout=true");
                        })
                        .permitAll()
                )
                .exceptionHandling(ex ->  ex
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint))
                        .build();
    }
}


