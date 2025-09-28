package com.imperialnet.foodstore.users.infrastructure.persistence.repository;

import com.imperialnet.foodstore.users.domain.model.Role;
import com.imperialnet.foodstore.users.infrastructure.persistence.repository.UserJPARepository;
import com.imperialnet.foodstore.users.infrastructure.persistence.entity.UserEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Inserts a default user if not present in DB when the application starts.
 */
@Configuration
public class UserSeeder {

    @Bean
    CommandLineRunner initDefaultUser(UserJPARepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String defaultEmail = "admin@foodstore.com";

            userRepository.findByEmail(defaultEmail).ifPresentOrElse(
                    user -> System.out.println("✅ Default user already exists: " + user.getEmail()),
                    () -> {
                        UserEntity defaultUser = UserEntity.builder()
                                .name("Jonathan")
                                .email(defaultEmail)
                                .passwordHash(passwordEncoder.encode("admin123"))
                                .role(Role.DUENO)
                                .lastname("Illescas")
                                .active(true)
                                .build();

                        userRepository.save(defaultUser);
                        System.out.println("👤 Default user created: " + defaultEmail + " / admin123");
                    }
            );
        };
    }
}
