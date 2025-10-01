package com.imperialnet.foodstore.products.infrastructure.persistence.repository;

import com.imperialnet.foodstore.products.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepositoryJpa extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByCategoryId(Long categoryId);
    List<ProductEntity> findByNameContaining(String name);
    long countByCategoryId(Long categoryId);
}
