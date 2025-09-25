# Seguridad en FoodStore (Spring Boot)

## Autenticación

- Se utiliza `Spring Security` con autenticación basada en formulario.
- El login se realiza mediante email y contraseña.
- Las credenciales se verifican usando el servicio `CustomUserDetailsService`, que carga los datos del usuario desde la base de datos.
- Las contraseñas se almacenan de forma segura usando `BCrypt` (`PasswordEncoder`).
- Se registran logs de seguridad en cada intento de login:
    - **Login exitoso** → `INFO`
    - **Login fallido** → `WARN` (incluye IP y motivo del fallo)

## Autorización

- Las rutas públicas son:
    - `/auth/**` (registro, recuperación de contraseña, etc.)
    - `/login` (página y procesamiento de login)
    - Recursos estáticos: `/css/**`, `/js/**`, `/images/**`
- Todas las demás rutas requieren que el usuario esté autenticado.
- Cuando un usuario intenta acceder a un recurso sin permisos:
    - Si **está autenticado pero no autorizado** → se captura en `CustomAccessDeniedHandler` y se loguea `WARN`.
    - Si **no está autenticado** → se captura en `CustomAuthenticationEntryPoint` y se loguea `WARN`.

## Roles

- Los roles se gestionan mediante la clase `Role` y se asignan como `GrantedAuthority` en `CustomUserDetails`.
- Spring Security usa el prefijo `ROLE_` para distinguir los roles.

## Auditoría

- Se utiliza `SpringSecurityAuditorAware` para registrar el usuario que realiza cambios en la base de datos.
- Si no hay usuario autenticado, se registra como `SYSTEM`.

## Logout

- El logout se realiza en `/logout` y redirige a `/login?logout=true`.
- Cada logout exitoso se loguea como evento de seguridad → `INFO`.

## Logging de seguridad

El sistema implementa un esquema unificado de logs de seguridad mediante `SecurityLogger`:

- **Login exitoso** → `INFO`
- **Login fallido** → `WARN`
- **Logout** → `INFO`
- **Acceso denegado** (usuario autenticado sin permisos) → `WARN`
- **Acceso no autenticado a recurso protegido** → `WARN`
- **Cambios sensibles** (ej. cambio de rol, reseteo de contraseña) → `INFO`

Todos los eventos incluyen contexto adicional mediante **MDC** (`action`, `username`, `userId`, `requestId`).

## Configuración técnica

- La configuración principal está en `SecurityConfig.java`.
- El encoder de contraseñas es `BCryptPasswordEncoder`.
- El proveedor de autenticación es `DaoAuthenticationProvider` usando el servicio personalizado de usuarios.
- Handlers de seguridad:
    - `CustomAccessDeniedHandler` → acceso denegado a usuario autenticado.
    - `CustomAuthenticationEntryPoint` → acceso no autenticado.
    - `SecurityLogger` → clase utilitaria para centralizar los logs de seguridad.

---

Este README resume cómo se gestiona la seguridad, autenticación, autorización, auditoría y logging de seguridad en la aplicación.
