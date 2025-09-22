# 📊 Matriz de Trazabilidad – Plataforma Web para Rotisería

Este documento vincula las Historias de Usuario (HU) con los Requisitos Funcionales (RF), los endpoints/DTO principales y los casos de prueba asociados.

| Historia de Usuario | Requisito(s) Funcional(es) | Endpoint/DTO Principal | Caso de Prueba |
|---------------------|----------------------------|-------------------------|----------------|
| HU-01 – Visualizar catálogo de productos | RF1 | GET /api/public/products | Test catálogo devuelve activos |
| HU-02 – Disponibilidad de stock | RF2 | GET /api/public/products | Test stock decrece tras orden |
| HU-03 – Búsqueda de productos | RF1 | GET /api/public/products?q=&categoryId= | Test búsqueda por nombre/categoría |
| HU-04 – Detalle de producto | RF1 | GET /api/public/products/{id} | Test muestra detalle completo |
| HU-05 – Checkout como invitado | RF3 | POST /api/public/orders | Test crea orden válida |
| HU-06 – Selección de método de entrega | RF3 | POST /api/public/orders (deliveryType) | Test valida dirección según método |
| HU-07 – Opciones de pago | RF4 | POST /payments/mp/checkout | Test transición PENDING→APPROVED |
| HU-08 – Validación de horarios de atención | RF6 | GET/PUT /api/settings/business-hours | Test rechaza pedidos fuera de horario |
| HU-09 – Confirmación de pedido | RF5 | POST /api/public/orders | Test genera número único |
| HU-10 – Visualización de pedidos entrantes | RF5 | GET /api/orders?status=RECEIVED | Test pedido aparece en tablero |
| HU-11 – Gestión de estados de pedido | RF5+RF9 | PATCH /api/orders/{id}/status | Test transición registrada |
| HU-12 – Cancelación de pedidos | RF5+RF9 | PATCH /api/orders/{id}/status | Test motivo registrado |
| HU-13 – Filtros en la gestión de pedidos | RF5 | GET /api/orders?status= | Test devuelve pedidos correctos |
| HU-14 – Histórico de pedidos | RF5 | GET /api/orders?status=DELIVERED | Test lista histórico |
| HU-15 – Gestión de productos | RF1+RF9 | CRUD /api/products | Test CRUD productos con auditoría |
| HU-16 – Control de visibilidad de productos | RF1+RF9 | PUT /api/products/{id} (isActive) | Test visibilidad producto |
| HU-17 – Administración de imágenes de productos | RF1+RF9 | PUT /api/products/{id}/image | Test gestionar imágenes |
| HU-18 – Actualización individual de stock | RF2+RF9 | PUT /api/products/{id}/stock | Test actualizar stock |
| HU-19 – Carga masiva de stock diario | RF2+RF9 | PUT /api/products/stock/bulk-update | Test carga masiva stock |
| HU-20 – Gestión de categorías | RF1+RF9 | CRUD /api/categories | Test CRUD categorías |
| HU-21 – Gestión de usuarios y roles | RF7+RF9 | CRUD /api/users | Test gestión usuarios |
| HU-22 – Configuración de horarios | RF6+RF9 | GET/PUT /api/settings/business-hours | Test bloqueo fuera de horario |
| HU-23 – Reportes de ventas y top productos | RF8 | GET /api/reports/sales, GET /api/reports/top-products | Test reportes correctos |
| HU-24 – Exportación de reportes a CSV | RF8 | GET /api/reports/sales/export?format=csv | Test exportación CSV |
| HU-26 – Configuración de perfil de la rotisería | RF7+RF9 | GET/PUT /api/settings/profile | Test modificación perfil |
| HU-27 – Auditoría del sistema | RF9 | GET /api/audit | Test registro completo y consulta |
