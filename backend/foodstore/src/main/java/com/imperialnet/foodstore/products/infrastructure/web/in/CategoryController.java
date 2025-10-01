package com.imperialnet.foodstore.products.infrastructure.web.in;

import com.imperialnet.foodstore.products.application.ports.in.CreateCategoryUseCase;
import com.imperialnet.foodstore.products.application.ports.in.GetAllCategoriesUseCase;
import com.imperialnet.foodstore.products.application.ports.in.GetCategoryByIdUseCase;
import com.imperialnet.foodstore.products.application.ports.in.UpdateCategoryUseCase;
import com.imperialnet.foodstore.products.application.ports.in.DeleteCategoryUseCase;
import com.imperialnet.foodstore.products.application.ports.in.ToggleCategoryStatusUseCase;
import com.imperialnet.foodstore.products.domain.model.Category;
import com.imperialnet.foodstore.products.infrastructure.mapper.CategoryMapper;
import com.imperialnet.foodstore.products.infrastructure.web.dto.CategoryResponse;
import com.imperialnet.foodstore.products.infrastructure.web.dto.CreateCategoryRequest;
import com.imperialnet.foodstore.products.infrastructure.web.dto.UpdateCategoryRequest;
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
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Categorías", description = "Operaciones relacionadas con la gestión de categorías de productos")
public class CategoryController {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetAllCategoriesUseCase getAllCategoriesUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final ToggleCategoryStatusUseCase toggleCategoryStatusUseCase;
    private final CategoryMapper categoryMapper;

    // --- Endpoint para crear categoría ---
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    @Operation(
            summary = "Crear una nueva categoría",
            description = "Crea una nueva categoría de productos en el sistema. Requiere autenticación de DUEÑO o ENCARGADO."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Categoría creada exitosamente",
            content = @Content(schema = @Schema(implementation = CategoryResponse.class))
    )
    @PreAuthorize("hasAnyRole('DUENO', 'ENCARGADO')")
    public CategoryResponse createCategory(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para crear una categoría",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateCategoryRequest.class))
            ) @Valid @RequestBody CreateCategoryRequest request,
            Authentication auth) {

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String fullName = userDetails.getFullName();

        MDC.put("action", "CREATE_CATEGORY");
        MDC.put("categoryName", request.name());

        log.info("Usuario: {} está creando una nueva categoría con nombre: {}", fullName, request.name());

        try {
            // Mapear request a entidad de dominio
            Category category = new Category(null, request.name(), request.description(), true);

            // Ejecutar caso de uso
            Category createdCategory = createCategoryUseCase.create(category);

            // Mapear entidad a response usando el mapper
            CategoryResponse response = categoryMapper.toResponse(createdCategory);

            log.info("Usuario: {} ha creado una nueva categoría con ID: {} y nombre: {}",
                    fullName, response.id(), response.name());

            return response;

        } catch (Exception e) {
            log.error("Error al crear categoría con nombre: {} por parte de: {}. Causa: {}",
                    request.name(), fullName, e.getMessage());
            throw e;
        } finally {
            MDC.clear();
        }
    }

    // --- Endpoint para obtener todas las categorías ---
    @Operation(
            summary = "Obtener todas las categorías",
            description = "Obtiene todas las categorías registradas en el sistema. Disponible para todos los roles autenticados."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de categorías obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = CategoryResponse.class))
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('DUENO', 'ENCARGADO', 'ATENCION')")
    public List<CategoryResponse> getAllCategories() {

        MDC.put("action", "GET_ALL_CATEGORIES");

        log.info("Obteniendo todas las categorías del sistema");

        try {
            // Ejecutar caso de uso
            List<Category> categories = getAllCategoriesUseCase.getAll();

            // Mapear entidades a responses usando el mapper
            List<CategoryResponse> responses = categories.stream()
                    .map(categoryMapper::toResponse)
                    .toList();

            log.info("Se han obtenido: {} categorías del sistema", responses.size());

            return responses;

        } catch (Exception e) {
            log.error("Error al obtener todas las categorías. Causa: {}", e.getMessage(), e);
            throw e;
        } finally {
            MDC.clear();
        }
    }

    // --- Endpoint para obtener categoría por ID ---
    @Operation(
            summary = "Obtener categoría por ID",
            description = "Obtiene los detalles de una categoría específica mediante su ID. Disponible para todos los roles autenticados."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Categoría obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = CategoryResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Categoría no encontrada",
            content = @Content
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DUENO', 'ENCARGADO', 'ATENCION')")
    public CategoryResponse getCategoryById(@PathVariable("id") Long id) {

        MDC.put("action", "GET_CATEGORY_BY_ID");
        MDC.put("categoryId", String.valueOf(id));

        log.info("Obteniendo categoría con ID: {}", id);

        try {
            // Ejecutar caso de uso - ahora lanza CategoryNotFoundException si no existe
            Optional<Category> categoryOpt = getCategoryByIdUseCase.getById(id);
            Category category = categoryOpt.get(); // Siempre tendrá valor o se habrá lanzado excepción

            // Mapear entidad a response usando el mapper
            CategoryResponse response = categoryMapper.toResponse(category);

            log.info("Categoría obtenida: ID: {}, Nombre: {}", response.id(), response.name());

            return response;

        } finally {
            MDC.clear();
        }
    }

    // --- Endpoint para actualizar categoría ---
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/update/{id}")
    @Operation(
            summary = "Actualizar categoría",
            description = "Actualiza los datos de una categoría existente. Requiere autenticación de DUEÑO o ENCARGADO."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Categoría actualizada exitosamente",
            content = @Content(schema = @Schema(implementation = CategoryResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Categoría no encontrada",
            content = @Content
    )
    @PreAuthorize("hasAnyRole('DUENO', 'ENCARGADO')")
    public CategoryResponse updateCategory(
            @PathVariable("id") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados de la categoría",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateCategoryRequest.class))
            ) @Valid @RequestBody UpdateCategoryRequest request,
            Authentication auth) {

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String fullName = userDetails.getFullName();

        MDC.put("action", "UPDATE_CATEGORY");
        MDC.put("categoryId", String.valueOf(id));
        MDC.put("categoryName", request.name());

        log.info("Usuario: {} está actualizando la categoría con ID: {} y nombre: {}", fullName, id, request.name());

        try {
            // Mapear request a entidad de dominio
            Category category = new Category(id, request.name(), request.description(), request.active());

            // Ejecutar caso de uso
            Category updatedCategory = updateCategoryUseCase.update(id, category);

            // Mapear entidad a response usando el mapper
            CategoryResponse response = categoryMapper.toResponse(updatedCategory);

            log.info("Usuario: {} ha actualizado la categoría con ID: {} y nuevo nombre: {}",
                    fullName, response.id(), response.name());

            return response;

        } catch (Exception e) {
            log.error("Error al actualizar categoría con ID: {} por parte de: {}. Causa: {}",
                    id, fullName, e.getMessage());
            throw e;
        } finally {
            MDC.clear();
        }
    }

    // --- Endpoint para eliminar categoría ---
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Eliminar categoría",
            description = "Elimina una categoría existente del sistema. Requiere autenticación de DUEÑO o ENCARGADO."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Categoría eliminada exitosamente",
            content = @Content
    )
    @ApiResponse(
            responseCode = "404",
            description = "Categoría no encontrada",
            content = @Content
    )
    @PreAuthorize("hasAnyRole('DUENO', 'ENCARGADO')")
    public void deleteCategory(@PathVariable("id") Long id, Authentication auth) {

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String fullName = userDetails.getFullName();

        MDC.put("action", "DELETE_CATEGORY");
        MDC.put("categoryId", String.valueOf(id));

        log.info("Usuario: {} está eliminando la categoría con ID: {}", fullName, id);

        try {
            // Ejecutar caso de uso
            deleteCategoryUseCase.delete(id);

            log.info("Usuario: {} ha eliminado la categoría con ID: {}", fullName, id);

        } catch (Exception e) {
            log.error("Error al eliminar categoría con ID: {} por parte de: {}. Causa: {}",
                    id, fullName, e.getMessage());
            throw e;
        } finally {
            MDC.clear();
        }
    }

    // --- Endpoint para activar/desactivar categoría ---
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/toggleStatus/{id}")
    @Operation(
            summary = "Activar o desactivar categoría",
            description = "Cambia el estado de actividad de una categoría. Requiere autenticación de DUEÑO o ENCARGADO."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Estado de categoría actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = CategoryResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Categoría no encontrada",
            content = @Content
    )
    @PreAuthorize("hasAnyRole('DUENO', 'ENCARGADO')")
    public CategoryResponse toggleCategoryStatus(@PathVariable("id") Long id, Authentication auth) {

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String fullName = userDetails.getFullName();

        MDC.put("action", "TOGGLE_CATEGORY_STATUS");
        MDC.put("categoryId", String.valueOf(id));

        log.info("Usuario: {} está cambiando el estado de la categoría con ID: {}", fullName, id);

        try {
            // Ejecutar caso de uso - lanza CategoryNotFoundException si no existe
            toggleCategoryStatusUseCase.toggleStatus(id);

            // Obtener la categoría actualizada para retornar el response
            Optional<Category> categoryOpt = getCategoryByIdUseCase.getById(id);
            Category toggledCategory = categoryOpt.get(); // Siempre tendrá valor o se habrá lanzado excepción

            // Mapear entidad a response usando el mapper
            CategoryResponse response = categoryMapper.toResponse(toggledCategory);

            log.info("Usuario: {} ha actualizado el estado de la categoría con ID: {} a: {}",
                    fullName, response.id(), response.active());

            return response;

        } finally {
            MDC.clear();
        }
    }
}
