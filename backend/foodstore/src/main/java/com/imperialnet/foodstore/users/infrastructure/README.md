# Paquete Infrastructure - Módulo de Usuarios (FoodStore)

Este paquete conecta la lógica de negocio del dominio de usuarios con la infraestructura técnica: base de datos, seguridad, envío de emails y exposición de endpoints web.

## Estructura

- **Persistence**
    - Entidades JPA (`UserEntity`, `PasswordResetTokenEntity`)
    - Repositorios (`UserJPARepository`, `PasswordResetTokenJpaRepository`)
    - Adaptadores (`UserRepositoryAdapter`, `PasswordResetTokenRepositoryAdapter`)
    - Seeder para usuario por defecto (`UserSeeder`)
    - Mappers para conversión entre entidades y modelos de dominio

- **Security**
    - Configuración de Spring Security (`SecurityConfig`)
    - Servicio de autenticación personalizado (`CustomUserDetailsService`)
    - Adaptador de usuario para Spring Security (`CustomUserDetails`)
    - Auditoría de cambios (`SpringSecurityAuditorAware`)

- **Web**
    - Controladores REST (`UserController`, `AuthController`, `PasswordRecoveryController`)
    - DTOs para requests/responses (`CreateUserRequest`, `UpdateUserRequest`, `UserResponse`, etc.)
    - Validaciones y documentación Swagger

- **Email**
    - Adaptador para envío de emails (`EmailServiceAdapter`)
    - Servicio de plantillas con Thymeleaf (`EmailTemplateService`)
    - Listener de eventos para envío asíncrono (`EmailEventListener`)

## Responsabilidades

- Persistir y recuperar usuarios y tokens de la base de datos.
- Gestionar la autenticación y autorización de usuarios.
- Exponer endpoints HTTP para operaciones de usuario y recuperación de contraseña.
- Enviar correos electrónicos para notificaciones y recuperación de contraseña.
- Mapear datos entre capas (DTOs, entidades, modelos de dominio).

## Ubicación

`backend/foodstore/src/main/java/com/imperialnet/foodstore/users/infrastructure/`

---

Este paquete permite que el módulo de usuarios interactúe de forma segura y eficiente con la base de datos, la web y servicios externos, manteniendo la separación de responsabilidades y la arquitectura limpia.