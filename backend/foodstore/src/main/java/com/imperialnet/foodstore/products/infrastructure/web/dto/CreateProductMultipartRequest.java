package com.imperialnet.foodstore.products.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO auxiliar usado exclusivamente para documentación Swagger.
 * Representa una solicitud multipart con un JSON (product) y un archivo (image).
 */
public class CreateProductMultipartRequest {

    @Schema(
            description = "Datos del producto en formato JSON",
            implementation = CreateProductRequest.class,
            required = true
    )
    private CreateProductRequest product;

    @Schema(
            description = "Archivo de imagen del producto",
            type = "string",
            format = "binary",
            required = true
    )
    private MultipartFile image;

    // Getters y Setters (Swagger los necesita para generar el esquema)
    public CreateProductRequest getProduct() {
        return product;
    }

    public void setProduct(CreateProductRequest product) {
        this.product = product;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
