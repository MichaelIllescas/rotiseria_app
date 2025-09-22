# 📖 Historias de Usuario – Plataforma Web para Rotisería

Este documento detalla las Historias de Usuario (HU) del sistema, con criterios de aceptación, dependencias y trazabilidad.  

---

## Cliente final (invitado)

### HU-01 – Visualizar catálogo de productos
**Actor:** Cliente final (invitado)  
**Descripción:** Como cliente quiero ver el catálogo de productos organizado por categorías, con fotos, precios y descripciones, para poder elegir fácilmente qué comprar.  
**Criterios de Aceptación:**  
1. El catálogo muestra solo productos activos.  
2. Cada producto incluye nombre, descripción, precio, imagen y categoría.  
3. Los productos están agrupados y filtrables por categoría.  
4. Es accesible públicamente sin login.  
5. Productos inactivos no aparecen.  
**Prioridad:** Alta  
**Dependencias:** HU-16, HU-19  
**Trazabilidad:** RF1 · `GET /api/public/products` · Test catálogo devuelve activos.  

---

### HU-02 – Disponibilidad de stock
**Actor:** Cliente final (invitado)  
**Descripción:** Como cliente quiero ver la disponibilidad de stock en tiempo real de cada producto para saber si puedo comprarlo.  
**Criterios de Aceptación:**  
1. Cada producto muestra `dailyStock`.  
2. Si stock = 0, aparece “sin stock”.  
3. Stock decrece automáticamente al vender.  
4. Puede actualizarse manualmente.  
5. Siempre sincronizado con DB.  
**Prioridad:** Alta  
**Dependencias:** HU-01, HU-15, HU-18  
**Trazabilidad:** RF2 · `GET /api/public/products` · Test stock decrece tras orden.  

---

### HU-03 – Búsqueda de productos
**Actor:** Cliente final (invitado)  
**Descripción:** Como cliente quiero buscar productos por nombre o categoría para encontrar más rápido lo que necesito.  
**Criterios de Aceptación:**  
1. Búsqueda parcial por nombre.  
2. Puede combinarse con categoría.  
3. Resultados muestran nombre, imagen, precio y stock.  
4. Si no hay resultados → mensaje claro.  
5. Accesible públicamente.  
**Prioridad:** Media  
**Dependencias:** HU-01, HU-02  
**Trazabilidad:** RF1 · `GET /api/public/products?q=&categoryId=` · Test búsqueda por nombre/categoría.  

---

### HU-04 – Detalle de producto
**Actor:** Cliente final (invitado)  
**Descripción:** Como cliente quiero ver el detalle completo de un producto para decidir si lo compro.  
**Criterios de Aceptación:**  
1. Vista dedicada con datos completos.  
2. Incluye stock disponible.  
3. Permite agregar al carrito.  
4. Si stock = 0, bloquea compra.  
5. Accesible públicamente.  
**Prioridad:** Media  
**Dependencias:** HU-01, HU-02  
**Trazabilidad:** RF1 · `GET /api/public/products/{id}` · Test muestra detalle completo.  

---

### HU-05 – Checkout como invitado
**Actor:** Cliente final (invitado)  
**Descripción:** Como cliente quiero comprar sin crear cuenta para agilizar el proceso.  
**Criterios de Aceptación:**  
1. Captura datos mínimos (nombre, teléfono, dirección si entrega).  
2. Valida horarios antes de aceptar.  
3. Se crea orden con estado `RECEIVED`.  
4. Reserva stock al confirmar.  
5. Calcula total correctamente.  
6. Muestra confirmación al cliente.  
**Prioridad:** Alta  
**Dependencias:** HU-01, HU-02, HU-04  
**Trazabilidad:** RF3 · `POST /api/public/orders` · Test crea orden válida.  

---

### HU-06 – Selección de método de entrega
**Actor:** Cliente final (invitado)  
**Descripción:** Como cliente quiero elegir retiro o entrega a domicilio.  
**Criterios de Aceptación:**  
1. Opción RETIRO no requiere dirección.  
2. Opción DOMICILIO requiere dirección.  
3. Método registrado en la orden.  
4. Validación bloquea pedidos sin dirección.  
**Prioridad:** Alta  
**Dependencias:** HU-05  
**Trazabilidad:** RF3 · `POST /api/public/orders` (deliveryType) · Test valida dirección según método.  

---

### HU-07 – Opciones de pago
**Actor:** Cliente final (invitado)  
**Descripción:** Como cliente quiero pagar con Mercado Pago o al retirar/entregar.  
**Criterios de Aceptación:**  
1. Métodos: MP y contraentrega.  
2. Pago aprobado MP → `APPROVED`.  
3. Pago contraentrega → `PENDING`.  
4. Pago rechazado MP → `REJECTED`.  
5. Estado visible en gestión interna.  
**Prioridad:** Alta  
**Dependencias:** HU-05, HU-06  
**Trazabilidad:** RF4 · `POST /payments/mp/checkout` · Test transición PENDING→APPROVED.  

---

### HU-08 – Validación de horarios de atención
**Actor:** Cliente final (invitado)  
**Descripción:** Como cliente quiero ver horarios disponibles para no perder tiempo fuera de horario.  
**Criterios de Aceptación:**  
1. Muestra horarios configurados.  
2. Fuera de horario → mensaje claro.  
3. Solo se aceptan pedidos en ventana válida.  
4. Validación en frontend y backend.  
5. Permitir marcar “cerrado hoy”.  
**Prioridad:** Alta  
**Dependencias:** HU-05, HU-06  
**Trazabilidad:** RF6 · `GET/PUT /api/settings/business-hours` · Test rechaza pedidos fuera de horario.  

---

### HU-09 – Confirmación de pedido
**Actor:** Cliente final (invitado)  
**Descripción:** Como cliente quiero recibir una confirmación visual al registrar el pedido.  
**Criterios de Aceptación:**  
1. Muestra número, productos, total, entrega/pago.  
2. Estado inicial `RECEIVED`.  
3. Funciona sin login.  
4. Refleja estado del pago (pendiente/aprobado).  
5. Número único de pedido.  
**Prioridad:** Alta  
**Dependencias:** HU-05, HU-07  
**Trazabilidad:** RF5 · `POST /api/public/orders` · Test genera número único y correcto.  

---

## Atención (gestión de pedidos)

### HU-10 – Visualización de pedidos entrantes
**Actor:** Atención  
**Descripción:** Como Atención quiero ver pedidos entrantes en tiempo real.  
**Criterios de Aceptación:**  
1. Tablero muestra pedidos `RECEIVED`.  
2. Actualización en tiempo real.  
3. Incluye datos clave (cliente, productos, entrega, total).  
4. Ordenados por fecha.  
5. Posibilidad de refresco manual.  
**Prioridad:** Alta  
**Dependencias:** HU-09, HU-05  
**Trazabilidad:** RF5 · `GET /api/orders?status=RECEIVED` · Test pedido aparece en tablero.  

---

### HU-11 – Gestión de estados de pedido
**Actor:** Atención  
**Descripción:** Como Atención quiero cambiar estados para reflejar avance.  
**Criterios de Aceptación:**  
1. Flujo válido: RECEIVED→PREPARING→READY→DELIVERED.  
2. Permitir cancelación en RECEIVED/PREPARING.  
3. Registra en auditoría.  
4. Estado visible en tablero.  
5. Solo rol Atención.  
**Prioridad:** Alta  
**Dependencias:** HU-10, HU-25  
**Trazabilidad:** RF5+RF9 · `PATCH /api/orders/{id}/status` · Test transición registrada.  

---

### HU-12 – Cancelación de pedidos
**Actor:** Atención  
**Descripción:** Como Atención quiero cancelar pedidos especificando motivo.  
**Criterios de Aceptación:**  
1. Cancelar solo en RECEIVED/PREPARING.  
2. Motivo obligatorio.  
3. Estado = CANCELED.  
4. Motivo visible en historial.  
5. Acción registrada en auditoría.  
**Prioridad:** Alta  
**Dependencias:** HU-10, HU-11  
**Trazabilidad:** RF5+RF9 · `PATCH /api/orders/{id}/status` · Test motivo registrado.  

---

### HU-13 – Filtros en la gestión de pedidos
**Actor:** Atención  
**Descripción:** Como Atención quiero filtrar pedidos por estado.  
**Criterios de Aceptación:**  
1. Filtro por RECEIVED, PREPARING, READY, DELIVERED, CANCELED.  
2. Solo muestra pedidos filtrados.  
3. Posibilidad de limpiar filtro.  
4. Persistencia temporal del filtro.  
**Prioridad:** Media  
**Dependencias:** HU-10, HU-11, HU-12  
**Trazabilidad:** RF5 · `GET /api/orders?status=` · Test devuelve pedidos correctos.  

---

### HU-14 – Histórico de pedidos
**Actor:** Atención  
**Descripción:** Como Atención quiero consultar pedidos completados.  
**Criterios de Aceptación:**  
1. Listar `DELIVERED` y `CANCELED`.  
2. Mostrar cliente, fecha, productos, total.  
3. Filtrar por fechas.  
4. Solo lectura.  
5. Acceso Atención+.  
**Prioridad:** Media  
**Dependencias:** HU-11, HU-12  
**Trazabilidad:** RF5 · `GET /api/orders?status=DELIVERED` · Test lista histórico.  

---

## Encargado (productos/stock)

### HU-15 – Gestión de productos
**Actor:** Encargado  
**Descripción:** Como Encargado quiero poder crear, editar y eliminar productos para mantener actualizado el catálogo.  
**Criterios de Aceptación:**  
1. Crear producto con nombre, descripción, precio, categoría, imagen, stock inicial.  
2. Editar cualquier campo de producto existente.  
3. Eliminar productos no asociados a pedidos activos.  
4. Productos eliminados no aparecen en catálogo.  
5. Todas las operaciones registradas en auditoría.  
**Prioridad:** Alta  
**Dependencias:** HU-01, HU-19, HU-25  
**Trazabilidad:** RF1+RF9 · CRUD `/api/products` · Test CRUD completo y auditoría.  

---

### HU-16 – Control de visibilidad de productos
**Actor:** Encargado  
**Descripción:** Como Encargado quiero poder activar o desactivar productos para controlar su visibilidad.  
**Criterios de Aceptación:**  
1. Marcar productos como activos/inactivos.  
2. Inactivos no aparecen en catálogo.  
3. Reactivación restaura visibilidad.  
4. Acción registrada en auditoría.  
5. Solo Encargado/Dueño.  
**Prioridad:** Alta  
**Dependencias:** HU-01, HU-15, HU-25  
**Trazabilidad:** RF1+RF9 · `PUT /api/products/{id}` (isActive) · Test visibilidad.  

---

### HU-17 – Administración de imágenes de productos
**Actor:** Encargado  
**Descripción:** Como Encargado quiero subir, cambiar o eliminar imágenes de productos.  
**Criterios de Aceptación:**  
1. Adjuntar imagen al crear producto.  
2. Cambiar imagen en cualquier momento.  
3. Eliminar imagen (usar placeholder si aplica).  
4. Aceptar solo JPG/PNG con límite de tamaño.  
5. Acción registrada en auditoría.  
**Prioridad:** Media  
**Dependencias:** HU-15, HU-25  
**Trazabilidad:** RF1+RF9 · `PUT /api/products/{id}/image` · Test gestionar imágenes.  

---

### HU-18 – Actualización individual de stock
**Actor:** Encargado/Dueño  
**Descripción:** Como Encargado quiero poder actualizar stock de un producto específico.  
**Criterios de Aceptación:**  
1. Modificar solo el campo stock.  
2. Cambio visible en catálogo.  
3. Stock=0 → “sin stock”.  
4. Acción registrada en auditoría.  
5. Solo Encargado/Dueño.  
**Prioridad:** Alta  
**Dependencias:** HU-02, HU-25  
**Trazabilidad:** RF2+RF9 · `PUT /api/products/{id}/stock` · Test actualizar stock.  

---

### HU-19 – Carga masiva de stock diario
**Actor:** Encargado  
**Descripción:** Como Encargado quiero cargar stock inicial de todos los productos activos en un solo paso al inicio de la jornada.  
**Criterios de Aceptación:**  
1. Establecer stock inicial masivo.  
2. Posible formulario editable o importación CSV.  
3. Sobrescribir stock del día anterior.  
4. No incluir productos inactivos.  
5. Acción registrada en auditoría.  
**Prioridad:** Media  
**Dependencias:** HU-02, HU-15, HU-25  
**Trazabilidad:** RF2+RF9 · `PUT /api/products/stock/bulk-update` · Test carga masiva.  

---

### HU-20 – Gestión de categorías
**Actor:** Encargado  
**Descripción:** Como Encargado quiero crear y gestionar categorías para organizar los productos.  
**Criterios de Aceptación:**  
1. Crear, editar y eliminar categorías.  
2. Categorías inactivas no deben aparecer.  
3. Al eliminar categoría, productos asociados deben reasignarse o quedar sin categoría.  
4. Acción registrada en auditoría.  
**Prioridad:** Media  
**Dependencias:** HU-15, HU-25  
**Trazabilidad:** RF1+RF9 · CRUD `/api/categories` · Test CRUD categorías.  

---

## Dueño (administración avanzada)

### HU-21 – Gestión de usuarios y roles
**Actor:** Dueño  
**Descripción:** Como Dueño quiero administrar usuarios y asignar roles.  
**Criterios de Aceptación:**  
1. Crear usuarios con datos básicos y rol.  
2. Editar datos y rol de usuario.  
3. Desactivar usuarios (soft delete).  
4. Roles determinan permisos.  
5. Acción registrada en auditoría.  
**Prioridad:** Alta  
**Dependencias:** HU-25  
**Trazabilidad:** RF7+RF9 · CRUD `/api/users` · Test gestión usuarios.  

---

### HU-22 – Configuración de horarios
**Actor:** Dueño/Encargado  
**Descripción:** Como Dueño o Encargado quiero configurar horarios de atención y ventanas de pedidos.  
**Criterios de Aceptación:**  
1. Definir múltiples rangos por día.  
2. Marcar días como “cerrados”.  
3. Cambios aplican inmediatamente en checkout.  
4. Pedidos fuera de horario bloqueados.  
5. Modificación registrada en auditoría.  
**Prioridad:** Alta  
**Dependencias:** HU-08, HU-25  
**Trazabilidad:** RF6+RF9 · `GET/PUT /api/settings/business-hours` · Test bloqueo fuera horario.  

---

### HU-23 – Reportes de ventas y top productos
**Actor:** Dueño/Encargado  
**Descripción:** Como Dueño o Encargado quiero ver reportes de ventas y productos más vendidos.  
**Criterios de Aceptación:**  
1. Totales por día, semana, mes.  
2. Ranking de productos por cantidad e ingresos.  
3. Filtrar por fechas.  
4. Datos en tiempo real.  
5. Solo Dueño y Encargado acceden.  
**Prioridad:** Alta  
**Dependencias:** HU-14, HU-25  
**Trazabilidad:** RF8 · `GET /api/reports/sales`, `GET /api/reports/top-products` · Test reportes correctos.  

---

### HU-24 – Exportación de reportes a CSV
**Actor:** Dueño/Encargado  
**Descripción:** Como Dueño o Encargado quiero exportar reportes en CSV para analizarlos externamente.  
**Criterios de Aceptación:**  
1. Exportar reporte visualizado en CSV.  
2. Encabezados claros, datos consistentes.  
3. Descarga con un clic.  
4. Formato UTF-8, delimitado por comas.  
5. Solo Dueño y Encargado.  
**Prioridad:** Media  
**Dependencias:** HU-23  
**Trazabilidad:** RF8 · `GET /api/reports/sales/export?format=csv` · Test exportación CSV.  

---

### HU-26 – Configuración de perfil de la rotisería
**Actor:** Dueño  
**Descripción:** Como Dueño quiero editar el perfil de la rotisería (nombre, dirección, teléfono, logo).  
**Criterios de Aceptación:**  
1. Permitir modificar datos y logo.  
2. Cambios reflejados en catálogo público.  
3. Logo JPG/PNG con límite de tamaño.  
4. Solo Dueño.  
5. Acción registrada en auditoría.  
**Prioridad:** Media  
**Dependencias:** HU-27  
**Trazabilidad:** RF7+RF9 · `GET/PUT /api/settings/profile` · Test modificación perfil.  

---

### HU-27 – Auditoría del sistema
**Actor:** Dueño  
**Descripción:** Como Dueño quiero consultar un historial de auditoría de cambios.  
**Criterios de Aceptación:**  
1. Registrar operaciones sensibles (productos, stock, usuarios, pedidos, horarios, perfil).  
2. Incluir fecha/hora, usuario, entidad, valores antes/después.  
3. Accesible solo para Dueño.  
4. Filtros por entidad y fechas.  
5. Solo lectura.  
**Prioridad:** Alta  
**Dependencias:** HU-11, HU-12, HU-15–20, HU-21, HU-24, HU-26  
**Trazabilidad:** RF9 · `GET /api/audit` · Test registro completo y consulta.  
