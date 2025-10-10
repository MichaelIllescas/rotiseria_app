// backend/foodstore/src/main/java/com/imperialnet/foodstore/products/infrastructure/persistence/adapter/ProductRepositoryAdapter.java
package com.imperialnet.foodstore.products.infrastructure.persistence.adapter;

import com.imperialnet.foodstore.products.application.ports.out.ProductRepositoryPort;
import com.imperialnet.foodstore.products.domain.exception.ProductNotFoundException;
import com.imperialnet.foodstore.products.domain.model.Product;
import com.imperialnet.foodstore.products.infrastructure.mapper.ProductPersistenceMapper;
import com.imperialnet.foodstore.products.infrastructure.persistence.entity.ProductEntity;
import com.imperialnet.foodstore.products.infrastructure.persistence.repository.ProductRepositoryJpa;
import org.springframework.stereotype.Repository;

import java.util.List;

// Adaptador que conecta el dominio con la capa JPA para Product
@Repository
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    // Repositorio JPA subyacente
    private final ProductRepositoryJpa jpaRepository;
    // Mapper entre entidad de persistencia y modelo de dominio
    private final ProductPersistenceMapper mapper;

    // Constructor con inyección de dependencias
    public ProductRepositoryAdapter(ProductRepositoryJpa jpaRepository, ProductPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    // Guarda un producto (crea o actualiza según tenga id)
    @Override
    public Product save(Product product) {
        ProductEntity productEntity = mapper.toEntity(product);
        ProductEntity savedEntity = jpaRepository.save(productEntity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Product findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new ProductNotFoundException("No se encontró el producto con ID: " + id));
    }


    // Elimina un producto por id
    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    // Devuelve todos los productos
    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    // Busca productos por id de categoría
    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        return jpaRepository.findByCategoryId(categoryId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    // Busca productos cuyo nombre contenga el fragmento dado
    @Override
    public List<Product> findByNameContaining(String name) {
        return jpaRepository.findByNameContaining(name).stream()
                .map(mapper::toDomain)
                .toList();
    }

    // Verifica existencia por id
    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    // Devuelve el primer producto cuyo nombre contenga el fragmento (asume lista no vacía)
    @Override
    public Product findByName(String name) {
        List<ProductEntity> results = jpaRepository.findByNameContaining(name);
        if (results.isEmpty()) {
            return null; // No se encontró ningún producto con ese nombre
        }
        return mapper.toDomain(results.get(0));
    }


    // Actualiza un producto (delegado a save)
    @Override
    public Product update(Product product) {
        return save(product);
    }

    // Cuenta productos por id de categoría
    @Override
    public long countByCategoryId(Long categoryId) {
        return jpaRepository.countByCategoryId(categoryId);
    }
    @Override
    public List<Product> saveAll(List<Product> products) {
        List<ProductEntity> entities = products.stream()
                .map(mapper::toEntity)
                .toList();
        List<ProductEntity> savedEntities = jpaRepository.saveAll(entities);
        return savedEntities.stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Product> findActiveProducts() {
        return jpaRepository.findByActiveTrue().stream()
                .map(mapper::toDomain)
                .toList();
    }
}
