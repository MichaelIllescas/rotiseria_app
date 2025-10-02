package com.imperialnet.foodstore.business.infrastructure.mapper;


import com.imperialnet.foodstore.business.domain.model.Business;
import com.imperialnet.foodstore.business.infrastructure.persistence.entity.BusinessEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para convertir entre el modelo de dominio y la entidad JPA.
 */
@Mapper(componentModel = "spring")
public interface BusinessEntityMapper {

    @Mapping(target = "id", source = "id")
    BusinessEntity toEntity(Business business);

    @Mapping(target = "id", source = "id")
    Business toDomain(BusinessEntity entity);
}
