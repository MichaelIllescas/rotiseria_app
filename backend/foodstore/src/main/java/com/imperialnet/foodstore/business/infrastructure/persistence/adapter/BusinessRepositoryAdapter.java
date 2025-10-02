package com.imperialnet.foodstore.business.infrastructure.persistence.adapter;

import com.imperialnet.foodstore.business.application.port.out.BusinessRepositoryPort;
import com.imperialnet.foodstore.business.domain.model.Business;
import com.imperialnet.foodstore.business.infrastructure.mapper.BusinessMapper;
import com.imperialnet.foodstore.business.infrastructure.persistence.entity.BusinessEntity;
import com.imperialnet.foodstore.business.infrastructure.persistence.repository.BusinessJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de persistencia que implementa BusinessRepositoryPort.
 * Actúa como bridge entre la capa de dominio y la infraestructura de persistencia JPA.
 */
@Component
@RequiredArgsConstructor
@Transactional
public class BusinessRepositoryAdapter implements BusinessRepositoryPort {

    private final BusinessJpaRepository jpaRepository;
    private final BusinessMapper mapper;

    @Override
    public Business save(Business business) {
        validateBusinessForSave(business);

        BusinessEntity entity = mapper.toEntity(business);
        entity.setId(null); // Asegurar que es una nueva entidad
        BusinessEntity savedEntity = jpaRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public Business update(Business business) {
        validateBusinessForUpdate(business);

        BusinessEntity existingEntity = jpaRepository.findById(business.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se puede actualizar: negocio con ID " + business.getId() + " no existe"));

        mapper.updateEntity(existingEntity, business);
        BusinessEntity updatedEntity = jpaRepository.save(existingEntity);

        return mapper.toDomain(updatedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Business> findById(Long businessId) {
        validateBusinessId(businessId);

        return jpaRepository.findById(businessId)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Business> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Business> findAllActive() {
        return jpaRepository.findByActiveTrue()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Business> findByNameContaining(String nameContains) {
        validateNameContains(nameContains);

        return jpaRepository.findByNameContainingIgnoreCase(nameContains)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Business> findByName(String name) {
        validateName(name);

        return jpaRepository.findByNameIgnoreCase(name)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Business> findByEmail(String email) {
        validateEmail(email);

        return jpaRepository.findByEmailIgnoreCase(email)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Business> findByEmailContaining(String emailContains) {
        validateEmailContains(emailContains);

        return jpaRepository.findByEmailContainingIgnoreCase(emailContains)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        validateName(name);

        return jpaRepository.existsByNameIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        validateEmail(email);

        return jpaRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNameAndIdNot(String name, Long excludeId) {
        validateName(name);
        validateBusinessId(excludeId);

        return jpaRepository.existsByNameIgnoreCaseAndIdNot(name, excludeId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmailAndIdNot(String email, Long excludeId) {
        validateEmail(email);
        validateBusinessId(excludeId);

        return jpaRepository.existsByEmailIgnoreCaseAndIdNot(email, excludeId);
    }

    @Override
    public boolean deleteById(Long businessId) {
        validateBusinessId(businessId);

        if (jpaRepository.existsById(businessId)) {
            jpaRepository.deleteById(businessId);
            return true;
        }
        return false;
    }

    @Override
    public void delete(Business business) {
        validateBusinessForDelete(business);

        BusinessEntity entity = mapper.toEntity(business);
        jpaRepository.delete(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return jpaRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countActive() {
        return jpaRepository.countByActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Business> findByCriteria(SearchCriteria criteria) {
        if (criteria == null) {
            throw new IllegalArgumentException("Los criterios de búsqueda no pueden ser nulos");
        }

        return jpaRepository.findByCriteria(
                        criteria.nameContains(),
                        criteria.emailContains(),
                        criteria.active(),
                        criteria.addressContains(),
                        criteria.phoneContains()
                ).stream()
                .map(mapper::toDomain)
                .toList();
    }

    // ===================== Métodos de validación privados =====================

    private void validateBusinessForSave(Business business) {
        if (business == null) {
            throw new IllegalArgumentException("El negocio no puede ser nulo");
        }
        if (business.getId() != null) {
            throw new IllegalArgumentException("El negocio para guardar no debe tener ID asignado");
        }
    }

    private void validateBusinessForUpdate(Business business) {
        if (business == null) {
            throw new IllegalArgumentException("El negocio no puede ser nulo");
        }
        if (business.getId() == null) {
            throw new IllegalArgumentException("El negocio para actualizar debe tener ID asignado");
        }
    }

    private void validateBusinessForDelete(Business business) {
        if (business == null) {
            throw new IllegalArgumentException("El negocio no puede ser nulo");
        }
        if (business.getId() == null) {
            throw new IllegalArgumentException("El negocio para eliminar debe tener ID asignado");
        }
    }

    private void validateBusinessId(Long businessId) {
        if (businessId == null) {
            throw new IllegalArgumentException("El ID del negocio no puede ser nulo");
        }
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
        }
    }

    private void validateNameContains(String nameContains) {
        if (nameContains == null || nameContains.trim().isEmpty()) {
            throw new IllegalArgumentException("El texto de búsqueda del nombre no puede ser nulo o vacío");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede ser nulo o vacío");
        }
    }

    private void validateEmailContains(String emailContains) {
        if (emailContains == null || emailContains.trim().isEmpty()) {
            throw new IllegalArgumentException("El texto de búsqueda del email no puede ser nulo o vacío");
        }
    }
}
