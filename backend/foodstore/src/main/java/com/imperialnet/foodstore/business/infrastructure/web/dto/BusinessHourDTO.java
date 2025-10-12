package com.imperialnet.foodstore.business.infrastructure.web.dto;

import java.util.List;
import lombok.Data;

@Data
public class BusinessHourDTO {
    private Long id;
    private String dayOfWeek;
    private boolean enabled;
    private List<BusinessHourRangeDTO> ranges;
}
