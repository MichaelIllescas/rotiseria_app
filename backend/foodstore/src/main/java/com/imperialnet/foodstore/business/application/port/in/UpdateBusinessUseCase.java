package com.imperialnet.foodstore.business.application.port.in;

import com.imperialnet.foodstore.business.domain.model.Business;

/**
 * Caso de uso para actualizar un negocio existente.
 */
public interface UpdateBusinessUseCase {

    /**
     * Actualiza un negocio existente en el sistema.
     *
     * @param command el comando con los datos del negocio a actualizar
     * @return el negocio actualizado
     * @throws IllegalArgumentException si los datos del comando son inválidos
     * @throws com.imperialnet.foodstore.business.domain.exception.BusinessNotFoundException si el negocio no existe
     */
    Business updateBusiness(UpdateBusinessCommand command);

    /**
     * Comando que encapsula los datos necesarios para actualizar un negocio.
     */
    record UpdateBusinessCommand(
            Long id,
            String name,
            String description,
            String email,
            String phone,
            String address,
            boolean active
    ) {}
}
