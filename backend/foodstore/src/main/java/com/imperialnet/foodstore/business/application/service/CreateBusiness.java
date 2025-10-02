package com.imperialnet.foodstore.business.application.service;

import com.imperialnet.foodstore.business.application.port.in.CreateBusinessUseCase;
import com.imperialnet.foodstore.business.application.port.out.BusinessRepositoryPort;
import com.imperialnet.foodstore.business.domain.model.Business;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CreateBusiness implements CreateBusinessUseCase {

    private final BusinessRepositoryPort businessRepository;

    public CreateBusiness(BusinessRepositoryPort businessRepository) {
        this.businessRepository = businessRepository;
    }

    @Override
    public Business createBusiness(CreateBusinessCommand command) {
        MDC.put("action", "CREATE_BUSINESS");
        MDC.put("businessName", command.name());

        try {
            Optional<Business> existing = businessRepository.findByName(command.name());
            if (existing.isPresent()) {
                log.warn("El negocio ya existe");
                throw new IllegalArgumentException(
                        "Ya existe un negocio con el nombre: " + command.name()
                );
            }

            // Crear la entidad Business a partir del comando
            Business business = new Business(
                    null, // ID será asignado por el repositorio
                    command.name(),
                    command.description(),
                    command.email(),
                    command.phone(),
                    command.address(),
                    command.active()
            );

            log.info("Creando negocio: active={}", business.isActive());
            Business saved = businessRepository.save(business);
            log.info("Negocio creado correctamente: id={}", saved.getId());
            return saved;

        } finally {
            MDC.clear();
        }
    }
}
