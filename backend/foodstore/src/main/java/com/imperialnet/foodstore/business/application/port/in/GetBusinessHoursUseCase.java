package com.imperialnet.foodstore.business.application.port.in;

import com.imperialnet.foodstore.business.domain.model.BusinessHour;

import java.util.List;

public interface GetBusinessHoursUseCase {
    List<BusinessHour> getAll();
}
