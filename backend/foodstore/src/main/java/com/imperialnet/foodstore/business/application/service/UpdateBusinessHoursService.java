package com.imperialnet.foodstore.business.application.service;

import java.util.List;

import com.imperialnet.foodstore.business.application.port.in.UpdateBusinessHoursUseCase;
import com.imperialnet.foodstore.business.application.port.out.BusinessHourRepositoryPort;
import com.imperialnet.foodstore.business.domain.model.BusinessHour;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateBusinessHoursService implements UpdateBusinessHoursUseCase {

    private final BusinessHourRepositoryPort repository;

    public UpdateBusinessHoursService(BusinessHourRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    @CachePut(value = "businessHours", key = "T(org.springframework.cache.interceptor.SimpleKey).EMPTY")
    public List<BusinessHour>  updateAll(List<BusinessHour> hours) {
        return repository.saveAll(hours);
    }
}
