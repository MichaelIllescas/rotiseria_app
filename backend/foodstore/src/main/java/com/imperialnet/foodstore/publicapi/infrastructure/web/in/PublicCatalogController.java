package com.imperialnet.foodstore.publicapi.infrastructure.web.in;

import com.imperialnet.foodstore.products.domain.model.Category;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController // Desactivado para evitar fallos de arranque
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicCatalogController {

    private final GetActiveProductsUseCase getAllProductsUseCase;
    private final GetActiveCategoriesUseCase getAllCategoriesUseCase;
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;


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






}
