package com.imperialnet.foodstore.publicapi.infrastructure.web.in;

import com.imperialnet.foodstore.business.application.port.in.GetCurrentBusinessUseCase;
import com.imperialnet.foodstore.business.infrastructure.mapper.BusinessWebMapper;
import com.imperialnet.foodstore.business.infrastructure.web.dto.BusinessResponse;
import com.imperialnet.foodstore.orders.application.ports.in.CreateOrderUseCase;
import com.imperialnet.foodstore.orders.domain.model.Order;
import com.imperialnet.foodstore.orders.infrastructure.web.dto.OrderRequest;
import com.imperialnet.foodstore.orders.infrastructure.web.dto.OrderResponse;
import com.imperialnet.foodstore.orders.infrastructure.web.mapper.OrderMapper;
import com.imperialnet.foodstore.products.infrastructure.mapper.CategoryMapper;
import com.imperialnet.foodstore.products.infrastructure.mapper.ProductMapper;
import com.imperialnet.foodstore.products.infrastructure.web.dto.CategoryResponse;
import com.imperialnet.foodstore.products.infrastructure.web.dto.ProductResponse;
import com.imperialnet.foodstore.publicapi.application.ports.in.GetActiveCategoriesUseCase;
import com.imperialnet.foodstore.publicapi.application.ports.in.GetActiveProductsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.imperialnet.foodstore.business.application.port.in.GetBusinessHoursUseCase;
import com.imperialnet.foodstore.business.infrastructure.mapper.BusinessHourMapper;
import com.imperialnet.foodstore.business.infrastructure.web.dto.BusinessHourDTO;

@Slf4j
@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
@Tag(name = "Enpoints Público", description = "Endpoints públicos (no requieren autenticación)")
public class PublicController {

    private final GetActiveProductsUseCase getAllProductsUseCase;
    private final GetActiveCategoriesUseCase getAllCategoriesUseCase;
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final GetBusinessHoursUseCase getBusinessHoursUseCase;
    private final BusinessHourMapper businessHourMapper;
    private final GetCurrentBusinessUseCase getCurrentBusinessUseCase;
    private final BusinessWebMapper businessMapper;
    private final CreateOrderUseCase createOrderUseCase;
    private final OrderMapper orderMapper;


    // --- Endpoint público para obtener solo productos activos --- (PUBLICO)
    @Operation(
            summary = "Obtener productos activos (público)",
            description = "Devuelve únicamente los productos con estado activo. Acceso público, sin autenticación."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de productos activos obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getProducts")
    public List<ProductResponse> getActiveProductsPublic() {
        MDC.put("action", "GET_ACTIVE_PRODUCTS_PUBLIC");
        log.info("Obteniendo productos activos (endpoint público)");
        try {
            List<ProductResponse >products= getAllProductsUseCase.getActiveProducts().stream()
                    .map(productMapper::toResponse)
                    .toList();

            log.info("Se han obtenido: {} productos ACTIVOS (público)", products.size());
            return  products;
        } catch (Exception e) {
            log.error("Error al obtener productos activos (público). Causa: {}", e.getMessage(), e);
            throw e;
        } finally {
            MDC.clear();
        }
    }



    // --- Endpoint público para obtener solo categorías activas (PUBLICO)---
    @Operation(
            summary = "Obtener categorías activas (público)",
            description = "Devuelve únicamente las categorías con estado activo. Acceso público, sin autenticación."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de categorías activas obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = CategoryResponse.class))
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getCategories")
    public List<CategoryResponse> getActiveCategoriesPublic() {
        MDC.put("action", "GET_ACTIVE_CATEGORIES_PUBLIC");
        log.info("Obteniendo categorías activas (endpoint público)");
        try {
            List<CategoryResponse> categories = getAllCategoriesUseCase.getActiveCategories().stream().map( categoryMapper::toResponse).toList();

            log.info("Se han obtenido: {} categorías ACTIVAS (público)", categories.size());
            return categories;
        } catch (Exception e) {
            log.error("Error al obtener categorías activas (público). Causa: {}", e.getMessage(), e);
            throw e;
        } finally {
            MDC.clear();
        }
    }


    // --- Endpoint público para obtener horarios de atención (PUBLICO) ---
    @Operation(
            summary = "Obtener horarios de atención (público)",
            description = "Devuelve todos los horarios de atención configurados. Acceso público, sin autenticación."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de horarios de atención obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = BusinessHourDTO.class))
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getBusinessHours")
    public List<BusinessHourDTO> getBusinessHoursPublic() {
        MDC.put("action", "GET_BUSINESS_HOURS_PUBLIC");
        log.info("Obteniendo horarios de atención (endpoint público)");
        try {
            List<BusinessHourDTO> hours = getBusinessHoursUseCase.getAll().stream()
                    .map(businessHourMapper::toDto)
                    .toList();
            log.info("Se han obtenido: {} horarios de atención (público)", hours.size());
            return hours;
        } catch (Exception e) {
            log.error("Error al obtener horarios de atención (público). Causa: {}", e.getMessage(), e);
            throw e;
        } finally {
            MDC.clear();
        }
    }



    // --- Endpoint público para los datos de la empresa ---
    @Operation(
            summary = "Obtener datos la empresa (público)",
            description = "Devuelve los datos configurados de la empresa. Acceso público, sin autenticación."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Datos de la empresa obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = BusinessResponse.class))
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getBusinessData")
    public BusinessResponse getBusinessDataPublic() {

        MDC.put("action", "GET_BUSINESS_DATA_PUBLIC");
        log.info("Obteniendo los datos de la empresa (endpoint público)");
        try {
            BusinessResponse response = businessMapper.toResponse(getCurrentBusinessUseCase.getCurrentBusiness());


            log.info("Se han obtenido los datos de la empresa: {} (público)", response.getName());
            return response;
        } catch (Exception e) {
            log.error("Error al obtener los datos de la empresa (público). Causa: {}", e.getMessage(), e);
            throw e;
        } finally {
            MDC.clear();
        }
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
    @PostMapping("/createOrder")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@Valid @RequestBody OrderRequest request) {
        MDC.put("action", "CREATE_ORDER");
        long start = System.nanoTime();
        try {
            log.debug("Received createOrder request");
            log.debug("Request payload: {}", request);

            // 1️⃣ Mapear DTO → Dominio
            Order order = orderMapper.toDomain(request);
            log.debug("Mapped to domain Order: {}", order);

            // 2️⃣ Ejecutar caso de uso
            Order createdOrder = createOrderUseCase.createOrder(order);
            log.info("Order created with id (if available): {}", createdOrder != null ? createdOrder.getId() : "null");
            log.debug("Created domain order: {}", createdOrder);

            // 3️⃣ Mapear Dominio → DTO de respuesta
            OrderResponse response = orderMapper.toResponse(createdOrder);
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




}
