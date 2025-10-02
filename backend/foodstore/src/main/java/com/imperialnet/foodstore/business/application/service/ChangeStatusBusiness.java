package com.imperialnet.foodstore.business.application.service;

import com.imperialnet.foodstore.business.application.port.in.ChangeStatusBusinessUseCase;
import com.imperialnet.foodstore.business.application.port.out.BusinessRepositoryPort;
import com.imperialnet.foodstore.business.domain.exception.BusinessNotFoundException;
import com.imperialnet.foodstore.business.domain.model.Business;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

/**
 * Implementación del caso de uso para cambiar el estado de activación de un negocio.
 *
 * Esta clase maneja la lógica de aplicación para activar o desactivar un negocio,
 * incluyendo validaciones de negocio y logging detallado.
 */
@Slf4j
@Service
public class ChangeStatusBusiness implements ChangeStatusBusinessUseCase {

    private final BusinessRepositoryPort businessRepository;

    /**
     * Constructor para inyección de dependencias.
     *
     * @param businessRepository puerto de salida para la persistencia de negocios
     */
    public ChangeStatusBusiness(BusinessRepositoryPort businessRepository) {
        this.businessRepository = businessRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Business changeBusinessStatus(Long businessId) {
        MDC.put("action", "TOGGLE_BUSINESS_STATUS");
        MDC.put("businessId", String.valueOf(businessId));

        try {
            if (businessId == null) {
                log.warn("Intento de cambiar estado con ID de negocio nulo");
                throw new IllegalArgumentException("El ID del negocio no puede ser nulo");
            }

            log.info("Iniciando cambio de estado (toggle) para negocio con ID: {}", businessId);

            // Buscar negocio existente
            Business business = businessRepository.findById(businessId)
                    .orElseThrow(() -> {
                        log.warn("Intento de cambiar estado de negocio inexistente con ID: {}", businessId);
                        return new BusinessNotFoundException(businessId);
                    });

            // Alternar estado usando lógica de dominio
            if (business.isActive()) {
                business.deactivate();
                log.debug("Desactivando negocio con ID: {}", businessId);
            } else {
                business.activate();
                log.debug("Activando negocio con ID: {}", businessId);
            }

            // Persistir cambios
            Business updatedBusiness = businessRepository.update(business);

            log.info("Estado del negocio con ID: {} actualizado correctamente a {}",
                    businessId, updatedBusiness.isActive() ? "ACTIVO" : "INACTIVO");

            return updatedBusiness;

        } catch (BusinessNotFoundException | IllegalArgumentException e) {
            throw e; // Propagamos a ControllerAdvice
        } catch (Exception e) {
            log.error("Error inesperado al cambiar estado del negocio con ID: {}. Causa: {}",
                    businessId, e.getMessage(), e);
            throw e;
        } finally {
            MDC.clear();
        }
    }

}
