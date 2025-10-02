package com.imperialnet.foodstore.business.infrastructure.mapper;


import com.imperialnet.foodstore.business.domain.model.Business;
import com.imperialnet.foodstore.business.infrastructure.web.dto.BusinessRequest;
import com.imperialnet.foodstore.business.infrastructure.web.dto.BusinessResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para convertir entre el modelo de dominio y los DTOs de la capa web.
 */
@Mapper(componentModel = "spring")
public interface BusinessWebMapper {

    // De DTO de entrada a dominio
    @Mapping(target = "id", ignore = true) // el ID se asigna en persistencia
    Business toDomain(BusinessRequest request);

    // De dominio a DTO de salida
    BusinessResponse toResponse(Business business);
}
