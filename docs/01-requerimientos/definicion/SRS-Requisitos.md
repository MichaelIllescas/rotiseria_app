# Requisitos de Software -- Rotisería Web

## 1. Requisitos Funcionales (RF)

-   **RF1 -- Catálogo:**\
    El sistema debe permitir CRUD de productos y categorías, incluyendo
    imágenes, descripción, precio y stock. Deben ser visibles
    públicamente.

-   **RF2 -- Stock dinámico:**\
    El sistema debe descontar stock automáticamente al realizar una
    venta y permitir actualizaciones manuales por Encargado/Dueño, con
    tope diario configurable.

-   **RF3 -- Carrito y Checkout como invitado:**\
    El sistema debe permitir a los clientes armar un carrito y confirmar
    pedidos como invitados, ingresando datos mínimos (nombre, teléfono,
    dirección si corresponde), validando horarios y calculando el total.

-   **RF4 -- Pagos:**\
    El sistema debe integrar Mercado Pago (Checkout Pro), registrar
    estados de pago y soportar pago al retirar o entregar.

-   **RF5 -- Pedidos:**\
    El sistema debe permitir la creación de pedidos y su gestión en
    tiempo real, con estados: *Recibido, Preparando, Listo, Entregado,
    Cancelado*.

-   **RF6 -- Horarios:**\
    El sistema debe permitir configurar horarios de atención y bloquear
    pedidos fuera de esos rangos.

-   **RF7 -- Usuarios y Roles:**\
    El sistema debe soportar autenticación JWT y gestión de usuarios con
    tres roles: Dueño, Encargado y Atención, aplicando políticas de
    autorización.

-   **RF8 -- Reportes:**\
    El sistema debe generar reportes de ventas (por día y por mes),
    identificar productos más vendidos y exportar datos en CSV.

-   **RF9 -- Auditoría:**\
    El sistema debe registrar operaciones sensibles (altas, bajas,
    cambios de producto, stock, estados de pedidos, usuarios).

------------------------------------------------------------------------

## 2. Requisitos No Funcionales (RNF)

-   **RNF1 -- Seguridad:**\
    Uso de JWT con expiración, contraseñas con hashing (BCrypt),
    validación de entradas y control CORS.

-   **RNF2 -- Rendimiento:**\
    Tiempo de respuesta API \< 300ms p95 en VPS demo; catálogos
    cacheables mediante CDN/NGINX.

-   **RNF3 -- Disponibilidad:**\
    El sistema debe estar disponible al menos el 99% durante el horario
    comercial del cliente.

-   **RNF4 -- Escalabilidad:**\
    La arquitectura hexagonal y el uso de contenedores Docker deben
    permitir escalar el sistema fácilmente.

-   **RNF5 -- Observabilidad:**\
    El sistema debe contar con logs estructurados, métricas y trazas de
    errores para facilitar monitoreo.

-   **RNF6 -- Mantenibilidad:**\
    Cobertura de tests superior al 70% en el dominio y casos de uso.
    Código formateado y con linting.

-   **RNF7 -- Portabilidad:**\
    El sistema debe ser desplegable de forma reproducible mediante
    Docker Compose en distintos entornos.
