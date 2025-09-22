# 🚀 Definición del Alcance Inicial / MVP – Plataforma Web para Rotisería (Actualizado)

## Funcionalidades incluidas en el MVP
1. **Autenticación y gestión de usuarios (RBAC)**  
   - Login con JWT.  
   - Roles: *Dueño*, *Encargado*, *Atención*.  
   - **Gestión de usuarios**: crear, editar, **desactivar** (sin borrado físico), restablecer contraseña.

2. **Catálogo público de productos**  
   - Fotos, descripciones, precios y **disponibilidad en tiempo real** (solo productos activos).

3. **Carrito de compras y checkout como invitado**  
   - Captura de datos mínimos (nombre, teléfono, dirección si corresponde).  
   - Validaciones de stock/horario al confirmar.

4. **Gestión de stock dinámica**  
   - Descuento automático al confirmar pedido.  
   - Ajuste manual por *Encargado/Dueño* (actualización individual y masiva).

5. **Pedidos en tiempo real**  
   - Estados: *Recibido → Preparando → Listo → Entregado / Cancelado*.  
   - Vista operativa para *Atención* con actualización instantánea.

6. **Pagos**  
   - Integración con **Mercado Pago (Checkout Pro)**.  
   - Opción **contraentrega** (efectivo/pos) registrada en el sistema.

7. **Reportes iniciales**  
   - **Ventas por día / semana / mes**.  
   - **Top productos** por cantidad e ingresos.  
   - Filtro por rango de fechas.  

8. **Auditoría (log funcional mínimo)**  
   - Registro de: login/logout, **altas/cambios de usuario**, **alta/edición de producto**, **cambios de stock**, **cambios de estado de pedido**, **intentos de pago**.  
   - Datos mínimos: usuario, acción, entidad/ID, timestamp, resultado.

9. **Validación de horarios de atención**  
   - Bloqueo de pedidos fuera de rango horario configurado.

10. **Panel básico (Backoffice)**  
    - Dashboard inicial con: total de ventas del día, pedidos activos, alerta de bajo stock.

---

## Funcionalidades fuera del MVP (para versiones posteriores)
1. **Reportes avanzados y exportaciones** (CSV/Excel, gráficos comparativos, cohortes).  
2. **Auditoría avanzada** (búsqueda/filtrado por actor, retención configurable, exportación).  
3. **Perfil/branding de la rotisería** (logo, tema visual, texto institucional).  
4. **Notificaciones y fidelización** (email/push/WhatsApp, cupones, campañas).  
5. **Multi-sucursal y roles jerárquicos** (permisos por local).  
6. **Microservicios / Microfrontends** (evolución desde monolito modular).  
7. **Zonas y tarifas de delivery avanzadas** (radio, costos dinámicos).  
