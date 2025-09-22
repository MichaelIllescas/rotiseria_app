# 📐 Arquitectura Modular Hexagonal -- Plataforma Web para Rotisería

Este documento describe cómo organizar el backend en **módulos
independientes**, cada uno siguiendo el patrón **Hexagonal (Ports &
Adapters)**.

------------------------------------------------------------------------

## Enfoque

-   Cada **módulo de negocio** (ej. productos, pedidos, usuarios,
    reportes) es un **hexágono independiente**.
-   Cada módulo incluye:
    -   **Dominio**: entidades y lógica de negocio.
    -   **Aplicación**: casos de uso + puertos (in/out).
    -   **Infraestructura**: adaptadores entrantes y salientes.
-   Los módulos se comunican **a través de sus puertos**, nunca
    directamente con el dominio de otro módulo.

------------------------------------------------------------------------

## 📂 Estructura de carpetas propuesta

    com.rotiseria
     ├─ products
     │   ├─ domain
     │   │    ├─ model
     │   │    │    ├─ Product.java
     │   │    │    └─ Category.java
     │   │    └─ service
     │   │         └─ StockService.java
     │   ├─ application
     │   │    ├─ ports
     │   │    │    ├─ in
     │   │    │    │    ├─ CreateProductUseCase.java
     │   │    │    │    └─ UpdateStockUseCase.java
     │   │    │    └─ out
     │   │    │         └─ ProductRepositoryPort.java
     │   │    └─ usecase
     │   │         ├─ CreateProductService.java
     │   │         └─ UpdateStockService.java
     │   └─ infrastructure
     │        ├─ persistence
     │        │    ├─ ProductEntity.java
     │        │    └─ ProductRepositoryAdapter.java
     │        └─ web
     │             ├─ ProductController.java
     │             ├─ ProductDTO.java
     │             └─ ProductMapper.java
     │
     ├─ orders
     │   ├─ domain
     │   │    ├─ Order.java
     │   │    ├─ OrderItem.java
     │   │    └─ OrderService.java
     │   ├─ application
     │   │    ├─ ports
     │   │    │    ├─ in
     │   │    │    │    └─ PlaceOrderUseCase.java
     │   │    │    └─ out
     │   │    │         └─ OrderRepositoryPort.java
     │   │    └─ usecase
     │   │         └─ PlaceOrderService.java
     │   └─ infrastructure
     │        ├─ persistence (OrderEntity, OrderRepositoryAdapter)
     │        └─ web (OrderController, DTOs, mappers)
     │
     ├─ users
     │   ├─ domain (User.java, Role.java)
     │   ├─ application
     │   │    ├─ ports (UserRepositoryPort.java, AuthUseCase.java)
     │   │    └─ usecase (AuthService.java)
     │   └─ infrastructure
     │        ├─ persistence (UserEntity, UserRepositoryAdapter)
     │        ├─ web (UserController, DTOs)
     │        └─ security (JwtProvider, JwtFilter)
     │
     ├─ reports
     │   ├─ domain (ReportService.java)
     │   ├─ application
     │   │    ├─ ports (ReportQueryPort.java)
     │   │    └─ usecase (GenerateReportService.java)
     │   └─ infrastructure
     │        ├─ query (SQL queries, CSV export)
     │        └─ web (ReportController, DTOs)
     │
     └─ shared
         ├─ exception (CustomException, GlobalHandler)
         └─ util (helpers reutilizables)

------------------------------------------------------------------------

## Beneficios de este enfoque

1.  **Aislamiento**: cada módulo tiene su propio dominio y lógica, sin
    mezclarse con otros.
2.  **Escalabilidad**: si un día se necesita migrar `orders` a un
    microservicio, se extrae el módulo sin grandes cambios.
3.  **Testabilidad**: los puertos permiten mockear dependencias y
    testear los casos de uso en aislamiento.
4.  **Mantenibilidad**: equipos distintos pueden trabajar en módulos
    separados sin pisarse.
5.  **Evolución futura**: se pueden agregar nuevos módulos (ej.
    notificaciones, fidelización) siguiendo el mismo patrón.

------------------------------------------------------------------------

## Notas finales

-   Mantener **dependencias unidireccionales**:
    `infraestructura → aplicación → dominio`.
-   **Nunca** un módulo debe llamar directamente al dominio de otro.
    Debe hacerlo vía **puertos compartidos**.
-   Esta organización sigue principios de **DDD (Domain-Driven Design)**
    aplicados en un **monolito modular**.
