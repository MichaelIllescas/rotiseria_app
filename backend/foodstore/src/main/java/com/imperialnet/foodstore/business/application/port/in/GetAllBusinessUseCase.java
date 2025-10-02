package com.imperialnet.foodstore.business.application.port.in;

import com.imperialnet.foodstore.business.domain.model.Business;

import java.util.List;

/**
 * Caso de uso para obtener todos los negocios del sistema.
 */
public interface GetAllBusinessUseCase {

    /**
     * Obtiene todos los negocios registrados en el sistema.
     *
     * @return lista de todos los negocios (activos e inactivos)
     */
    List<Business> getAllBusiness();

    /**
     * Obtiene solo los negocios activos del sistema.
     *
     * @return lista de negocios activos únicamente
     */
    List<Business> getAllActiveBusiness();
}
