package com.imperialnet.foodstore.products.application.ports.out;

import com.imperialnet.foodstore.products.domain.model.Product;

import java.util.List;

public interface ProductRepositoryPort {
    Product save(Product product);
    Product findById(Long id);
    void deleteById(Long id);
    List<Product> findAll();
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByNameContaining(String name);
    boolean existsById(Long id);
    Product findByName(String name);
    Product update(Product product);
    long countByCategoryId(Long categoryId);
    List <Product> saveAll(List<Product> products);
    List<Product> findActiveProducts();
}
