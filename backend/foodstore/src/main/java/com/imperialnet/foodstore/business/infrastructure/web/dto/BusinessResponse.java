package com.imperialnet.foodstore.business.infrastructure.web.dto;

import lombok.Builder;
import lombok.Value;

/**
 * DTO de salida para exponer datos del negocio en API pública.
 * Usa @Value para inmutabilidad y @Builder para facilidad de construcción.
 */
@Value
@Builder
public class BusinessResponse {
    Long id;
    String name;
    String description;
    String email;
    String phone;
    String address;
    boolean active;
}
