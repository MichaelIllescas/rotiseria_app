package com.imperialnet.foodstore.products.infrastructure.mapper;



import com.imperialnet.foodstore.products.domain.model.Category;
import com.imperialnet.foodstore.products.infrastructure.persistence.entity.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryPersistenceMapper {

    // Dominio → Entity
    CategoryEntity toEntity(Category category);

    // Entity → Dominio
    Category toDomain(CategoryEntity entity);
}
