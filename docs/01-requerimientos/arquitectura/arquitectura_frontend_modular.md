# 🎨 Arquitectura Frontend -- Plataforma Web para Rotisería

Este documento describe la organización del **frontend en React** con un
enfoque **modular por dominio/feature**, alineado con la arquitectura
hexagonal del backend.

------------------------------------------------------------------------

## Enfoque modular

-   Cada **módulo del frontend** corresponde a un **módulo del backend**
    (ej. productos, pedidos, usuarios, reportes).
-   Dentro de cada módulo se incluyen:
    -   **components** → componentes visuales reutilizables del módulo.
    -   **pages** → páginas específicas que se enrutan desde React
        Router.
    -   **services** → llamadas HTTP al backend (usando `apiClient`).
    -   **hooks** → lógica de negocio/estado encapsulada en hooks
        personalizados.
    -   **types** → definición de DTOs o estructuras de datos que maneja
        el módulo.

------------------------------------------------------------------------

## 📂 Estructura de carpetas propuesta

    src/
     ├─ modules/
     │   ├─ products/
     │   │   ├─ components/
     │   │   │    ├─ ProductCard.jsx
     │   │   │    └─ ProductForm.jsx
     │   │   ├─ pages/
     │   │   │    └─ ProductsPage.jsx
     │   │   ├─ services/
     │   │   │    └─ productService.js
     │   │   ├─ hooks/
     │   │   │    └─ useProducts.js
     │   │   └─ types.js
     │   │
     │   ├─ orders/
     │   │   ├─ components/
     │   │   │    └─ OrderTable.jsx
     │   │   ├─ pages/
     │   │   │    └─ OrdersPage.jsx
     │   │   ├─ services/
     │   │   │    └─ orderService.js
     │   │   ├─ hooks/
     │   │   │    └─ useOrders.js
     │   │   └─ types.js
     │   │
     │   ├─ users/
     │   │   ├─ components/
     │   │   │    └─ UserForm.jsx
     │   │   ├─ pages/
     │   │   │    └─ UsersPage.jsx
     │   │   ├─ services/
     │   │   │    └─ userService.js
     │   │   ├─ hooks/
     │   │   │    └─ useUsers.js
     │   │   └─ types.js
     │   │
     │   └─ reports/
     │       ├─ components/
     │       │    └─ ReportChart.jsx
     │       ├─ pages/
     │       │    └─ ReportsPage.jsx
     │       ├─ services/
     │       │    └─ reportService.js
     │       ├─ hooks/
     │       │    └─ useReports.js
     │       └─ types.js
     │
     ├─ shared/
     │   ├─ components/   # comunes a todos los módulos (Button, Modal, Navbar…)
     │   ├─ hooks/        # hooks globales (useForm, useAuth…)
     │   ├─ services/     # apiClient.js, interceptores de Axios
     │   └─ utils/        # helpers y funciones de apoyo
     │
     ├─ routes/           # definición de rutas centralizadas (React Router)
     ├─ App.jsx           # componente principal
     └─ main.jsx          # punto de entrada

------------------------------------------------------------------------

## Ejemplo: módulo `products`

    src/modules/products/
     ├─ components/
     │    ├─ ProductCard.jsx
     │    └─ ProductForm.jsx
     ├─ pages/
     │    └─ ProductsPage.jsx
     ├─ services/
     │    └─ productService.js   # llamadas HTTP a /api/products
     ├─ hooks/
     │    └─ useProducts.js      # lógica de estado/validación
     └─ types.js                 # definición de tipos de datos (ProductDTO)

------------------------------------------------------------------------

## Beneficios del enfoque modular

1.  **Alineación con backend**: cada módulo refleja un hexágono del
    backend.
2.  **Escalabilidad**: facilita mantener y agregar nuevas features sin
    romper otras.
3.  **Mantenibilidad**: código organizado, fácil de ubicar según el
    dominio.
4.  **Reutilización**: componentes y hooks compartidos viven en
    `shared`.
5.  **Preparación para futuro**: habilita migrar a microfrontends si es
    necesario.
