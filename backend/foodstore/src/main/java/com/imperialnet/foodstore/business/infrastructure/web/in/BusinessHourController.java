package com.imperialnet.foodstore.business.infrastructure.web.in;

import java.util.List;

import com.imperialnet.foodstore.business.application.port.in.GetBusinessHoursUseCase;
import com.imperialnet.foodstore.business.application.port.in.UpdateBusinessHoursUseCase;
import com.imperialnet.foodstore.business.infrastructure.mapper.BusinessHourMapper;
import com.imperialnet.foodstore.business.infrastructure.web.dto.BusinessHourDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/settings/business-hours")
@Tag(name = "Horarios del Negocio", description = "Operaciones para consultar y actualizar los horarios de atención del negocio")
public class BusinessHourController {

    private final GetBusinessHoursUseCase getUseCase;
    private final UpdateBusinessHoursUseCase updateUseCase;
    private final BusinessHourMapper mapper;

    public BusinessHourController(GetBusinessHoursUseCase getUseCase,
                                  UpdateBusinessHoursUseCase updateUseCase,
                                  BusinessHourMapper mapper) {
        this.getUseCase = getUseCase;
        this.updateUseCase = updateUseCase;
        this.mapper = mapper;
    }

    @Operation(
            summary = "Obtener horarios de atención",
            description = "Devuelve la lista completa de horarios de atención configurados en el sistema."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de horarios obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = BusinessHourDTO.class))
    )
    @GetMapping
    public ResponseEntity<List<BusinessHourDTO>> getAll() {
        MDC.put("action", "GET_BUSINESS_HOURS");
        log.info("Obteniendo todos los horarios de atención");
        try {
            var hours = getUseCase.getAll().stream()
                    .map(mapper::toDto)
                    .toList();
            log.info("Se han obtenido {} horarios de atención", hours.size());
            return ResponseEntity.ok(hours);
        } finally {
            MDC.clear();
        }
    }

    @Operation(
            summary = "Actualizar horarios de atención",
            description = "Actualiza en bloque todos los horarios de atención. Reemplaza la configuración existente por la provista."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Horarios actualizados exitosamente",
            content = @Content
    )
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody List<BusinessHourDTO> dtos) {
        MDC.put("action", "UPDATE_BUSINESS_HOURS");
        MDC.put("items", String.valueOf(dtos != null ? dtos.size() : 0));
        log.info("Actualizando horarios de atención ({} elementos)", dtos != null ? dtos.size() : 0);
        try {
            var domainHours = (dtos == null ? List.<BusinessHourDTO>of() : dtos).stream()
                    .map(mapper::toDomain)
                    .toList();
            updateUseCase.updateAll(domainHours);
            log.info("Horarios de atención actualizados correctamente ({} elementos)", domainHours.size());
            return ResponseEntity.noContent().build();
        } finally {
            MDC.clear();
        }
    }
}
