package com.imperialnet.foodstore.orders.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Entidad JPA que representa un ítem de pedido en la base de datos.
 * Corresponde al modelo de dominio OrderItem.
 */
@Entity
@Table(name = "order_items")
@Getter
@Setter
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "unit_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @PrePersist
    @PreUpdate
    protected void validate() {
        if (productId == null)
            throw new IllegalStateException("El producto debe tener un ID válido.");
        if (productName == null || productName.isBlank())
            throw new IllegalStateException("El nombre del producto es obligatorio.");
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalStateException("El precio no puede ser nulo o negativo.");
        if (quantity == null || quantity <= 0)
            throw new IllegalStateException("La cantidad debe ser mayor que cero.");
    }

    /**
     * Calcula el subtotal del ítem (precio * cantidad).
     */
    public BigDecimal getSubtotal() {
        return (unitPrice == null || quantity == null)
                ? BigDecimal.ZERO
                : unitPrice.multiply(BigDecimal.valueOf(quantity));
    }


}
