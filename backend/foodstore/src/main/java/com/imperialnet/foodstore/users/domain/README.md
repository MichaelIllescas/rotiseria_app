# Módulo Domain - FoodStore

Este módulo representa la **lógica de negocio** y el **modelo de dominio** de la aplicación FoodStore. Aquí se definen las entidades principales, reglas de negocio y excepciones específicas del dominio de usuarios.

## Estructura y Propósito

- **Modelos:**
    - `User`: Agregado raíz que representa un usuario del sistema.
    - `Role`: Enumera los roles posibles (`DUENO`, `ENCARGADO`, `ATENCION`).
    - `PasswordResetToken`: Gestiona el proceso de recuperación de contraseña.

- **Eventos:**
    - `EmailEvent`: Evento genérico para el envío de emails (por ejemplo, recuperación de contraseña).

- **Excepciones:**
    - `BusinessException`: Excepción genérica de negocio.
    - `UserNotFoundException`: Se lanza cuando no se encuentra un usuario.

## Principales Reglas de Negocio

- **User**
    - Creación y rehidratación de usuarios.
    - Activación/desactivación de usuarios.
    - Cambio de rol, email, nombre y apellido.
    - Validación de complejidad de contraseñas (mínimo 8 caracteres, mayúscula, minúscula, número y caracter especial).
    - Asignación de ID inmutable.
    - Auditoría de cambios (`createdBy`, `updatedBy`, fechas).

- **PasswordResetToken**
    - Generación de tokens únicos para recuperación de contraseña.
    - Control de expiración (30 minutos).

## Consideraciones

- El módulo es **puro**, sin dependencias de frameworks (no usa anotaciones JPA).
- Las reglas de negocio se aplican directamente en los modelos.
- Las excepciones permiten manejar errores específicos del dominio.

## Ubicación

`backend/foodstore/src/main/java/com/imperialnet/foodstore/users/domain/`

---

Este módulo es la base para la integridad y consistencia de los datos de usuarios en FoodStore.
