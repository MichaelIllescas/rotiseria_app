package com.imperialnet.foodstore.users.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testCreateNewUser() {
        String name = "John";
        String lastname = "Doe";
        String email = "joni.illes@hotmail.com";
        String passwordHash = "hashedPassword.1";
        Role role = Role.DUENO;
        String createdBy = "Admin User";
        User user = User.createNew(name, lastname, email, passwordHash, role, createdBy);
        assertNull(user.getId());
        assertEquals(name, user.getName());
        assertEquals(lastname, user.getLastname());
        assertEquals(email, user.getEmail());
        assertEquals(passwordHash, user.getPasswordHash());
        assertEquals(role, user.getRole());
        assertTrue(user.isActive());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        assertEquals(createdBy, user.getCreatedBy());

    }

    @Test
    void testActivateDeactivateUser() {
        User user = User.createNew("Jane", "Smith", "jane.smith@example .com", "1.hashedPwd", Role.DUENO, "Admin");
        user.deactivate("admin");
        assertTrue(!user.isActive());
        assertThat(user.getUpdatedBy()).isEqualTo("admin");
    }

    @Test
    void testChangeRole() {
        User user = User.createNew("Alice", "Johnson", "alice.johnson@example.com", "hashedPwd.1", Role.DUENO, "Admin");
        user.changeRole(Role.ENCARGADO, "admin");
        assertEquals(Role.ENCARGADO, user.getRole());
        assertEquals("admin", user.getUpdatedBy());
    }
    @Test
    void testChangeEmail() {
        User user = User.createNew("Bob", "Brown", "joni.illes@hotmail.com", "1.hashedPwd", Role.DUENO, "Admin");
        String newEmail = "illes@com.com";
        user.changeEmail(newEmail, "admin");
        assertEquals(newEmail, user.getEmail());
        assertEquals("admin", user.getUpdatedBy());
    }

   @Test
    void testChangeEmailInvalid() {
       User user = User.createNew("Bob", "Brown", "ille@scom.com", "hashedPwd.1", Role.DUENO, "Admin");
       assertThrows(IllegalArgumentException.class, () -> {
           user.changeEmail("invalidEmail", "admin");
       });

   }

   @Test
    void testCreateUserInvalidEmail() {
       assertThrows(IllegalArgumentException.class, () -> {
           User.createNew("Bob", "Brown", "invalidEmail", ".12hashedPwd", Role.DUENO, "Admin");
       });
   }


   @Test
    void validatePasswordComplexityError() {
       var exception = assertThrows(IllegalArgumentException.class, () -> {
           User.validatePasswordComplexity("simple");
       });
       assertEquals(  "La contraseña debe tener al menos 8 caracteres, una mayúscula, " +
               "una minúscula, un número y un caracter especial", exception.getMessage());
   }



   @Test
    void changePasswordHash() {
       User user = User.createNew("Bob", "Brown", "joni@gmail.com", "hashedPwd.1", Role.DUENO, "Admin");
       String newPasswordHash = "NewhashedPwd.2";
       user.changePasswordHash(newPasswordHash, "admin");
       assertEquals(newPasswordHash, user.getPasswordHash());
       assertEquals("admin", user.getUpdatedBy());
   }

   @Test
    void changePasswordHashInvalid() {
       User user = User.createNew("Bob", "Brown", "ille@scom.com", "hashedPwd.1", Role.DUENO, "Admin");
       //debe valir que arroje una excepcion si la password va vacia
         assertThrows(NullPointerException.class, () -> {
              user.changePasswordHash(null, "admin");
         });
   }


}