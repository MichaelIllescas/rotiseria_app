// java
package com.imperialnet.foodstore.orders.infrastructure.web.controller;

import com.imperialnet.foodstore.orders.application.ports.in.CreateOrderUseCase;
import com.imperialnet.foodstore.orders.application.ports.in.GetAllOrdersUseCase;
import com.imperialnet.foodstore.orders.domain.model.Order;
import com.imperialnet.foodstore.orders.infrastructure.web.dto.OrderRequest;
import com.imperialnet.foodstore.orders.infrastructure.web.dto.OrderResponse;
import com.imperialnet.foodstore.orders.infrastructure.web.mapper.OrderMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
@Tag(name = "Orders", description = "APIs para gestionar órdenes de pedido")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetAllOrdersUseCase getAllOrdersUseCase;
    private final OrderMapper mapper;

    public OrderController(CreateOrderUseCase createOrderUseCase, OrderMapper mapper, GetAllOrdersUseCase getAllOrdersUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.getAllOrdersUseCase = getAllOrdersUseCase;
        this.mapper = mapper;
    }

    /**
     * Endpoint para registrar una nueva orden.
     *
     * @param request DTO de creación de orden recibido desde el frontend.
     * @return la orden creada con sus ítems completos.
     */
    @Operation(summary = "Crear orden", description = "Registra una nueva orden y devuelve la orden creada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/public/create")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@Valid @RequestBody OrderRequest request) {
        MDC.put("action", "CREATE_ORDER");
        long start = System.nanoTime();
        try {
            log.debug("Received createOrder request");
            log.debug("Request payload: {}", request);

            // 1️⃣ Mapear DTO → Dominio
            Order order = mapper.toDomain(request);
            log.debug("Mapped to domain Order: {}", order);

            // 2️⃣ Ejecutar caso de uso
            Order createdOrder = createOrderUseCase.createOrder(order);
            log.info("Order created with id (if available): {}", createdOrder != null ? createdOrder.getId() : "null");
            log.debug("Created domain order: {}", createdOrder);

            // 3️⃣ Mapear Dominio → DTO de respuesta
            OrderResponse response = mapper.toResponse(createdOrder);
            log.debug("Mapped to response DTO: {}", response);

            long durationMs = (System.nanoTime() - start) / 1_000_000;
            log.info("CREATE_ORDER completed in {} ms", durationMs);

            // 4️⃣ Devolver respuesta HTTP 201 Created
            return response;
        } catch (IllegalArgumentException ex) {
            long durationMs = (System.nanoTime() - start) / 1_000_000;
            log.warn("Validation error while creating order after {} ms: {}", durationMs, ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            long durationMs = (System.nanoTime() - start) / 1_000_000;
            log.error("Unexpected error while creating order after {} ms", durationMs, ex);
            throw ex;
        } finally {
            MDC.clear();
        }
    }



    /**
     * Endpoint para obener todas las ordenes.
     *
     * @return listado de ordenes con sus items.
     */
    @Operation(summary = "Obtener todas las órdenes", description = "Recupera todas las órdenes registradas en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Órdenes recuperadas correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getAllOrders() {
        MDC.put("action", "GET_ALL_ORDERS");
        long start = System.nanoTime();
        try {
            log.debug("Solicitud para obtener todas las órdenes");

            // 1️⃣ Ejecutar caso de uso para obtener todas las órdenes
            List<Order> orders = getAllOrdersUseCase.getAllOrders();
            log.debug("Se han recuperado {} ordenes", orders.size());

            // 2️⃣ Mapear Dominio → DTO de respuesta
            List<OrderResponse> response = orders.stream()
                    .map(mapper::toResponse)
                    .collect(Collectors.toList());

            long durationMs = (System.nanoTime() - start) / 1_000_000;
            log.info("GET_ALL_ORDERS returned {} orders in {} ms", response.size(), durationMs);

            // 3️⃣ Devolver respuesta HTTP 200 OK
            return response;
        } catch (Exception ex) {
            long durationMs = (System.nanoTime() - start) / 1_000_000;
            log.error("Error inesperado al recuperar las ordenes after {} ms", durationMs, ex);
            throw ex;
        } finally {
            MDC.clear();
        }

    }


}
