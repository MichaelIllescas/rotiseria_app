package com.imperialnet.foodstore.products.domain.model;

import java.math.BigDecimal;

/**
 * Aggregate root: Product
 * Representa un producto del catálogo de la rotisería.
 */
public class Product {

    private Long id;
    private Long categoryId;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private int dailyStock;
    private boolean active;

    // --- Constructor ---
    public Product(Long id, Long categoryId, String name, String description,
                   BigDecimal price, String imageUrl, int dailyStock, boolean active) {
        this.id = id;
        this.categoryId = categoryId;

        // Validaciones de invariantes
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio y no puede estar vacío");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio es obligatorio y debe ser mayor a cero");
        }
        if (dailyStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }

        this.name = name.trim();
        this.description = description != null ? description.trim() : null;
        this.price = price;
        this.imageUrl = imageUrl;
        this.dailyStock = dailyStock;
        this.active = active;
    }

    // --- Reglas de dominio ---
    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void decreaseStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        if (this.dailyStock < quantity) {
            throw new IllegalStateException("Stock insuficiente para el producto " + this.name);
        }
        this.dailyStock -= quantity;
    }

    public void resetDailyStock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        this.dailyStock = quantity;
    }

    // --- Getters ---
    public Long getId() { return id; }
    public Long getCategoryId() { return categoryId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public int getDailyStock() { return dailyStock; }
    public boolean isActive() { return active; }
}
