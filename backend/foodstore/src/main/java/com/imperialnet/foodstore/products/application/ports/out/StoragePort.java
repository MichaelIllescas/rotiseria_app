package com.imperialnet.foodstore.products.application.ports.out;

import org.springframework.web.multipart.MultipartFile;

/**
 * Puerto de salida para la gestión del almacenamiento de archivos.
 *
 * Esta interfaz define las operaciones necesarias para guardar o eliminar archivos
 * sin acoplar la aplicación a un tipo específico de almacenamiento (local, nube, etc.).
 *
 * En el MVP se implementará en el adaptador FileSystemStorageAdapter,
 * pero en el futuro podrá reemplazarse por un almacenamiento remoto (S3, R2, etc.)
 * sin afectar la lógica de negocio.
 */
public interface StoragePort {

    /**
     * Guarda un archivo en una carpeta específica del sistema de almacenamiento.
     *
     * @param file   el archivo recibido (imagen u otro)
     * @param folder la carpeta donde se guardará (por ejemplo, "products")
     * @return la URL pública del archivo guardado
     */
    String save(MultipartFile file, String folder);

    /**
     * Elimina un archivo existente del almacenamiento.
     *
     * @param fileUrl la URL o ruta del archivo a eliminar
     */
    void delete(String fileUrl);
}
