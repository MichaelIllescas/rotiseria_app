package com.imperialnet.foodstore.products.infrastructure.mapper;



import com.imperialnet.foodstore.products.domain.model.Product;
import com.imperialnet.foodstore.products.infrastructure.persistence.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductPersistenceMapper {

    // Dominio → Entity
    @Mapping(source = "categoryId", target = "category.id")
    ProductEntity toEntity(Product product);

    // Entity → Dominio
    @Mapping(source = "category.id", target = "categoryId")
    Product toDomain(ProductEntity entity);
}
