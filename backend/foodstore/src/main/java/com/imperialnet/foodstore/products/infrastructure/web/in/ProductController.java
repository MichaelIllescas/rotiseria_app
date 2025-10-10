package com.imperialnet.foodstore.products.infrastructure.web.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imperialnet.foodstore.products.application.ports.in.*;
import com.imperialnet.foodstore.products.application.ports.out.StoragePort;
import com.imperialnet.foodstore.products.domain.model.Product;
import com.imperialnet.foodstore.products.infrastructure.mapper.ProductMapper;
import com.imperialnet.foodstore.products.infrastructure.web.dto.CreateProductMultipartRequest;
import com.imperialnet.foodstore.products.infrastructure.web.dto.ProductResponse;
import com.imperialnet.foodstore.products.infrastructure.web.dto.CreateProductRequest;
import com.imperialnet.foodstore.config.security.CustomUserDetails;
import com.imperialnet.foodstore.products.infrastructure.web.dto.UpdateStockRequest;
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
    private final DeleteProductUseCase deleteProductUseCase;
    private final ToggleProducStatusUseCase toggleProducStatusUseCase;
    private final BulkUpdateStockUseCase bulkUpdateStockUseCase;
    private final UpdateStockUseCase updateStockUseCase;

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

    // --- Endpoint público para obtener solo productos activos ---
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
    @GetMapping("/active")
    public List<ProductResponse> getActiveProductsPublic() {
        MDC.put("action", "GET_ACTIVE_PRODUCTS_PUBLIC");
        log.info("Obteniendo productos activos (endpoint público)");
        try {
            List<Product> products = getAllProductsUseCase.getAll();
            return products.stream()
                    .filter(Product::isActive)
                    .map(productMapper::toResponse)
                    .toList();
        } catch (Exception e) {
            log.error("Error al obtener productos activos (público). Causa: {}", e.getMessage(), e);
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

    // endpoint para eliminar un producto
    @Operation (
            summary = "Eliminar un producto",
            description = "Elimina un producto existente del catálogo. Disponible para roles DUENO y ENCARGADO."
    )
    @ApiResponse (
            responseCode = "204",
            description = "Producto eliminado exitosamente"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('DUENO', 'ENCARGADO')")
    @DeleteMapping("/delete/{id}")
    public void deleteProduct(
            @Parameter(description = "ID del producto a eliminar", required = true)
            @PathVariable Long id,
            Authentication auth) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        MDC.put("action", "DELETE_PRODUCT");

        log.info("→ El Usuario: {}, incia eliminacion de producto id: {}", user.getFullName(), id);
        try {
            deleteProductUseCase.delete(id);
            log.info("← El Usuario: {}, finaliza exitosamente eliminacion de producto id: {}", user.getFullName(), id);
        } finally {
            MDC.clear();
        }
    }

    // endpoint para cambiar el estado de un producto (activo/inactivo)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('DUENO', 'ENCARGADO')")
    @PutMapping("/toggleStatus/{id}")
    @Operation(
            summary = "Alternar estado de un producto(Activo/Inactivo)",
            description = "Alterna el estado de un producto entre activo e inactivo. Disponible para roles DUENO y ENCARGADO."
    )
    public void toggleProductStatus(
            @Parameter(description = "ID del producto cuyo estado se va a alternar", required = true)
            @PathVariable Long id,
            Authentication auth) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        MDC.put("action", "TOGGLE_PRODUCT_STATUS");

        log.info("→ El Usuario: {}, incia alternar estado de producto id: {}", user.getFullName(), id);
        try {
            // Llamar al caso de uso para alternar el estado del producto
            toggleProducStatusUseCase.execute(id);
            log.info("← El Usuario: {}, finaliza exitosamente alternar estado de producto id: {}", user.getFullName(), id);
        } finally {
            MDC.clear();
        }
    }

    // Endpoint para modificar el stock de un listado de productos.
    @PutMapping("/updateBulkStocks")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('DUENO', 'ENCARGADO')")
    @Operation(
            summary = "Modificar stock de productos a granel",
            description = "Modifica el stock de un listado de productos. Disponible para roles DUENO y ENCARGADO."
    )
    public void updateStock(
            @Valid @RequestBody List<UpdateStockRequest> requests,
            Authentication auth
    ) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

        MDC.put("action", "UPDATE_PRODUCTS_BULK_STOCK");
        MDC.put("user", user.getFullName());

        log.info("→ Inicio de modificación masiva de stock por el usuario: {}", user.getFullName());

        try {
            // Convertir DTOs a objetos del comando
            var items = requests.stream()
                    .map(req -> new UpdateStockCommand.ProductStockItem(req.productId(), req.stock()))
                    .toList();

            var command = new UpdateStockCommand(items);

            // Llamar al caso de uso masivo
            bulkUpdateStockUseCase.bulkUpdateStock(command);

            log.info("← Finaliza exitosamente la modificación masiva de stock por el usuario: {}", user.getFullName());

        } catch (Exception ex) {
            log.error("✖ Error al modificar stock de productos por el usuario: {} → {}", user.getFullName(), ex.getMessage(), ex);
            throw ex;
        } finally {
            MDC.clear();
        }
    }

    // endpoint para actualizar stock de un proucto
    @Operation(
            summary = "Actualizar stock de un producto",
            description = "Actualiza el stock diario de un producto específico. Disponible para roles DUENO y ENCARGADO."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Stock del producto actualizado exitosamente")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('DUENO', 'ENCARGADO')")
    @PutMapping("/updateStock")
    public void updateStock(
            @Valid @RequestBody UpdateStockRequest request,
            Authentication auth) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        MDC.put("action", "UPDATE_PRODUCT_STOCK");

        log.info("→ El Usuario: {}, incia actualizacion de stock del producto id: {}", user.getFullName(), request.productId());
        try {
            // Llamar al caso de uso para actualizar el stock del producto
            updateStockUseCase.updateStock(request.productId(), request.stock());
            log.info("← El Usuario: {}, finaliza exitosamente actualizacion de stock del producto id: {}", user.getFullName(), request.productId());
        } finally {
            MDC.clear();
        }
    }



}
