package com.imperialnet.foodstore.products.infrastructure.mapper;


import com.imperialnet.foodstore.products.application.ports.out.ProductRepositoryPort;
import com.imperialnet.foodstore.products.domain.model.Category;
import com.imperialnet.foodstore.products.infrastructure.web.dto.CategoryResponse;
import com.imperialnet.foodstore.products.infrastructure.web.dto.CreateCategoryRequest;
import com.imperialnet.foodstore.products.infrastructure.web.dto.UpdateCategoryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class CategoryMapper {

    @Autowired
    protected ProductRepositoryPort productRepository;

    // DTO → Dominio
    @Mapping(target = "id", ignore = true)
    public abstract Category toDomain(CreateCategoryRequest dto);

    @Mapping(target = "id", ignore = true)
    public abstract Category toDomain(UpdateCategoryRequest dto);

    // Dominio → DTO con conteo de productos
    public CategoryResponse toResponse(Category category) {
        Long productCount = productRepository.countByCategoryId(category.getId());
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.isActive(),
                productCount
        );
    }
}
