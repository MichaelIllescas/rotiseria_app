# 📊 Matriz de Objetivos y Restricciones – Plataforma Web para Rotisería

## Objetivos
1. **Digitalizar pedidos y stock** en tiempo real para reducir errores operativos.  
2. **Ofrecer compras online sin registro**, con un flujo ágil para el cliente.  
3. **Integrar pagos digitales y contraentrega** para ampliar las opciones del cliente.  
4. **Generar reportes de ventas y productos más vendidos** para mejorar la toma de decisiones.  
5. **Garantizar disponibilidad ≥99%** en horario comercial con tiempos de respuesta <300ms p95.  

## Restricciones
1. **Plazo:** la primera versión (MVP) debe estar lista en un tiempo reducido, priorizando funcionalidades críticas (catálogo, pedidos, pagos, stock).  
2. **Presupuesto:** limitado, orientado a una PyME; se debe optimizar costos de infraestructura (uso de VPS con Docker).  
3. **Tecnología disponible:**  
   - Backend en **Java Spring Boot** con arquitectura hexagonal modular.  
   - Frontend en **React** modular.  
   - Base de datos **MySQL**.  
   - Despliegue en VPS con **Docker** y **NGINX**.  
4. **Integraciones externas:** dependencia de **Mercado Pago API** para cobros online.  
5. **Recursos humanos:** equipo pequeño (1-2 desarrolladores).  
6. **Operación:** disponibilidad y soporte acotado, con monitoreo básico y backups programados.  
