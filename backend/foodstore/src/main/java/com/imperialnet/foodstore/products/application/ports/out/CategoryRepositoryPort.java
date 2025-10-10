package com.imperialnet.foodstore.products.application.ports.out;

import com.imperialnet.foodstore.products.domain.model.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CategoryRepositoryPort {
    boolean existsById(Long id);
    Category save(Category category);
    Category findById(Long id);
    void deleteById(Long id);
    List<Category> findAll();
    Category update(Category category);
    List<Category> findByNameContaining(String name);
    Category findByName(String name);
    List<Category> findActiveCategories();
}
