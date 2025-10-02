package com.imperialnet.foodstore.business.domain.exception;

/**
 * Excepción lanzada cuando no se encuentra un negocio solicitado.
 */
public class BusinessNotFoundException extends RuntimeException {

    private final Long businessId;

    public BusinessNotFoundException(Long businessId) {
        super("Negocio con ID " + businessId + " no encontrado");
        this.businessId = businessId;
    }

    public BusinessNotFoundException(Long businessId, String message) {
        super(message);
        this.businessId = businessId;
    }

    public BusinessNotFoundException(Long businessId, String message, Throwable cause) {
        super(message, cause);
        this.businessId = businessId;
    }

    public Long getBusinessId() {
        return businessId;
    }
}
