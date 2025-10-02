package com.imperialnet.foodstore.business.application.port.out;

import com.imperialnet.foodstore.business.domain.model.Business;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para la persistencia de negocios.
 * Define las operaciones de acceso a datos que necesita el dominio.
 */
public interface BusinessRepositoryPort {

    /**
     * Guarda un nuevo negocio en el repositorio.
     *
     * @param business el negocio a guardar (sin ID)
     * @return el negocio guardado con su ID asignado
     * @throws IllegalArgumentException si el negocio es nulo o inválido
     */
    Business save(Business business);

    /**
     * Actualiza un negocio existente en el repositorio.
     *
     * @param business el negocio a actualizar (con ID)
     * @return el negocio actualizado
     * @throws IllegalArgumentException si el negocio es nulo o inválido
     */
    Business update(Business business);

    /**
     * Busca un negocio por su identificador único.
     *
     * @param businessId el ID del negocio a buscar
     * @return un Optional con el negocio si existe, vacío en caso contrario
     * @throws IllegalArgumentException si el ID es nulo
     */
    Optional<Business> findById(Long businessId);

    /**
     * Obtiene todos los negocios del repositorio.
     *
     * @return lista de todos los negocios
     */
    List<Business> findAll();

    /**
     * Obtiene todos los negocios activos.
     *
     * @return lista de negocios activos únicamente
     */
    List<Business> findAllActive();

    /**
     * Busca negocios por nombre (búsqueda parcial, case-insensitive).
     *
     * @param nameContains texto que debe contener el nombre
     * @return lista de negocios cuyo nombre contiene el texto especificado
     * @throws IllegalArgumentException si nameContains es nulo o vacío
     */
    List<Business> findByNameContaining(String nameContains);

    /**
     * Busca un negocio por su nombre exacto (case-insensitive).
     *
     * @param name el nombre exacto del negocio
     * @return un Optional con el negocio si existe, vacío en caso contrario
     * @throws IllegalArgumentException si el nombre es nulo o vacío
     */
    Optional<Business> findByName(String name);

    /**
     * Busca un negocio por su email exacto (case-insensitive).
     *
     * @param email el email del negocio
     * @return un Optional con el negocio si existe, vacío en caso contrario
     * @throws IllegalArgumentException si el email es nulo o vacío
     */
    Optional<Business> findByEmail(String email);

    /**
     * Busca negocios por email que contenga el texto especificado.
     *
     * @param emailContains texto que debe contener el email
     * @return lista de negocios cuyo email contiene el texto especificado
     * @throws IllegalArgumentException si emailContains es nulo o vacío
     */
    List<Business> findByEmailContaining(String emailContains);

    /**
     * Verifica si existe un negocio con el nombre especificado.
     *
     * @param name el nombre a verificar
     * @return true si existe un negocio con ese nombre, false en caso contrario
     * @throws IllegalArgumentException si el nombre es nulo o vacío
     */
    boolean existsByName(String name);

    /**
     * Verifica si existe un negocio con el email especificado.
     *
     * @param email el email a verificar
     * @return true si existe un negocio con ese email, false en caso contrario
     * @throws IllegalArgumentException si el email es nulo o vacío
     */
    boolean existsByEmail(String email);

    /**
     * Verifica si existe un negocio con el nombre especificado, excluyendo un ID específico.
     * Útil para validaciones durante actualizaciones.
     *
     * @param name el nombre a verificar
     * @param excludeId el ID del negocio a excluir de la búsqueda
     * @return true si existe otro negocio con ese nombre, false en caso contrario
     * @throws IllegalArgumentException si el nombre es nulo o vacío
     */
    boolean existsByNameAndIdNot(String name, Long excludeId);

    /**
     * Verifica si existe un negocio con el email especificado, excluyendo un ID específico.
     * Útil para validaciones durante actualizaciones.
     *
     * @param email el email a verificar
     * @param excludeId el ID del negocio a excluir de la búsqueda
     * @return true si existe otro negocio con ese email, false en caso contrario
     * @throws IllegalArgumentException si el email es nulo o vacío
     */
    boolean existsByEmailAndIdNot(String email, Long excludeId);

    /**
     * Elimina un negocio del repositorio por su ID.
     *
     * @param businessId el ID del negocio a eliminar
     * @return true si se eliminó exitosamente, false si no existía
     * @throws IllegalArgumentException si el ID es nulo
     */
    boolean deleteById(Long businessId);

    /**
     * Elimina un negocio del repositorio.
     *
     * @param business el negocio a eliminar
     * @throws IllegalArgumentException si el negocio es nulo o no tiene ID
     */
    void delete(Business business);

    /**
     * Cuenta el total de negocios en el repositorio.
     *
     * @return número total de negocios
     */
    long count();

    /**
     * Cuenta el total de negocios activos en el repositorio.
     *
     * @return número total de negocios activos
     */
    long countActive();

    /**
     * Busca negocios con criterios múltiples.
     *
     * @param criteria criterios de búsqueda
     * @return lista de negocios que cumplen los criterios
     */
    List<Business> findByCriteria(SearchCriteria criteria);

    /**
     * Criterios de búsqueda para consultas complejas.
     */
    record SearchCriteria(
            String nameContains,
            String emailContains,
            Boolean active,
            String addressContains,
            String phoneContains
    ) {}
}
