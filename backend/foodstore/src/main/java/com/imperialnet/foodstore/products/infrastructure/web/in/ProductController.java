package com.imperialnet.foodstore.products.infrastructure.web.in;

import com.imperialnet.foodstore.products.application.ports.in.CreateProductUseCase;
import com.imperialnet.foodstore.products.application.ports.in.GetAllProductsUseCase;
import com.imperialnet.foodstore.products.domain.model.Product;
import com.imperialnet.foodstore.products.infrastructure.web.dto.ProductResponse;
import com.imperialnet.foodstore.products.infrastructure.web.dto.CreateProductRequest;
import com.imperialnet.foodstore.config.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Operaciones relacionadas con la gestión de productos de la rotisería")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final GetAllProductsUseCase getAllProductsUseCase;

    // --- Endpoint para crear producto ---
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    @Operation(
            summary = "Crear un nuevo producto",
            description = "Crea un nuevo producto en el catálogo de la rotisería. Requiere autenticación de DUEÑO o ENCARGADO."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Producto creado exitosamente",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
    )
    @PreAuthorize("hasAnyRole('DUENO', 'ENCARGADO')")
    public ProductResponse createProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para crear un producto",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateProductRequest.class))
            ) @Valid @RequestBody CreateProductRequest request,
            Authentication auth) {

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String fullName = userDetails.getFullName();

        MDC.put("action", "CREATE_PRODUCT");
        MDC.put("productName", request.name());
        MDC.put("categoryId", String.valueOf(request.categoryId()));

        log.info("Usuario: {} está creando un nuevo producto con nombre: {} para categoría ID: {}",
                fullName, request.name(), request.categoryId());

        try {
            // Mapear request a entidad de dominio
            Product product = new Product(
                    null, // ID será asignado por el repositorio
                    request.categoryId(),
                    request.name(),
                    request.description(),
                    request.price(),
                    request.imageUrl(),
                    request.dailyStock(),
                    true // Por defecto se crea activo
            );

            // Ejecutar caso de uso
            Product createdProduct = createProductUseCase.create(product);

            // Mapear entidad a response
            ProductResponse response = new ProductResponse(
                    createdProduct.getId(),
                    createdProduct.getCategoryId(),
                    createdProduct.getName(),
                    createdProduct.getDescription(),
                    createdProduct.getPrice(),
                    createdProduct.getImageUrl(),
                    createdProduct.getDailyStock(),
                    createdProduct.isActive()
            );

            log.info("Usuario: {} ha creado un nuevo producto con ID: {} y nombre: {}",
                    fullName, response.id(), response.name());

            return response;

        } catch (Exception e) {
            log.error("Error al crear producto con nombre: {} por parte de: {}. Causa: {}",
                    request.name(), fullName, e.getMessage());
            throw e;
        } finally {
            MDC.clear();
        }
    }

    // --- Endpoint para obtener todos los productos ---
    @Operation(
            summary = "Obtener todos los productos",
            description = "Obtiene todos los productos registrados en el catálogo. Disponible para todos los roles autenticados."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de productos obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('DUENO', 'ENCARGADO', 'ATENCION')")
    public List<ProductResponse> getAllProducts() {

        MDC.put("action", "GET_ALL_PRODUCTS");

        log.info("Obteniendo todos los productos del catálogo");

        try {
            // Ejecutar caso de uso
            List<Product> products = getAllProductsUseCase.getAll();

            // Mapear entidades a responses
            List<ProductResponse> responses = products.stream()
                    .map(product -> new ProductResponse(
                            product.getId(),
                            product.getCategoryId(),
                            product.getName(),
                            product.getDescription(),
                            product.getPrice(),
                            product.getImageUrl(),
                            product.getDailyStock(),
                            product.isActive()
                    ))
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
}
