package com.imperialnet.foodstore.business.application.service;

import com.imperialnet.foodstore.business.application.port.in.GetAllBusinessUseCase;
import com.imperialnet.foodstore.business.application.port.out.BusinessRepositoryPort;
import com.imperialnet.foodstore.business.domain.model.Business;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GetAllBusiness implements GetAllBusinessUseCase {

    private final BusinessRepositoryPort businessRepository;

    public GetAllBusiness(BusinessRepositoryPort businessRepository) {
        this.businessRepository = businessRepository;
    }

    @Override
    public List<Business> getAllBusiness() {
        MDC.put("action", "GET_ALL_BUSINESS");

        try {
            log.info("Obteniendo todos los negocios");
            List<Business> businesses = businessRepository.findAll();
            log.info("Se encontraron {} negocios en total", businesses.size());
            return businesses;

        } finally {
            MDC.clear();
        }
    }

    @Override
    public List<Business> getAllActiveBusiness() {
        MDC.put("action", "GET_ALL_ACTIVE_BUSINESS");

        try {
            log.info("Obteniendo todos los negocios activos");
            List<Business> activeBusinesses = businessRepository.findAllActive();
            log.info("Se encontraron {} negocios activos", activeBusinesses.size());
            return activeBusinesses;

        } finally {
            MDC.clear();
        }
    }
}
