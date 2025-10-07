package com.imperialnet.foodstore.products.infrastructure.web.in;

import com.imperialnet.foodstore.products.application.ports.out.StoragePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Productos (imágenes)", description = "Gestión de imágenes de productos")
public class ProductImageUploadController {

    private final StoragePort storagePort;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('DUENO', 'ENCARGADO')")
    @Operation(
            summary = "Subir imagen de producto",
            description = """
                    Permite subir una imagen al servidor y devuelve la URL pública generada.
                    El frontend puede usar esa URL en el campo `imageUrl` al crear o editar un producto.
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Imagen subida correctamente",
                            content = @Content(schema = @Schema(example = "{\"imageUrl\": \"/uploads/products/imagen123.jpg\"}"))),
                    @ApiResponse(responseCode = "400", description = "Archivo inválido", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error al guardar la imagen", content = @Content)
            }
    )
    public ResponseEntity<?> uploadProductImage(
            @RequestPart("image") MultipartFile image
    ) {
        try {
            if (image == null || image.isEmpty()) {
                return ResponseEntity.badRequest().body("El archivo de imagen es obligatorio.");
            }

            String imageUrl = storagePort.save(image, "products");
            log.info("✓ Imagen subida correctamente: {}", imageUrl);

            return ResponseEntity.ok().body(new ImageUploadResponse(imageUrl));

        } catch (Exception e) {
            log.error("✗ Error al subir imagen: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar la imagen.");
        }
    }

    private record ImageUploadResponse(String imageUrl) {}
}
