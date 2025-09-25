# 📑 Diseño Arquitectónico de Logging – Plataforma Rotisería

Este documento define el **patrón de logging** a implementar en el monolito modular de la Rotisería, siguiendo buenas prácticas de **observabilidad** y los requisitos no funcionales del sistema.

---

## 🎯 Objetivos del Logging
1. **Observabilidad (RNF5):** logs estructurados, trazabilidad de errores y métricas.
2. **Correlación por request:** cada petición debe poder seguirse de principio a fin (requestId).
3. **Auditoría:** cambios sensibles deben registrarse (productos, stock, pedidos, usuarios).
4. **Diagnóstico:** logs útiles para detectar problemas en producción.

---

## 📐 Patrón propuesto

### 1. Niveles de log
- `TRACE`: depuración muy detallada (no en prod).
- `DEBUG`: detalles técnicos (dev).
- `INFO`: eventos de negocio esperados (ej. creación de pedido).
- `WARN`: anomalías no críticas (stock bajo, pedido cancelado).
- `ERROR`: fallos graves o excepciones inesperadas.

### 2. Formato estructurado (JSON)
Campos mínimos:
- `timestamp`
- `level`
- `requestId`
- `userId` (si aplica)
- `action` (ej. CREATE_ORDER)
- `message`
- `exception` (si corresponde)

Ejemplo:
```json
{
  "timestamp": "2025-09-25T15:33:12Z",
  "level": "INFO",
  "requestId": "abc123",
  "userId": "7",
  "action": "CREATE_ORDER",
  "entityId": "1045",
  "message": "Order created successfully",
  "durationMs": 245
}
```

### 3. Ubicación de logs por capa
- **Controllers (adapters entrantes):**
  - Generan `requestId`.
  - Log inicio/fin request (INFO).
  - Log inputs simplificados (nunca contraseñas).
- **Use cases (application):**
  - Log eventos de negocio (INFO).
  - Log condiciones especiales (WARN).
- **Dominio:**
  - No logs directos (mantener limpio).
- **Adapters salientes (persistencia, integraciones):**
  - Log `DEBUG` para queries/calls externas.
  - Log `ERROR` si falla integración.

### 4. Seguridad
Además de la auditoría de negocio, se deben registrar eventos de **seguridad**:
- Logins exitosos y fallidos (sin mostrar contraseñas).
- Intentos de acceso denegado (WARN).
- Creación/desactivación de usuarios (INFO).
- Cambios de configuración sensible (INFO/ERROR).

Estos logs permiten detectar intentos de intrusión y cumplir con políticas de trazabilidad.

### 5. Retención y rotación
- **Dev:** logs en consola.
- **Prod:** logs en archivo JSON, rotación diaria, retención 14 días.

---

## 🔧 Estrategia por entorno

### application-dev.properties
```properties
logging.level.root=DEBUG
logging.level.com.rotiseria=DEBUG
```

### application-prod.properties
```properties
logging.level.root=INFO
logging.level.com.rotiseria=INFO
```

### logback-spring.xml (extracto)
```xml
<configuration>
    <springProfile name="dev">
        <root level="DEBUG">
            <appender-ref ref="CONSOLE"/>
        </root>
    </springProfile>

    <springProfile name="prod">
        <root level="INFO">
            <appender-ref ref="FILE_JSON"/>
        </root>
    </springProfile>
</configuration>
```

---

## ✅ Casos críticos a loguear
- Autenticación (login/logout).
- Creación, actualización y cancelación de pedidos.
- Cambios de stock y productos.
- Creación / desactivación de usuarios.
- Integraciones con Mercado Pago.
- Errores no controlados (stacktrace completo).

---

## 📌 Conclusión
Este patrón asegura que el sistema:
- Cumpla con los **requisitos no funcionales de observabilidad**.
- Permita **auditoría y trazabilidad**.
- Tenga diferenciación clara entre entornos (dev/prod).
- Registre también **eventos de seguridad**, fundamentales en un sistema con roles.

