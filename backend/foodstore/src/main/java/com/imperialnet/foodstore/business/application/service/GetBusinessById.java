package com.imperialnet.foodstore.business.application.service;

import com.imperialnet.foodstore.business.application.port.in.GetBusinessByIdUseCase;
import com.imperialnet.foodstore.business.application.port.out.BusinessRepositoryPort;
import com.imperialnet.foodstore.business.domain.exception.BusinessNotFoundException;
import com.imperialnet.foodstore.business.domain.model.Business;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class GetBusinessById implements GetBusinessByIdUseCase {

    private final BusinessRepositoryPort businessRepository;

    public GetBusinessById(BusinessRepositoryPort businessRepository) {
        this.businessRepository = businessRepository;
    }

    @Override
    public Business getBusinessById(Long businessId) {
        MDC.put("action", "GET_BUSINESS_BY_ID");
        MDC.put("businessId", String.valueOf(businessId));

        try {
            if (businessId == null) {
                log.warn("ID de negocio nulo proporcionado para búsqueda");
                throw new IllegalArgumentException("El ID del negocio no puede ser nulo");
            }

            log.info("Buscando negocio con ID: {}", businessId);
            Optional<Business> business = businessRepository.findById(businessId);

            if (business.isEmpty()) {
                log.warn("Negocio no encontrado");
                throw new BusinessNotFoundException(businessId);
            }

            log.info("Negocio encontrado exitosamente: {}", business.get().getName());
            return business.get();

        } finally {
            MDC.clear();
        }
    }
}
