package com.imperialnet.foodstore.business.infrastructure.persistence.repository;

import com.imperialnet.foodstore.business.infrastructure.persistence.entity.BusinessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad BusinessEntity.
 * Proporciona operaciones de acceso a datos usando Spring Data JPA.
 */
@Repository
public interface BusinessJpaRepository extends JpaRepository<BusinessEntity, Long> {

    /**
     * Busca todos los negocios activos.
     */
    List<BusinessEntity> findByActiveTrue();

    /**
     * Busca negocios por nombre que contenga el texto especificado (case-insensitive).
     */
    List<BusinessEntity> findByNameContainingIgnoreCase(String nameContains);

    /**
     * Busca un negocio por nombre exacto (case-insensitive).
     */
    Optional<BusinessEntity> findByNameIgnoreCase(String name);

    /**
     * Busca un negocio por email exacto (case-insensitive).
     */
    Optional<BusinessEntity> findByEmailIgnoreCase(String email);

    /**
     * Busca negocios por email que contenga el texto especificado (case-insensitive).
     */
    List<BusinessEntity> findByEmailContainingIgnoreCase(String emailContains);

    /**
     * Verifica si existe un negocio con el nombre especificado (case-insensitive).
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Verifica si existe un negocio con el email especificado (case-insensitive).
     */
    boolean existsByEmailIgnoreCase(String email);

    /**
     * Verifica si existe un negocio con el nombre especificado, excluyendo un ID (case-insensitive).
     */
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long excludeId);

    /**
     * Verifica si existe un negocio con el email especificado, excluyendo un ID (case-insensitive).
     */
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long excludeId);

    /**
     * Cuenta el total de negocios activos.
     */
    long countByActiveTrue();

    /**
     * Busca negocios por criterios múltiples usando consulta personalizada.
     */
    @Query("SELECT b FROM BusinessEntity b WHERE " +
           "(:nameContains IS NULL OR LOWER(b.name) LIKE LOWER(CONCAT('%', :nameContains, '%'))) AND " +
           "(:emailContains IS NULL OR LOWER(b.email) LIKE LOWER(CONCAT('%', :emailContains, '%'))) AND " +
           "(:active IS NULL OR b.active = :active) AND " +
           "(:addressContains IS NULL OR LOWER(b.address) LIKE LOWER(CONCAT('%', :addressContains, '%'))) AND " +
           "(:phoneContains IS NULL OR b.phone LIKE CONCAT('%', :phoneContains, '%'))")
    List<BusinessEntity> findByCriteria(
            @Param("nameContains") String nameContains,
            @Param("emailContains") String emailContains,
            @Param("active") Boolean active,
            @Param("addressContains") String addressContains,
            @Param("phoneContains") String phoneContains
    );
}
