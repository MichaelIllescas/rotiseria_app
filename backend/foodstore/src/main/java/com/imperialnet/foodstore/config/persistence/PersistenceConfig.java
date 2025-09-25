package com.imperialnet.foodstore.config.persistence;

import com.imperialnet.foodstore.config.security.SpringSecurityAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

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
