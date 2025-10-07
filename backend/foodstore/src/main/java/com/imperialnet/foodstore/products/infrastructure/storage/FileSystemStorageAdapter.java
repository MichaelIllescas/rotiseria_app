package com.imperialnet.foodstore.products.infrastructure.storage;

import com.imperialnet.foodstore.products.application.ports.out.StoragePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

/**
 * Implementación local del puerto StoragePort.
 *
 * Guarda los archivos (imágenes) en el sistema de archivos del servidor
 * dentro de la carpeta "uploads/", creando las subcarpetas necesarias.
 *
 * Esta implementación es ideal para el MVP, ya que evita dependencias externas
 * y permite servir los archivos directamente desde el backend (por ejemplo en localhost:8080/uploads/...).
 */
@Slf4j
@Service
public class FileSystemStorageAdapter implements StoragePort {

    private static final String BASE_UPLOAD_DIR = "uploads/";

    @Override
    public String save(MultipartFile file, String folder) {
        try {
            // Crear la carpeta si no existe
            Path directory = Paths.get(BASE_UPLOAD_DIR, folder).toAbsolutePath();
            Files.createDirectories(directory);

            // Generar un nombre único para evitar colisiones
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path destination = directory.resolve(filename);

            // Guardar el archivo en disco
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            String publicUrl = "/uploads/" + folder + "/" + filename;

            log.info("Archivo guardado correctamente en: {}", publicUrl);
            return publicUrl;

        } catch (IOException e) {
            log.error("Error al guardar archivo: {}", e.getMessage(), e);
            throw new RuntimeException("Error al guardar el archivo en el sistema local", e);
        }
    }

    @Override
    public void delete(String fileUrl) {
        try {
            String relativePath = fileUrl.replaceFirst("^/uploads/", "");
            Path path = Paths.get(BASE_UPLOAD_DIR, relativePath).toAbsolutePath();
            Files.deleteIfExists(path);
            log.info("Archivo eliminado: {}", fileUrl);
        } catch (IOException e) {
            log.error("Error al eliminar archivo: {}", e.getMessage(), e);
            throw new RuntimeException("Error al eliminar el archivo del sistema local", e);
        }
    }
}
