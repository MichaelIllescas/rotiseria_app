package com.imperialnet.foodstore.orders.infrastructure.web.dto;

import com.imperialnet.foodstore.orders.domain.model.DeliveryType;
import com.imperialnet.foodstore.orders.domain.model.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * DTO para recibir datos de creación o actualización de un pedido.
 */
public record OrderRequest(

        @NotBlank(message = "El nombre del cliente es obligatorio.")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres.")
        String customerName,

        @NotBlank(message = "El teléfono es obligatorio.")
        String phone,

        @Email(message = "El formato del email no es válido.")
        @Size(max = 120, message = "El email no puede superar los 120 caracteres.")
        String email,

        @NotNull(message = "El tipo de entrega es obligatorio.")
        DeliveryType deliveryType,

        String address,

        @NotNull(message = "El método de pago es obligatorio.")
        PaymentMethod paymentMethod,

        @Valid
        @NotNull(message = "Debe incluir al menos un ítem en el pedido.")
        List<OrderItemRequest> items
) {}
