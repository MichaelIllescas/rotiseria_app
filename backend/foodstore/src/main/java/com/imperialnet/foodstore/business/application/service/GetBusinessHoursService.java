package com.imperialnet.foodstore.business.application.service;

import java.util.List;

import com.imperialnet.foodstore.business.application.port.in.GetBusinessHoursUseCase;
import com.imperialnet.foodstore.business.application.port.out.BusinessHourRepositoryPort;
import com.imperialnet.foodstore.business.domain.model.BusinessHour;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
public class GetBusinessHoursService implements GetBusinessHoursUseCase {

    private final BusinessHourRepositoryPort repository;

    public GetBusinessHoursService(BusinessHourRepositoryPort repository) {
        this.repository = repository;
    }

    // 1. Se cachea la lectura
    @Cacheable("businessHours")
    @Override
    public List<BusinessHour> getAll() {
        return repository.findAll();
    }
}
