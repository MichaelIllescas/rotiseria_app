package com.imperialnet.foodstore.business.infrastructure.web.in;

import com.imperialnet.foodstore.business.application.port.in.CreateBusinessUseCase;
import com.imperialnet.foodstore.business.application.port.in.GetAllBusinessUseCase;
import com.imperialnet.foodstore.business.application.port.in.GetBusinessByIdUseCase;
import com.imperialnet.foodstore.business.application.port.in.UpdateBusinessUseCase;
import com.imperialnet.foodstore.business.application.port.in.DeleteBusinessUseCase;
import com.imperialnet.foodstore.business.application.port.in.ChangeStatusBusinessUseCase;
import com.imperialnet.foodstore.business.domain.model.Business;
import com.imperialnet.foodstore.business.infrastructure.mapper.BusinessWebMapper;
import com.imperialnet.foodstore.business.infrastructure.web.dto.BusinessResponse;
import com.imperialnet.foodstore.business.infrastructure.web.dto.BusinessRequest;
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
@RequestMapping("/api/business")
@RequiredArgsConstructor
@Tag(name = "Negocios", description = "Operaciones relacionadas con la gestión de información del negocio")
public class BusinessController {

    private final CreateBusinessUseCase createBusinessUseCase;
    private final GetAllBusinessUseCase getAllBusinessUseCase;
    private final GetBusinessByIdUseCase getBusinessByIdUseCase;
    private final UpdateBusinessUseCase updateBusinessUseCase;
    private final DeleteBusinessUseCase deleteBusinessUseCase;
    private final ChangeStatusBusinessUseCase changeStatusBusinessUseCase;
    private final BusinessWebMapper businessWebMapper;

    // --- Endpoint para crear negocio ---
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    @Operation(
            summary = "Crear un nuevo negocio",
            description = "Crea un nuevo negocio en el sistema. Requiere autenticación de DUEÑO o ENCARGADO."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Negocio creado exitosamente",
            content = @Content(schema = @Schema(implementation = BusinessResponse.class))
    )
    @PreAuthorize("hasAnyRole('DUENO', 'ENCARGADO')")
    public BusinessResponse createBusiness(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para crear un negocio",
                    required = true,
                    content = @Content(schema = @Schema(implementation = BusinessRequest.class))
            ) @Valid @RequestBody BusinessRequest request,
            Authentication auth) {

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String fullName = userDetails.getFullName();

        MDC.put("action", "CREATE_BUSINESS");
        MDC.put("businessName", request.getName());

        log.info("Usuario: {} está creando un nuevo negocio con nombre: {}", fullName, request.getName());

        try {
            // Crear comando para el caso de uso
            CreateBusinessUseCase.CreateBusinessCommand command = new CreateBusinessUseCase.CreateBusinessCommand(
                    request.getName(),
                    request.getDescription(),
                    request.getEmail(),
                    request.getPhone(),
                    request.getAddress(),
                    request.isActive()
            );

            // Ejecutar caso de uso
            Business createdBusiness = createBusinessUseCase.createBusiness(command);

            // Mapear entidad a response usando el mapper
            BusinessResponse response = businessWebMapper.toResponse(createdBusiness);

            log.info("Usuario: {} ha creado un nuevo negocio con ID: {} y nombre: {}",
                    fullName, response.getId(), response.getName());

            return response;

        } catch (Exception e) {
            log.error("Error al crear negocio con nombre: {} por parte de: {}. Causa: {}",
                    request.getName(), fullName, e.getMessage());
            throw e;
        } finally {
            MDC.clear();
        }
    }

    // --- Endpoint para obtener todos los negocios ---
    @Operation(
            summary = "Obtener todos los negocios",
            description = "Obtiene todos los negocios registrados en el sistema. Disponible para todos los roles autenticados."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de negocios obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = BusinessResponse.class))
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('DUENO', 'ENCARGADO', 'ATENCION')")
    public List<BusinessResponse> getAllBusiness() {

        MDC.put("action", "GET_ALL_BUSINESS");

        log.info("Obteniendo todos los negocios del sistema");

        try {
            // Ejecutar caso de uso
            List<Business> businesses = getAllBusinessUseCase.getAllBusiness();

            // Mapear entidades a responses usando el mapper
            List<BusinessResponse> responses = businesses.stream()
                    .map(businessWebMapper::toResponse)
                    .toList();

            log.info("Se han obtenido: {} negocios del sistema", responses.size());

            return responses;

        } catch (Exception e) {
            log.error("Error al obtener todos los negocios. Causa: {}", e.getMessage(), e);
            throw e;
        } finally {
            MDC.clear();
        }
    }

    // --- Endpoint para obtener negocios activos ---
    @Operation(
            summary = "Obtener negocios activos",
            description = "Obtiene todos los negocios activos del sistema. Disponible para todos los roles autenticados."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de negocios activos obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = BusinessResponse.class))
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getActive")
    @PreAuthorize("hasAnyRole('DUENO', 'ENCARGADO', 'ATENCION')")
    public List<BusinessResponse> getAllActiveBusiness() {

        MDC.put("action", "GET_ALL_ACTIVE_BUSINESS");

        log.info("Obteniendo todos los negocios activos del sistema");

        try {
            // Ejecutar caso de uso
            List<Business> businesses = getAllBusinessUseCase.getAllActiveBusiness();

            // Mapear entidades a responses usando el mapper
            List<BusinessResponse> responses = businesses.stream()
                    .map(businessWebMapper::toResponse)
                    .toList();

            log.info("Se han obtenido: {} negocios activos del sistema", responses.size());

            return responses;

        } catch (Exception e) {
            log.error("Error al obtener todos los negocios activos. Causa: {}", e.getMessage(), e);
            throw e;
        } finally {
            MDC.clear();
        }
    }

    // --- Endpoint para obtener negocio por ID ---
    @Operation(
            summary = "Obtener negocio por ID",
            description = "Obtiene los detalles de un negocio específico mediante su ID. Disponible para todos los roles autenticados."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Negocio obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = BusinessResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Negocio no encontrado",
            content = @Content
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DUENO', 'ENCARGADO', 'ATENCION')")
    public BusinessResponse getBusinessById(@PathVariable("id") Long id) {

        MDC.put("action", "GET_BUSINESS_BY_ID");
        MDC.put("businessId", String.valueOf(id));

        log.info("Obteniendo negocio con ID: {}", id);

        try {
            // Ejecutar caso de uso
            Business business = getBusinessByIdUseCase.getBusinessById(id);

            // Mapear entidad a response usando el mapper
            BusinessResponse response = businessWebMapper.toResponse(business);

            log.info("Negocio obtenido: ID: {}, Nombre: {}", response.getId(), response.getName());

            return response;

        } finally {
            MDC.clear();
        }
    }

    // --- Endpoint para actualizar negocio ---
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/update/{id}")
    @Operation(
            summary = "Actualizar negocio",
            description = "Actualiza los datos de un negocio existente. Requiere autenticación de DUEÑO o ENCARGADO."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Negocio actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = BusinessResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Negocio no encontrado",
            content = @Content
    )
    @PreAuthorize("hasAnyRole('DUENO', 'ENCARGADO')")
    public BusinessResponse updateBusiness(
            @PathVariable("id") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados del negocio",
                    required = true,
                    content = @Content(schema = @Schema(implementation = BusinessRequest.class))
            ) @Valid @RequestBody BusinessRequest request,
            Authentication auth) {

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String fullName = userDetails.getFullName();

        MDC.put("action", "UPDATE_BUSINESS");
        MDC.put("businessId", String.valueOf(id));
        MDC.put("businessName", request.getName());

        log.info("Usuario: {} está actualizando el negocio con ID: {} y nombre: {}", fullName, id, request.getName());

        try {
            // Crear comando para el caso de uso
            UpdateBusinessUseCase.UpdateBusinessCommand command = new UpdateBusinessUseCase.UpdateBusinessCommand(
                    id,
                    request.getName(),
                    request.getDescription(),
                    request.getEmail(),
                    request.getPhone(),
                    request.getAddress(),
                    request.isActive()
            );

            // Ejecutar caso de uso
            Business updatedBusiness = updateBusinessUseCase.updateBusiness(command);

            // Mapear entidad a response usando el mapper
            BusinessResponse response = businessWebMapper.toResponse(updatedBusiness);

            log.info("Usuario: {} ha actualizado el negocio con ID: {} y nuevo nombre: {}",
                    fullName, response.getId(), response.getName());

            return response;

        } catch (Exception e) {
            log.error("Error al actualizar negocio con ID: {} por parte de: {}. Causa: {}",
                    id, fullName, e.getMessage());
            throw e;
        } finally {
            MDC.clear();
        }
    }

    // --- Endpoint para eliminar negocio ---
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Eliminar negocio",
            description = "Elimina un negocio existente del sistema. Requiere autenticación de DUEÑO o ENCARGADO."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Negocio eliminado exitosamente",
            content = @Content
    )
    @ApiResponse(
            responseCode = "404",
            description = "Negocio no encontrado",
            content = @Content
    )
    @PreAuthorize("hasAnyRole('DUENO', 'ENCARGADO')")
    public void deleteBusiness(@PathVariable("id") Long id, Authentication auth) {

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String fullName = userDetails.getFullName();

        MDC.put("action", "DELETE_BUSINESS");
        MDC.put("businessId", String.valueOf(id));

        log.info("Usuario: {} está eliminando el negocio con ID: {}", fullName, id);

        try {
            // Ejecutar caso de uso
            deleteBusinessUseCase.deleteBusiness(id);

            log.info("Usuario: {} ha eliminado el negocio con ID: {}", fullName, id);

        } catch (Exception e) {
            log.error("Error al eliminar negocio con ID: {} por parte de: {}. Causa: {}",
                    id, fullName, e.getMessage());
            throw e;
        } finally {
            MDC.clear();
        }
    }

    // --- Endpoint para cambiar estado del negocio ---
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/changeStatus/{id}")
    @Operation(
            summary = "Cambiar estado del negocio",
            description = "Cambia el estado de actividad de un negocio. Requiere autenticación de DUEÑO o ENCARGADO."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Estado del negocio actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = BusinessResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Negocio no encontrado",
            content = @Content
    )
    @PreAuthorize("hasAnyRole('DUENO', 'ENCARGADO')")
    public BusinessResponse changeBusinessStatus(@PathVariable("id") Long id, Authentication auth) {

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String fullName = userDetails.getFullName();

        MDC.put("action", "CHANGE_BUSINESS_STATUS");
        MDC.put("businessId", String.valueOf(id));

        log.info("Usuario: {} está cambiando el estado del negocio con ID: {}", fullName, id);

        try {
            // Ejecutar caso de uso
            Business updatedBusiness = changeStatusBusinessUseCase.changeBusinessStatus(id);

            // Mapear entidad a response usando el mapper
            BusinessResponse response = businessWebMapper.toResponse(updatedBusiness);

            log.info("Usuario: {} ha actualizado el estado del negocio con ID: {} a: {}",
                    fullName, response.getId(), response.isActive());

            return response;

        } finally {
            MDC.clear();
        }
    }
}
