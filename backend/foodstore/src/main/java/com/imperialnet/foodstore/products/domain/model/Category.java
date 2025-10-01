package com.imperialnet.foodstore.products.domain.model;

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
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio y no puede estar vacío");
        }
        if (description != null && description.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía si se especifica");
        }

        this.id = id;
        this.name = name.trim();
        this.description = description != null ? description.trim() : null;
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
