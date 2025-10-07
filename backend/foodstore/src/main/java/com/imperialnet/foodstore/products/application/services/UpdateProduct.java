package com.imperialnet.foodstore.products.application.services;

import com.imperialnet.foodstore.products.application.ports.in.UpdateProductUseCase;
import com.imperialnet.foodstore.products.application.ports.out.CategoryRepositoryPort;
import com.imperialnet.foodstore.products.application.ports.out.ProductRepositoryPort;
import com.imperialnet.foodstore.products.domain.exception.CategoryNotFoundException;
import com.imperialnet.foodstore.products.domain.exception.DuplicateProductNameException;
import com.imperialnet.foodstore.products.domain.exception.ProductNotFoundException;
import com.imperialnet.foodstore.products.domain.model.Category;
import com.imperialnet.foodstore.products.domain.model.Product;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UpdateProduct implements UpdateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final CategoryRepositoryPort categoryRepositoryPort;

    public UpdateProduct(ProductRepositoryPort productRepositoryPort, CategoryRepositoryPort categoryRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
        this.categoryRepositoryPort = categoryRepositoryPort;
    }

    @Override
    public Product update(Long id, Product product) {
        MDC.put("action", "UPDATE_PRODUCT");
        MDC.put("productName", product.getName());
        MDC.put("productId", id.toString());
        //verificar que el producto exista
        Product existing = productRepositoryPort.findById(id);
        if (existing == null) {
            log.warn("El producto no existe");
            throw new ProductNotFoundException("No existe un producto con el id: " + id);
        }
        //verificar que no haya un producto con el mismo nombre
        if (!existing.getName().equals(product.getName())) {
            Product productWithSameName = productRepositoryPort.findByName(product.getName());
            if (productWithSameName != null) {
                log.warn("El producto ya existe");
                throw new DuplicateProductNameException(
                        "Ya existe un producto con el nombre: " + product.getName()
                );
            }
        }
        //verificar que exista la categoria
        if( product.getCategoryId() != null ) {
            Category category = categoryRepositoryPort.findById(product.getCategoryId());
            if (category == null) {
                log.warn("La categoria no existe");
                throw new CategoryNotFoundException(
                        "No existe una categoria con el id: " + product.getCategoryId()
                );
            }
        }
        //actualizar el producto
        try {
            log.info("Actualizando producto: price={}, active={}", product.getPrice(), product.isActive());
           Product toUpdate = new Product(
                   id,
                   product.getCategoryId(),
                   product.getName(),
                   product.getDescription(),
                   product.getPrice(),
                   product.getImageUrl(),
                   product.getDailyStock(),
                   product.isActive()
           );
           Product updated = productRepositoryPort.update(toUpdate);
           log.info("Producto actualizado correctamente: id={}", updated.getId());
           return updated;

        } catch (Exception e) {
            log.error("Error al actualizar el producto", e);
            throw e;
        } finally {
            MDC.clear();
        }

    }
}
