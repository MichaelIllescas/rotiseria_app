package com.imperialnet.foodstore.products.infrastructure.mapper;


import com.imperialnet.foodstore.products.domain.model.Product;
import com.imperialnet.foodstore.products.infrastructure.web.dto.CreateProductRequest;
import com.imperialnet.foodstore.products.infrastructure.web.dto.ProductResponse;
import com.imperialnet.foodstore.products.infrastructure.web.dto.UpdateProductRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // DTO → Dominio
    @Mapping(target = "id", ignore = true) // el id lo genera la BD
    Product toDomain(CreateProductRequest dto);

    @Mapping(target = "id", ignore = true)
    Product toDomain(UpdateProductRequest dto);

    // Dominio → DTO
    ProductResponse toResponse(Product product);
}
