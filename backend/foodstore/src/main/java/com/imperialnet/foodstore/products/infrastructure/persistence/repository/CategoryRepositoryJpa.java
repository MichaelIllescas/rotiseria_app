package com.imperialnet.foodstore.products.infrastructure.persistence.repository;

import com.imperialnet.foodstore.products.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface CategoryRepositoryJpa extends JpaRepository<CategoryEntity, Long> {
   List<CategoryEntity>  findByNameContaining(String name);

    List<CategoryEntity> findByActiveTrue();
}
