package com.imperialnet.foodstore.products.infrastructure.web.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imperialnet.foodstore.products.application.ports.in.CreateProductUseCase;
import com.imperialnet.foodstore.products.application.ports.in.GetAllProductsUseCase;
import com.imperialnet.foodstore.products.application.ports.in.UpdateProductUseCase;
import com.imperialnet.foodstore.products.application.ports.out.StoragePort;
import com.imperialnet.foodstore.products.domain.model.Product;
import com.imperialnet.foodstore.products.infrastructure.mapper.ProductMapper;
import com.imperialnet.foodstore.products.infrastructure.web.dto.CreateProductMultipartRequest;
import com.imperialnet.foodstore.products.infrastructure.web.dto.ProductResponse;
import com.imperialnet.foodstore.products.infrastructure.web.dto.CreateProductRequest;
import com.imperialnet.foodstore.config.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Operaciones relacionadas con la gestión de productos de la rotisería")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final GetAllProductsUseCase getAllProductsUseCase;
    private final StoragePort storagePort;
    private final ProductMapper productMapper;
    private final UpdateProductUseCase updateProductUseCase;

    // --- Endpoint para crear producto ---
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('DUENO', 'ENCARGADO')")
    @Operation(
            summary = "Registrar nuevo producto",
            description = "Registra un producto con sus datos y URL de imagen previamente procesada"
    )
    public ProductResponse createProduct(@Valid @RequestBody CreateProductRequest request, Authentication auth) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        MDC.put("action", "CREATE_PRODUCT");

        log.info("→El usuario> {} inicia la creacion de producto: {}", user.getFullName(), request.name());
        try {
            Product productRequest= productMapper.toDomain(request);
            Product created = createProductUseCase.create(productRequest);
            log.info("←El usuario> {} finaliza exitosamente la creacion de producto: {}", user.getFullName(), created.getName());
            return productMapper.toResponse(created);
        } finally {
            MDC.clear();
        }
    }

    // --- Endpoint para obtener todos los productos ---
    @Operation(
            summary = "Obtener todos los productos",
            description = "Obtiene todos los productos registrados en el catálogo. Disponible para roles ENCARGADO Y DUENO."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de productos obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('DUENO', 'ENCARGADO')")
    public List<ProductResponse> getAllProducts() {

        MDC.put("action", "GET_ALL_PRODUCTS");

        log.info("Obteniendo todos los productos del catálogo");

        try {
            // Ejecutar caso de uso
            List<Product> products = getAllProductsUseCase.getAll();

            // Mapear entidades a responses
            List<ProductResponse> responses = products.stream()
                    .map(product -> productMapper.toResponse(product)
                    )
                    .toList();

            log.info("Se han obtenido: {} productos del catálogo", responses.size());

            return responses;

        } catch (Exception e) {
            log.error("Error al obtener todos los productos. Causa: {}", e.getMessage(), e);
            throw e;
        } finally {
            MDC.clear();
        }
    }

    //endpoint para actualizar un producto
    @Operation(
            summary = "Actualizar un producto",
            description = "Actualiza los datos de un producto existente. Disponible para roles DUENO y ENCARGADO."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Producto actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
    )
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('DUENO', 'ENCARGADO')")
    @PutMapping("/update/{id}")
    public ProductResponse updateProduct(
            @Parameter(description = "ID del producto a actualizar", required = true)
            @PathVariable Long id,
            @Valid @RequestBody CreateProductRequest request,
            Authentication auth) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        MDC.put("action", "UPDATE_PRODUCT");

            log.info("→ El Usuario: {}, incia actualizacion de producto: {}", user.getFullName(), request.name());
            try {
                Product productRequest= productMapper.toDomain(request);
                Product updated = updateProductUseCase.update(id, productRequest);
                log.info("← El Usuario: {}, finaliza exitosamente actualizacion de producto: {}", user.getFullName(), updated.getName());
                return productMapper.toResponse(updated);
            } finally {
                MDC.clear();
            }

    }
}
