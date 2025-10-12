package com.imperialnet.foodstore.business.infrastructure.persistence.adapter;


import java.util.List;
import java.util.stream.Collectors;

import com.imperialnet.foodstore.business.application.port.out.BusinessHourRepositoryPort;
import com.imperialnet.foodstore.business.domain.model.BusinessHour;
import com.imperialnet.foodstore.business.infrastructure.mapper.BusinessHourMapper;
import com.imperialnet.foodstore.business.infrastructure.persistence.repository.BusinessHourJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class BusinessHourRepositoryAdapter implements BusinessHourRepositoryPort {

    private final BusinessHourJpaRepository jpaRepository;
    private final BusinessHourMapper mapper;

    public BusinessHourRepositoryAdapter(BusinessHourJpaRepository jpaRepository, BusinessHourMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<BusinessHour> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void saveAll(List<BusinessHour> hours) {
        jpaRepository.deleteAll(); // reemplazo completo
        jpaRepository.saveAll(
                hours.stream().map(mapper::toEntity).toList()
        );
    }
}
