package com.imperialnet.foodstore.products.infrastructure.mapper;


import com.imperialnet.foodstore.products.domain.model.Category;
import com.imperialnet.foodstore.products.infrastructure.web.dto.CategoryResponse;
import com.imperialnet.foodstore.products.infrastructure.web.dto.CreateCategoryRequest;
import com.imperialnet.foodstore.products.infrastructure.web.dto.UpdateCategoryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    // DTO → Dominio
    @Mapping(target = "id", ignore = true)
    Category toDomain(CreateCategoryRequest dto);

    @Mapping(target = "id", ignore = true)
    Category toDomain(UpdateCategoryRequest dto);

    // Dominio → DTO
    CategoryResponse toResponse(Category category);
}
