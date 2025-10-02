package com.imperialnet.foodstore.business.application.port.in;

/**
 * Caso de uso para eliminar un negocio del sistema.
 */
public interface DeleteBusinessUseCase {

    /**
     * Elimina un negocio del sistema por su ID.
     *
     * @param businessId el ID del negocio a eliminar
     * @throws IllegalArgumentException si el ID es nulo
     * @throws com.imperialnet.foodstore.business.domain.exception.BusinessNotFoundException si el negocio no existe
     */
    void deleteBusiness(Long businessId);
}
