package com.imperialnet.foodstore.products.domain.model;

import java.util.Objects;

/**
 * Entity: Category
 * Representa una categoría de productos (ej. Pizzas, Empanadas).
 */
public class Category {

    private Long id;
    private String name;
    private String description;
    private boolean active;

    // --- Constructor ---
    public Category(Long id, String name, String description, boolean active) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "El nombre es obligatorio");
        this.description = description;
        this.active = active;
    }

    // --- Reglas de dominio ---
    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    // --- Getters ---
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isActive() { return active; }
}
