package com.imperialnet.foodstore.users.application.usecase;

import com.imperialnet.foodstore.users.application.ports.out.UserRepositoryPort;
import com.imperialnet.foodstore.users.domain.model.Role;
import com.imperialnet.foodstore.users.domain.model.User;
import com.imperialnet.foodstore.users.infrastructure.web.dto.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class CreateUserTest {

    private User user;

    private UserRepositoryPort userRepository = mock(UserRepositoryPort.class);
    private  PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private CreateUser service = new CreateUser(userRepository, passwordEncoder);


    @BeforeEach
    void setUp() {
        user = User.createNew(
                "Bob",
                "Brown",
                "bob@mail.com",
                "StrongPwd.1",
                Role.DUENO,
                "Admin"
        );
    }

    //Caso feliz: request válido, email no existe, guarda y devuelve response correcto.
    @Test
    void shouldCreateUserSuccessfully() {
        assertEquals("Bob",user.getName());
        assertEquals("Brown",user.getLastname());
        assertEquals("bob@mail.com",user.getEmail());
        assertEquals(Role.DUENO,user.getRole());
        assertTrue(user.isActive());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        assertEquals("Admin",user.getCreatedBy());
        assertEquals("Admin",user.getUpdatedBy());

    }
    //Caso inválido: repo devuelve un usuario existente → lanza IllegalArgumentException("El email ya está en uso").
    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists(){
        CreateUserRequest request = new CreateUserRequest("Bob", "Brown", "bob@mail.com", "StrongPwd.1", Role.DUENO);
        User existingUser = User.createNew("Bob", "Brown", "bob@mail.com", "hasA.12121h", Role.DUENO, "system");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            service.execute(request, "admin");
        });
    }

    //Valida que el PasswordEncoder.encode() se llame con la contraseña raw del request y que el User guardado tenga el hash devuelto por el encoder.
        @Test
        void shouldEncodePasswordBeforeSaving() {
            // Arrange
            CreateUserRequest request = new CreateUserRequest("Bob", "Brown", "bob@mail.com", "StrongPwd.1", Role.DUENO);

            // el encoder devuelve un hash simulado
            when(passwordEncoder.encode(request.password())).thenReturn("HASHED-PWD-123");
            when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
            when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

            // Act
            service.execute(request, "admin");

            // Assert
            // capturar el User que fue guardado
            ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(captor.capture());
            User savedUser = captor.getValue();

            // comprobar que el password guardado es el hash, no el raw
            assertThat(savedUser.getPasswordHash()).isEqualTo("HASHED-PWD-123");
            assertThat(savedUser.getPasswordHash()).isNotEqualTo(request.password());

            // verificar que encode fue llamado una vez con la raw password
            verify(passwordEncoder, times(1)).encode(request.password());
        }



    //Valida que el UserRepositoryPort.save() reciba un User con nombre, apellido, email, rol y createdBy correctos.
    @Test
    void shouldPassCorrectUserToRepository(){
        CreateUserRequest request = new CreateUserRequest("Bob", "Brown", "juan@bob.com", "StrongPwd.1", Role.DUENO);

        when(passwordEncoder.encode(request.password())).thenReturn("HASHED-PWD-123");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        service.execute(request, "admin");
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User savedUser = captor.getValue();
        assertThat(savedUser.getName()).isEqualTo("Bob");
        assertThat(savedUser.getLastname()).isEqualTo("Brown");
        assertThat(savedUser.getEmail()).isEqualTo("juan@bob.com");
        assertThat(savedUser.getRole()).isEqualTo(Role.DUENO);
        assertThat(savedUser.getCreatedBy()).isEqualTo("admin");

    }

    //Chequea que la respuesta devuelta corresponda al User que el repo retorna (ej. id asignado, email, nombre).
    @Test
    void shouldReturnResponseMappedFromSavedUser(){
        CreateUserRequest request = new CreateUserRequest("Bob", "Brown", "juan@bob.com", "StrongPwd.1", Role.DUENO);

        when(passwordEncoder.encode(request.password())).thenReturn("HASHED-PWD-123");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.assignIdOnce(42L); // simular que el repo asigna ID
            return u;
        });
        var response = service.execute(request, "admin");
        assertThat(response.id()).isEqualTo(42L);
        assertThat(response.name()).isEqualTo("Bob");
        assertThat(response.lastname()).isEqualTo("Brown");
        assertThat(response.email()).isEqualTo("juan@bob.com");
        assertThat(response.role()).isEqualTo("DUENO");

    }


}