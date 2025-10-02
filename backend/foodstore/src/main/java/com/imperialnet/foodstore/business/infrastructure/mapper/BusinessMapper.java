package com.imperialnet.foodstore.business.infrastructure.mapper;

import com.imperialnet.foodstore.business.domain.model.Business;
import com.imperialnet.foodstore.business.infrastructure.persistence.entity.BusinessEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre BusinessEntity (persistencia) y Business (dominio).
 * Encapsula la lógica de transformación entre las capas de infraestructura y dominio.
 */
@Component
public class BusinessMapper {

    /**
     * Convierte una entidad JPA a modelo de dominio.
     *
     * @param entity la entidad JPA a convertir
     * @return el modelo de dominio correspondiente
     * @throws IllegalArgumentException si la entidad es nula
     */
    public Business toDomain(BusinessEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("La entidad no puede ser nula");
        }

        return new Business(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getAddress(),
                entity.isActive()
        );
    }

    /**
     * Convierte un modelo de dominio a entidad JPA para persistencia.
     *
     * @param business el modelo de dominio a convertir
     * @return la entidad JPA correspondiente
     * @throws IllegalArgumentException si el modelo de dominio es nulo
     */
    public BusinessEntity toEntity(Business business) {
        if (business == null) {
            throw new IllegalArgumentException("El modelo de dominio no puede ser nulo");
        }

        BusinessEntity entity = new BusinessEntity();
        entity.setId(business.getId());
        entity.setName(business.getName());
        entity.setDescription(business.getDescription());
        entity.setEmail(business.getEmail());
        entity.setPhone(business.getPhone());
        entity.setAddress(business.getAddress());
        entity.setActive(business.isActive());

        return entity;
    }

    /**
     * Actualiza una entidad existente con los datos del modelo de dominio.
     * Útil para operaciones de actualización donde queremos preservar el contexto de persistencia.
     *
     * @param entity la entidad existente a actualizar
     * @param business el modelo de dominio con los nuevos datos
     * @throws IllegalArgumentException si algún parámetro es nulo
     */
    public void updateEntity(BusinessEntity entity, Business business) {
        if (entity == null) {
            throw new IllegalArgumentException("La entidad no puede ser nula");
        }
        if (business == null) {
            throw new IllegalArgumentException("El modelo de dominio no puede ser nulo");
        }

        entity.setName(business.getName());
        entity.setDescription(business.getDescription());
        entity.setEmail(business.getEmail());
        entity.setPhone(business.getPhone());
        entity.setAddress(business.getAddress());
        entity.setActive(business.isActive());
        // Nota: No actualizamos el ID porque debe ser inmutable
    }
}
