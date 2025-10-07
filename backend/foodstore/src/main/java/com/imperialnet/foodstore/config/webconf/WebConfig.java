package com.imperialnet.foodstore.config.webconf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/** * Configura los recursos estáticos del servidor,

 * permitiendo servir archivos desde la carpeta "uploads/"
 * como contenido público bajo la URL /uploads/**.
 *
 * Ejemplo: http://localhost:8080/uploads/products/pizza.jpg
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Carpeta base donde se guardan los archivos
        Path uploadDir = Paths.get("uploads").toAbsolutePath();
        String uploadPath = uploadDir.toUri().toString();

        // Registrar el handler para que /uploads/** sirva los archivos locales
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath)
                .setCachePeriod(0); // sin caché durante desarrollo
    }
}
