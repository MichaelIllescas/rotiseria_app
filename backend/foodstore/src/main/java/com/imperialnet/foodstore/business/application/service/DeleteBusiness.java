package com.imperialnet.foodstore.business.application.service;

import com.imperialnet.foodstore.business.application.port.in.DeleteBusinessUseCase;
import com.imperialnet.foodstore.business.application.port.out.BusinessRepositoryPort;
import com.imperialnet.foodstore.business.domain.exception.BusinessNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeleteBusiness implements DeleteBusinessUseCase {

    private final BusinessRepositoryPort businessRepository;

    public DeleteBusiness(BusinessRepositoryPort businessRepository) {
        this.businessRepository = businessRepository;
    }

    @Override
    public void deleteBusiness(Long businessId) {
        MDC.put("action", "DELETE_BUSINESS");
        MDC.put("businessId", String.valueOf(businessId));

        try {
            if (businessId == null) {
                log.warn("ID de negocio nulo proporcionado para eliminación");
                throw new IllegalArgumentException("El ID del negocio no puede ser nulo");
            }

            log.info("Eliminando negocio con ID: {}", businessId);
            boolean deleted = businessRepository.deleteById(businessId);

            if (!deleted) {
                log.warn("Negocio no encontrado para eliminación");
                throw new BusinessNotFoundException(businessId);
            }

            log.info("Negocio eliminado exitosamente");

        } finally {
            MDC.clear();
        }
    }
}
