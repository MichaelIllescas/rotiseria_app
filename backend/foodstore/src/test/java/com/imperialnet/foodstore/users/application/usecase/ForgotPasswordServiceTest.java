package com.imperialnet.foodstore.users.application.usecase;

import com.imperialnet.foodstore.users.application.ports.out.PasswordResetTokenRepositoryPort;
import com.imperialnet.foodstore.users.application.ports.out.UserRepositoryPort;
import com.imperialnet.foodstore.users.domain.event.EmailEvent;
import com.imperialnet.foodstore.users.domain.model.Role;
import com.imperialnet.foodstore.users.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ForgotPasswordServiceTest {

    private UserRepositoryPort userRepository;
    private PasswordResetTokenRepositoryPort tokenRepository;
    private ApplicationEventPublisher eventPublisher;
    private ForgotPasswordService service;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepositoryPort.class);
        tokenRepository = mock(PasswordResetTokenRepositoryPort.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        service = new ForgotPasswordService(userRepository, tokenRepository, eventPublisher);
    }

    @Test
    void shouldPublishEmailEventWhenUserExists() {
        // Arrange
        User user = User.createNew("Bob", "Brown", "bob@mail.com", "hashPwd.1", Role.DUENO, "system");
        user.assignIdOnce(1L);

        when(userRepository.findByEmail("bob@mail.com")).thenReturn(Optional.of(user));

        // Act
        service.execute("bob@mail.com");

        // Assert
        // verificar que se guardó un token
        verify(tokenRepository, times(1)).save(any(), eq(1L));

        // capturar el evento publicado
        ArgumentCaptor<EmailEvent> captor = ArgumentCaptor.forClass(EmailEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        EmailEvent event = captor.getValue();

        assertThat(event.getTo()).isEqualTo("bob@mail.com");
        assertThat(event.getSubject()).isEqualTo("Recuperación de contraseña");
        assertThat(event.getTemplateName()).isEqualTo("email/password-reset");
        assertThat(event.getModel()).containsKey("resetLink");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findByEmail("missing@mail.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            service.execute("missing@mail.com");
        });

        verify(tokenRepository, never()).save(any(), anyLong());
        verify(eventPublisher, never()).publishEvent(any());
    }
}
