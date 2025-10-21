package com.imperialnet.foodstore.orders.infrastructure.persistence.entity;

import com.imperialnet.foodstore.orders.domain.model.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA que representa un pedido (Order) en la base de datos.
 * Corresponde al agregado raíz del dominio.
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;

    @Column(nullable = false, length = 30)
    private String phone;

    @Column(length = 120)
    private String email;

    @Column(length = 255)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_type", nullable = false, length = 20)
    private DeliveryType deliveryType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 20)
    private PaymentStatus paymentStatus;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItemEntity> items = new ArrayList<>();

    // ============================
    // 🔹 Eventos del ciclo de vida
    // ============================

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    // ============================
    // 🔹 Lógica auxiliar
    // ============================

    public BigDecimal calculateTotal() {
        return items == null || items.isEmpty()
                ? BigDecimal.ZERO
                : items.stream()
                .map(OrderItemEntity::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void updateTotal() {
        this.total = calculateTotal();
    }

    public void addItem(OrderItemEntity item) {
        items.add(item);
        item.setOrder(this);
        updateTotal();
    }

    public void removeItem(OrderItemEntity item) {
        items.remove(item);
        item.setOrder(null);
        updateTotal();
    }

    /**
     * Setter seguro utilizado por el mapper para establecer ítems
     * manteniendo la consistencia bidireccional.
     */
    public void setItems(List<OrderItemEntity> items) {
        this.items.clear();
        if (items != null) {
            items.forEach(this::addItem);
        }
    }
}
