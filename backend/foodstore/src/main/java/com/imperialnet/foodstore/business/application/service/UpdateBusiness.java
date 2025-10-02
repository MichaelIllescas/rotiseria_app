package com.imperialnet.foodstore.business.application.service;

import com.imperialnet.foodstore.business.application.port.in.UpdateBusinessUseCase;
import com.imperialnet.foodstore.business.application.port.out.BusinessRepositoryPort;
import com.imperialnet.foodstore.business.domain.exception.BusinessNotFoundException;
import com.imperialnet.foodstore.business.domain.model.Business;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UpdateBusiness implements UpdateBusinessUseCase {

    private final BusinessRepositoryPort businessRepository;

    public UpdateBusiness(BusinessRepositoryPort businessRepository) {
        this.businessRepository = businessRepository;
    }

    @Override
    public Business updateBusiness(UpdateBusinessCommand command) {
        MDC.put("action", "UPDATE_BUSINESS");
        MDC.put("businessId", String.valueOf(command.id()));
        MDC.put("businessName", command.name());

        try {
            if (command.id() == null) {
                log.warn("ID de negocio nulo proporcionado para actualización");
                throw new IllegalArgumentException("El ID del negocio no puede ser nulo");
            }

            log.info("Actualizando negocio con ID: {}", command.id());

            // Verificar que el negocio existe
            Optional<Business> existingBusiness = businessRepository.findById(command.id());
            if (existingBusiness.isEmpty()) {
                log.warn("Negocio no encontrado para actualización");
                throw new BusinessNotFoundException(command.id());
            }

            // Verificar duplicados por nombre (excluyendo el mismo negocio)
            Optional<Business> duplicateByName = businessRepository.findByName(command.name());
            if (duplicateByName.isPresent() && !duplicateByName.get().getId().equals(command.id())) {
                log.warn("Ya existe otro negocio con el nombre: {}", command.name());
                throw new IllegalArgumentException("Ya existe otro negocio con el nombre: " + command.name());
            }

            // Crear el negocio actualizado
            Business updatedBusiness = new Business(
                    command.id(),
                    command.name(),
                    command.description(),
                    command.email(),
                    command.phone(),
                    command.address(),
                    command.active()
            );

            log.info("Guardando negocio actualizado: active={}, email={}",
                    updatedBusiness.isActive(), updatedBusiness.getEmail());
            Business saved = businessRepository.update(updatedBusiness);
            log.info("Negocio actualizado exitosamente: id={}", saved.getId());
            return saved;

        } finally {
            MDC.clear();
        }
    }
}
