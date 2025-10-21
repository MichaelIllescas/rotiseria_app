package com.imperialnet.foodstore.orders.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entidad de dominio: Order
 * Modelo validado, seguro y compatible con MapStruct.
 */
public class Order {

    private final Long id;
    private final LocalDateTime createdAt;
    private final String customerName;
    private final String phone;
    private final String email;
    private final DeliveryType deliveryType;
    private final String address;
    private final PaymentMethod paymentMethod;

    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private BigDecimal total;
    private final List<OrderItem> items = new ArrayList<>();

    // ============================
    // 🔹 Constructor público principal (usado por MapStruct)
    // ============================
    public Order(Long id,
                 String customerName,
                 String phone,
                 String email,
                 DeliveryType deliveryType,
                 String address,
                 PaymentMethod paymentMethod,
                 OrderStatus status,
                 PaymentStatus paymentStatus,
                 BigDecimal total,
                 List<OrderItem> items,
                 LocalDateTime createdAt) {

        validateBasicData(customerName, phone, email, deliveryType, address, paymentMethod);

        this.id = id;
        this.customerName = customerName.trim();
        this.phone = phone.trim();
        this.email = (email != null && !email.isBlank()) ? email.trim() : null;
        this.deliveryType = deliveryType;
        this.address = (deliveryType == DeliveryType.DELIVERY && address != null) ? address.trim() : null;
        this.paymentMethod = paymentMethod;
        this.status = status != null ? status : OrderStatus.RECEIVED;
        this.paymentStatus = paymentStatus != null ? paymentStatus : PaymentStatus.PENDING;
        this.total = total != null ? total : BigDecimal.ZERO;
        if (items != null) this.items.addAll(items);
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    // ============================
    // 🔹 Validación interna
    // ============================
    private void validateBasicData(String customerName,
                                   String phone,
                                   String email,
                                   DeliveryType deliveryType,
                                   String address,
                                   PaymentMethod paymentMethod) {
        if (customerName == null || customerName.isBlank())
            throw new IllegalArgumentException("El nombre del cliente es obligatorio.");
        if (phone == null || phone.isBlank())
            throw new IllegalArgumentException("El teléfono es obligatorio.");
        if (deliveryType == null)
            throw new IllegalArgumentException("El tipo de entrega es obligatorio.");
        if (paymentMethod == null)
            throw new IllegalArgumentException("El método de pago es obligatorio.");
        if (deliveryType == DeliveryType.DELIVERY && (address == null || address.isBlank()))
            throw new IllegalArgumentException("La dirección es obligatoria para pedidos con entrega.");
        if (email != null && !email.contains("@"))
            throw new IllegalArgumentException("El email no tiene un formato válido.");
    }

    // ============================
    // 🔹 Comportamientos de dominio
    // ============================

    /** Agrega un ítem al pedido y recalcula el total. */
    public void addItem(OrderItem item) {
        Objects.requireNonNull(item, "El ítem no puede ser nulo.");
        if (item.getQuantity() <= 0)
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        this.items.add(item);
        recalculateTotal();
    }

    /** Elimina un ítem del pedido (si existe) y actualiza el total. */
    public void removeItem(OrderItem item) {
        if (this.items.remove(item)) {
            recalculateTotal();
        }
    }

    /** Recalcula el total del pedido. */
    private void recalculateTotal() {
        this.total = this.items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** Cambia el estado del pedido con reglas simples. */
    public void updateStatus(OrderStatus newStatus) {
        Objects.requireNonNull(newStatus, "El estado no puede ser nulo.");
        if (this.status == OrderStatus.CANCELED)
            throw new IllegalStateException("No se puede modificar un pedido cancelado.");
        if (this.status == OrderStatus.DELIVERED && newStatus != OrderStatus.DELIVERED)
            throw new IllegalStateException("Un pedido entregado no puede modificarse.");
        this.status = newStatus;
    }

    /** Marca el pago como aprobado. */
    public void approvePayment() {
        if (this.paymentStatus == PaymentStatus.APPROVED)
            throw new IllegalStateException("El pago ya está aprobado.");
        this.paymentStatus = PaymentStatus.APPROVED;
    }

    /** Cancela el pedido (solo si no fue entregado). */
    public void cancel() {
        if (this.status == OrderStatus.DELIVERED)
            throw new IllegalStateException("No se puede cancelar un pedido entregado.");
        this.status = OrderStatus.CANCELED;
    }

    // ============================
    // 🔹 Getters
    // ============================

    public Long getId() { return id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getCustomerName() { return customerName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public DeliveryType getDeliveryType() { return deliveryType; }
    public String getAddress() { return address; }
    public OrderStatus getStatus() { return status; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public BigDecimal getTotal() { return total; }

    /** Devuelve una copia defensiva de los ítems. */
    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    @Override
    public String toString() {
        return "Order{" +
                "customerName='" + customerName + '\'' +
                ", email='" + email + '\'' +
                ", total=" + total +
                ", status=" + status +
                '}';
    }
}
