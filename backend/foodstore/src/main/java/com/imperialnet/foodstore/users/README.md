# Módulo de Usuarios

El módulo de **usuarios** gestiona todas las operaciones relacionadas con la administración de cuentas:  
- Registro y actualización de usuarios.  
- Activación y desactivación.  
- Cambio y recuperación de contraseñas.  
- Consulta de información de usuario.  

Forma parte de la arquitectura **hexagonal**, donde la lógica de negocio se encuentra aislada del detalle técnico.

---

## 📐 Flujo de datos

1. **Solicitud HTTP**  
   - El cliente envía un request a un **controlador REST** (`UserController`, `AuthController`, etc.).  
   - El controlador recibe un **DTO** (`CreateUserRequest`, `UpdateUserRequest`, etc.).  
   - El controlador invoca una **interfaz de caso de uso** (`CreateUserUseCase`, `UpdateUserUseCase`, etc.).  

2. **Aplicación (Puertos de entrada + implementación)**  
   - Cada interfaz de caso de uso tiene una **clase concreta que la implementa** (`CreateUser`, `UpdateUser`, `ForgotPasswordService`, etc.).  
   - El controlador nunca conoce la implementación, solo la interfaz.  
   - La implementación:  
     - Convierte el **DTO → objeto de dominio** (`User`) usando `UserDtoMapper`.  
     - Aplica reglas de negocio y validaciones (ej. unicidad de email, complejidad de contraseña).  
     - Si necesita persistir, invoca un **puerto de salida** (`UserRepositoryPort`, `EmailServicePort`, etc.).  

3. **Infraestructura (Adaptadores de salida)**  
   - Los **puertos de salida** se implementan en esta capa.  
   - Ejemplo: `UserRepositoryAdapter` implementa `UserRepositoryPort`.  
   - El adapter convierte:  
     - **Dominio → Entidad JPA** (con `UserMapper`) antes de persistir.  
     - **Entidad JPA → Dominio** al recuperar datos.  
   - Usa un repositorio JPA de Spring Data para las operaciones reales.  

4. **Dominio**  
   - Aquí viven las entidades (`User`, `PasswordResetToken`), eventos (`EmailEvent`) y excepciones.  
   - El dominio no conoce ni DTOs ni entidades JPA.  
   - Solo maneja reglas de negocio puras.  

5. **Respuesta**  
   - El caso de uso retorna un objeto de dominio.  
   - Se convierte a **DTO de salida** (`UserResponse`, `CreateUserResponse`) con un mapper.  
   - El controlador devuelve ese DTO como respuesta HTTP.  

---

## 🔗 Endpoints principales

### Usuarios
- **POST `/api/users/register`** → Registrar un nuevo usuario.  
- **PUT `/api/users/update/{id}`** → Actualizar usuario.  
- **DELETE `/api/users/delete/{id}`** → Eliminar usuario.  
- **PATCH `/api/users/activate/{id}`** → Activar usuario.  
- **PATCH `/api/users/deactivate/{id}`** → Desactivar usuario.  
- **PATCH `/api/users/changePassword/{id}`** → Cambiar contraseña.  
- **GET `/api/users/getUser/{id}`** → Obtener usuario por ID.  
- **GET `/api/users/getAll`** → Obtener todos los usuarios.  
- **GET `/api/users/me`** → Obtener usuario en sesión.  

### Recuperación de contraseña
- **POST `/auth/forgot-password`** → Iniciar proceso de recuperación (envía email con token).  
- **POST `/auth/reset-password`** → Restablecer contraseña usando token.  

---

## ⚙️ Decisiones de diseño

- La **validación de contraseñas** se realiza en la capa de aplicación antes de codificarlas.  
- El dominio **solo maneja hashes de contraseñas válidas**.  
- Los **Mappers** garantizan que el dominio nunca conozca DTOs ni entidades JPA.  
- El envío de correos se maneja mediante **eventos** (`EmailEvent` → `EmailEventListener`).  
- Activación/desactivación: si el estado ya coincide con el solicitado, no se ejecuta acción ni se lanza excepción.  

---

## 🧪 Pruebas

- Tests unitarios en dominio (`User`, `PasswordResetToken`) y aplicación (`CreateUser`, `UpdateUser`, `ForgotPasswordService`, etc.).  
- Se cubren escenarios felices y fallidos (usuario no encontrado, email duplicado, password inválida).  
- Los adaptadores (`UserRepositoryAdapter`, `EmailServiceAdapter`) se mockean en pruebas de aplicación.  

---
