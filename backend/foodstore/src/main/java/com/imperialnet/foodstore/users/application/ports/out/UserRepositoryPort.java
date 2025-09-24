package com.imperialnet.foodstore.users.application.ports.out;


import com.imperialnet.foodstore.users.domain.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Outbound port for user persistence operations.
 * Defines the contract without depending on infrastructure (JPA, DB).
 */

public interface UserRepositoryPort {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    User save(User user);

    void deleteById(Long id);
}
