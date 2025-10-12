package com.imperialnet.foodstore.business.application.port.out;

import com.imperialnet.foodstore.business.domain.model.BusinessHour;

import java.util.List;

public interface BusinessHourRepositoryPort {

    List<BusinessHour> findAll();
    void saveAll(List<BusinessHour> hours);
}
