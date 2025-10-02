package com.imperialnet.foodstore.business.application.port.in;

import com.imperialnet.foodstore.business.domain.model.Business;
import com.imperialnet.foodstore.business.domain.exception.BusinessNotFoundException;

/**
 * Caso de uso para alternar el estado de activación de un negocio.
 */
public interface ChangeStatusBusinessUseCase {

    /**
     * Cambia el estado de activación de un negocio (toggle).
     * Si el negocio está activo, se desactiva; si está inactivo, se activa.
     *
     * @param businessId el ID del negocio
     * @return el negocio con el estado actualizado
     * @throws IllegalArgumentException si el ID es nulo o inválido
     * @throws BusinessNotFoundException si el negocio no existe
     */
    Business changeBusinessStatus(Long businessId);
}
