package com.imperialnet.foodstore.users.infrastructure.security;

import com.imperialnet.foodstore.users.infrastructure.persistence.entity.UserEntity;
import com.imperialnet.foodstore.users.infrastructure.persistence.repository.UserJPARepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Loads user details from the database and adapts them for Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserJPARepository userRepository;

    public CustomUserDetailsService(UserJPARepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Called by Spring Security during authentication.
     * @param username email used for login
     * @return UserDetails for authentication and authorization
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return CustomUserDetails.fromEntity(userEntity);
    }
}
