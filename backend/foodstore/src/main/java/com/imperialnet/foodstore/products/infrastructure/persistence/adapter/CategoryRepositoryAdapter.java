package com.imperialnet.foodstore.products.infrastructure.persistence.adapter;

import com.imperialnet.foodstore.products.application.ports.out.CategoryRepositoryPort;
import com.imperialnet.foodstore.products.domain.model.Category;
import com.imperialnet.foodstore.products.infrastructure.mapper.CategoryPersistenceMapper;
import com.imperialnet.foodstore.products.infrastructure.persistence.entity.CategoryEntity;
import com.imperialnet.foodstore.products.infrastructure.persistence.repository.CategoryRepositoryJpa;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// Adaptador que conecta el dominio con la capa JPA para Category
public class CategoryRepositoryAdapter implements CategoryRepositoryPort {

    // Repositorio JPA subyacente
    private final CategoryRepositoryJpa categoryRepository;
    // Mapper para convertir entre entidad y modelo de dominio
    private final CategoryPersistenceMapper categoryPersistenceMapper;

    // Constructor que inyecta dependencias
    public CategoryRepositoryAdapter(CategoryRepositoryJpa categoryRepository,
                                     CategoryPersistenceMapper categoryPersistenceMapper) {
        this.categoryPersistenceMapper = categoryPersistenceMapper;
        this.categoryRepository = categoryRepository;
    }

    // Verifica si existe una categoría por id
    @Override
    public boolean existsById(Long id) {
        return categoryRepository.existsById(id);
    }

    // Guarda una nueva categoría (o actualiza si ya tiene id)
    @Override
    public Category save(Category category) {
        CategoryEntity categoryEntity = categoryPersistenceMapper.toEntity(category);
        return categoryPersistenceMapper.toDomain(categoryRepository.save(categoryEntity));
    }

    // Busca una categoría por id, devuelve null si no existe
    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryPersistenceMapper::toDomain)
                .orElse(null);
    }

    // Elimina una categoría por id
    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    // Recupera todas las categorías
    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryPersistenceMapper::toDomain)
                .toList();
    }

    // Actualiza una categoría existente (delegado a save)
    @Override
    public Category update(Category category) {
        CategoryEntity categoryEntity = categoryPersistenceMapper.toEntity(category);
        return categoryPersistenceMapper.toDomain(categoryRepository.save(categoryEntity));
    }

    // Busca categorías cuyo nombre contenga el fragmento dado
    @Override
    public List<Category> findByNameContaining(String name) {
        return categoryRepository.findByNameContaining(name)
                .stream()
                .map(categoryPersistenceMapper::toDomain)
                .toList();
    }

    // Devuelve la primera coincidencia por nombre parcial o null
    @Override
    public Category findByName(String name) {
        return categoryRepository.findByNameContaining(name)
                .stream()
                .map(categoryPersistenceMapper::toDomain)
                .findFirst()
                .orElse(null);
    }
}