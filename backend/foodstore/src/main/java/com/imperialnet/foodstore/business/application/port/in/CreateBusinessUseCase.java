package com.imperialnet.foodstore.business.application.port.in;

import com.imperialnet.foodstore.business.domain.model.Business;

/**
 * Caso de uso para crear un nuevo negocio.
 */
public interface CreateBusinessUseCase {

    /**
     * Crea un nuevo negocio en el sistema.
     *
     * @param command el comando con los datos del negocio a crear
     * @return el negocio creado con su ID asignado
     * @throws IllegalArgumentException si los datos del comando son inválidos
     */
    Business createBusiness(CreateBusinessCommand command);

    /**
     * Comando que encapsula los datos necesarios para crear un negocio.
     */
    record CreateBusinessCommand(
            String name,
            String description,
            String email,
            String phone,
            String address,
            boolean active
    ) {}
}
