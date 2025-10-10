package com.imperialnet.foodstore.publicapi.infrastructure.web;

import com.imperialnet.foodstore.products.application.ports.in.GetAllProductsUseCase;
import com.imperialnet.foodstore.products.domain.model.Product;
import com.imperialnet.foodstore.products.infrastructure.mapper.ProductMapper;
import com.imperialnet.foodstore.products.infrastructure.web.dto.ProductResponse;
import com.imperialnet.foodstore.publicapi.application.ports.in.GetActiveCategoriesUseCase;
import com.imperialnet.foodstore.products.domain.model.Category;
import com.imperialnet.foodstore.products.infrastructure.mapper.CategoryMapper;
import com.imperialnet.foodstore.products.infrastructure.web.dto.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
//@RestController // Desactivado: evitar colisión de rutas
//@RequestMapping("/api/public") // Desactivado
@RequiredArgsConstructor
@Tag(name = "Catálogo Público", description = "Endpoints públicos del catálogo (no requieren autenticación)")
public class PublicCatalogController {

    private final GetAllProductsUseCase getAllProductsUseCase;
    private final ProductMapper productMapper;

    private final GetActiveCategoriesUseCase getActiveCategoriesUseCase;
    private final CategoryMapper categoryMapper;

    // Productos activos (público)
    @Operation(
            summary = "Listar productos activos (público)",
            description = "Devuelve únicamente los productos con estado activo, sin requerir autenticación."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de productos activos",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/products/active")
    public List<ProductResponse> getActiveProducts() {
        MDC.put("action", "PUBLIC_GET_ACTIVE_PRODUCTS");
        log.info("Obteniendo productos activos (público)");
        try {
            List<Product> products = getAllProductsUseCase.getAll();
            return products.stream()
                    .filter(Product::isActive)
                    .map(productMapper::toResponse)
                    .toList();
        } finally {
            MDC.clear();
        }
    }

    // Categorías activas (público)
    @Operation(
            summary = "Listar categorías activas (público)",
            description = "Devuelve únicamente las categorías activas, sin requerir autenticación."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de categorías activas",
            content = @Content(schema = @Schema(implementation = CategoryResponse.class))
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/categories/active")
    public List<CategoryResponse> getActiveCategories() {
        MDC.put("action", "PUBLIC_GET_ACTIVE_CATEGORIES");
        log.info("Obteniendo categorías activas (público)");
        try {
            List<Category> categories = getActiveCategoriesUseCase.getActiveCategories();
            return categories.stream().map(categoryMapper::toResponse).toList();
        } finally {
            MDC.clear();
        }
    }
}
