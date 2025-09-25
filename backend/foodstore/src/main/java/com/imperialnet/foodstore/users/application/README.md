# Paquete Application - Módulo de Usuarios (FoodStore)

El paquete `application` define los **casos de uso** del módulo de usuarios, siguiendo el patrón de arquitectura limpia. Aquí se implementa la lógica que conecta el dominio con la infraestructura (persistencia, servicios externos).

## Propósito

- Orquestar la lógica de negocio definida en el dominio.
- Exponer servicios para la capa web (controllers).
- Validar reglas de negocio antes de interactuar con la infraestructura.
- Mantener la independencia respecto a frameworks y detalles técnicos.

## Estructura

- **Casos de uso (services):**
    - `CreateUser`: Alta de usuarios, validación de email y complejidad de contraseña.
    - `UpdateUser`: Modificación de datos, cambio de rol, activación/desactivación y cambio de contraseña.
    - `DeleteUser`: Baja lógica de usuarios.
    - `GetAllUsers`: Listado de todos los usuarios.
    - `GetUserById`: Consulta de usuario por ID.
    - `ForgotPasswordService`: Generación de token y envío de email para recuperación de contraseña.
    - `ResetPasswordService`: Validación de token y cambio de contraseña.

- **Interfaces (ports):**
    - `in`: Contratos de los casos de uso que expone la aplicación.
    - `out`: Contratos para servicios externos (repositorios, email).

## Principales responsabilidades

- Validar datos y reglas de negocio antes de modificar el estado.
- Gestionar errores y excepciones específicas del dominio.
- Encapsular la lógica de interacción con repositorios y servicios externos.
- Transformar entidades de dominio a DTOs para la capa web.

## Logging en casos de uso

Los casos de uso implementan un esquema de logging consistente con la arquitectura:

- Se utiliza **MDC (Mapped Diagnostic Context)** para registrar la acción (`action=CREATE_USER`, `action=RESET_PASSWORD`, etc.).
- No se generan logs `INFO` en esta capa (eso corresponde a los controllers).
- Se registran:
    - **`WARN`** → condiciones de negocio previstas pero anómalas  
      (ej. email ya registrado, token de recuperación inválido, contraseña sin complejidad).
    - **`ERROR`** → errores inesperados del sistema  
      (fallos de base de datos, excepciones no controladas).
- Todas las excepciones se relanzan para que sean procesadas por el **`GlobalExceptionHandler`**, que las convierte en respuestas HTTP adecuadas.

Este enfoque asegura **trazabilidad**, **auditoría** y separación de responsabilidades entre controladores y casos de uso.


## Ubicación

`backend/foodstore/src/main/java/com/imperialnet/foodstore/users/application/`

---

Este paquete es el corazón de la lógica de aplicación, asegurando que las operaciones sobre usuarios sean consistentes y seguras.