package com.imperialnet.foodstore.config;

import com.imperialnet.foodstore.users.infrastructure.security.SpringSecurityAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class PersistenceConfig {

    /**
     * AuditorAware le dice a Spring quién es el "usuario actual"
     * para rellenar los campos @CreatedBy y @LastModifiedBy.
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        // Delega en nuestra implementación personalizada
        return new SpringSecurityAuditorAware();
    }
}
