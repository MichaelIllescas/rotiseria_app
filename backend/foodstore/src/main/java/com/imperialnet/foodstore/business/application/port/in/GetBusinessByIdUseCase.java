package com.imperialnet.foodstore.business.application.port.in;

import com.imperialnet.foodstore.business.domain.model.Business;

/**
 * Caso de uso para obtener un negocio por su ID.
 */
public interface GetBusinessByIdUseCase {

    /**
     * Obtiene un negocio por su identificador único.
     *
     * @param businessId el ID del negocio a buscar
     * @return el negocio encontrado
     * @throws IllegalArgumentException si el ID es nulo
     * @throws com.imperialnet.foodstore.business.domain.exception.BusinessNotFoundException si el negocio no existe
     */
    Business getBusinessById(Long businessId);
}
