package com.imperialnet.foodstore.business.infrastructure.mapper;

import com.imperialnet.foodstore.business.domain.model.BusinessHour;
import com.imperialnet.foodstore.business.infrastructure.persistence.entity.BusinessHourEntity;
import com.imperialnet.foodstore.business.infrastructure.web.dto.BusinessHourDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface BusinessHourMapper {
    // DTO <-> Domain
    BusinessHourDTO toDto(BusinessHour domain);
    BusinessHour toDomain(BusinessHourDTO dto);

    // Domain <-> Entity
    @Mapping(target = "id", source = "id")
    @Mapping(target = "dayOfWeek", source = "dayOfWeek")
    @Mapping(target = "enabled", source = "enabled")
    @Mapping(target = "ranges", source = "ranges")
    BusinessHourEntity toEntity(BusinessHour domain);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "dayOfWeek", source = "dayOfWeek")
    @Mapping(target = "enabled", source = "enabled")
    @Mapping(target = "ranges", source = "ranges")
    BusinessHour toDomain(BusinessHourEntity entity);

    // Range mapping between Domain and Embeddable
    @Mapping(target = "openTime", source = "open")
    @Mapping(target = "closeTime", source = "close")
    BusinessHourEntity.RangeEmbeddable toEmbeddable(BusinessHour.Range range);

    @Mapping(target = "open", source = "openTime")
    @Mapping(target = "close", source = "closeTime")
    BusinessHour.Range toRange(BusinessHourEntity.RangeEmbeddable embeddable);
}
