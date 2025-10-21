package com.imperialnet.foodstore.orders.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entidad de dominio: OrderItem
 * Representa un producto incluido dentro de un pedido.
 * Forma parte del agregado Order.
 */
public class OrderItem {

    private final Long productId;
    private String productName;
    private BigDecimal unitPrice;
    private final Integer quantity;

    // ============================
    // 🔹 Constructor principal (solo con ID y cantidad)
    // ============================
    public OrderItem(Long productId, Integer quantity) {
        if (productId == null)
            throw new IllegalArgumentException("El ID del producto es obligatorio.");
        if (quantity == null || quantity <= 0)
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");

        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = BigDecimal.ZERO; // 👈 valor por defecto seguro
        this.productName = "DESCONOCIDO"; // 👈 nombre temporal hasta que se complete
    }

    // ============================
    // 🔹 Métodos de dominio
    // ============================

    /** Calcula el subtotal del ítem (precio * cantidad). */
    public BigDecimal getSubtotal() {
        if (unitPrice == null) return BigDecimal.ZERO;
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    /** Devuelve una nueva instancia con cantidad actualizada. */
    public OrderItem withQuantity(Integer newQuantity) {
        if (newQuantity == null || newQuantity <= 0)
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        return new OrderItem(this.productId, newQuantity);
    }

    /** Verifica si otro ítem representa el mismo producto. */
    public boolean isSameProduct(OrderItem other) {
        Objects.requireNonNull(other, "El ítem a comparar no puede ser nulo.");
        return this.productId.equals(other.productId);
    }

    /** Indica si el ítem es gratuito (precio 0). */
    public boolean isFreeItem() {
        return unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) == 0;
    }

    // ============================
    // 🔹 Métodos de actualización controlada
    // ============================

    /** Completa los datos del producto (nombre y precio). */
    public void completeProductData(String name, BigDecimal price) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("El precio no puede ser negativo.");

        this.productName = name.trim();
        this.unitPrice = price;
    }

    // ============================
    // 🔹 Getters
    // ============================

    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public Integer getQuantity() { return quantity; }

    // ============================
    // 🔹 Igualdad por identidad de producto
    // ============================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem)) return false;
        OrderItem that = (OrderItem) o;
        return Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    @Override
    public String toString() {
        return productName + " x" + quantity + " ($" + unitPrice + ")";
    }
}
