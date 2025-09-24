package com.imperialnet.foodstore.users.application.usecase;

import com.imperialnet.foodstore.users.application.ports.out.UserRepositoryPort;
import com.imperialnet.foodstore.users.domain.exception.BusinessException;
import com.imperialnet.foodstore.users.domain.exception.UserNotFoundException;
import com.imperialnet.foodstore.users.domain.model.Role;
import com.imperialnet.foodstore.users.domain.model.User;
import com.imperialnet.foodstore.users.infrastructure.web.dto.UpdateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UpdateUserTest {

    private UserRepositoryPort userRepository;
    private PasswordEncoder passwordEncoder;
    private UpdateUser service;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepositoryPort.class);
        passwordEncoder = mock(PasswordEncoder.class);
        service = new UpdateUser(userRepository, passwordEncoder);
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        // Arrange
        User user = User.createNew("Bob", "Brown", "bob@mail.com", "hashPwd.1", Role.DUENO, "system");
        user.assignIdOnce(1L);

        UpdateUserRequest request = new UpdateUserRequest("Rob", "Black", "bob@mail.com", Role.DUENO, true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        var response = service.execute(1L, request, "admin");

        // Assert
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User savedUser = captor.getValue();

        assertThat(savedUser.getName()).isEqualTo("Rob");
        assertThat(savedUser.getLastname()).isEqualTo("Black");
        assertThat(savedUser.getEmail()).isEqualTo("bob@mail.com");
        assertThat(savedUser.getRole()).isEqualTo(Role.DUENO);
        assertThat(savedUser.isActive()).isTrue();
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            service.execute(99L, new UpdateUserRequest("A","B","c@mail.com", Role.DUENO,true), "admin");
        });
    }

    @Test
    void shouldThrowBusinessExceptionWhenEmailAlreadyInUseByAnotherUser() {
        User user = User.createNew("Bob", "Brown", "bob@mail.com", "hashPwd.1", Role.DUENO, "system");
        user.assignIdOnce(1L);

        User another = User.createNew("Alice", "Green", "rob@mail.com", "hashPwd.2", Role.DUENO, "system");
        another.assignIdOnce(2L);

        UpdateUserRequest request = new UpdateUserRequest("Rob", "Black", "rob@mail.com", Role.DUENO, true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("rob@mail.com")).thenReturn(Optional.of(another));

        assertThrows(BusinessException.class, () -> {
            service.execute(1L, request, "admin");
        });
    }
}
