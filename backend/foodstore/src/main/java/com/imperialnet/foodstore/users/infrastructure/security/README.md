# Seguridad en FoodStore (Spring Boot)

## Autenticación

- Se utiliza `Spring Security` con autenticación basada en formulario.
- El login se realiza mediante email y contraseña.
- Las credenciales se verifican usando el servicio `CustomUserDetailsService`, que carga los datos del usuario desde la base de datos.
- Las contraseñas se almacenan de forma segura usando `BCrypt` (`PasswordEncoder`).

## Autorización

- Las rutas públicas son:
    - `/auth/**` (registro, recuperación de contraseña, etc.)
    - `/login` (página y procesamiento de login)
    - Recursos estáticos: `/css/**`, `/js/**`, `/images/**`
- Todas las demás rutas requieren que el usuario esté autenticado.

## Roles

- Los roles se gestionan mediante la clase `Role` y se asignan como `GrantedAuthority` en `CustomUserDetails`.
- Spring Security usa el prefijo `ROLE_` para distinguir los roles.

## Auditoría

- Se utiliza `SpringSecurityAuditorAware` para registrar el usuario que realiza cambios en la base de datos.
- Si no hay usuario autenticado, se registra como `SYSTEM`.

## Logout

- El logout se realiza en `/logout` y redirige a `/login?logout=true`.

## Configuración técnica

- La configuración principal está en `SecurityConfig.java`.
- El encoder de contraseñas es `BCryptPasswordEncoder`.
- El proveedor de autenticación es `DaoAuthenticationProvider` usando el servicio personalizado de usuarios.

---

Este README resume cómo se gestiona la seguridad, autenticación y autorización en la aplicación.
