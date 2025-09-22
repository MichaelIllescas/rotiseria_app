package com.imperialnet.foodstore.users.infrastructure.security;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // ⚠️ ojo: si tu formulario tiene CSRF token activalo
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // registro y demás públicos
                        .requestMatchers("/login").permitAll()   // tu login personalizado
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // recursos estáticos
                        .requestMatchers("/api/admin/**").hasRole("DUENO")
                        .requestMatchers("/api/manager/**").hasAnyRole("DUENO", "ENCARGADO")
                        .requestMatchers("/api/att/**").hasAnyRole("DUENO", "ENCARGADO", "ATENCION")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")            // GET para renderizar login.html
                        .loginProcessingUrl("/login")   // POST que procesa login
                        .defaultSuccessUrl("http://localhost:5173   ", true)   // redirige tras login
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                )
                .build();
    }

}
