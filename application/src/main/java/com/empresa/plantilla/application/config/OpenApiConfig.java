package com.empresa.plantilla.application.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI/Swagger para la documentación de la API.
 * <p>
 * Esta configuración proporciona:
 * - Información detallada de la API (título, versión, descripción)
 * - Configuración de seguridad JWT
 * - Múltiples servidores (local, dev, staging, producción)
 * - Información de contacto y licencia
 *
 * @author Equipo de Desarrollo
 * @version 1.0.0
 */
@Configuration
public class OpenApiConfig {

    @Value("${application.version:1.0.0}")
    private String appVersion;

    @Value("${application.name:API de Negocio}")
    private String appName;

    @Value("${application.description:API RESTful para gestión de microservicios}")
    private String appDescription;

    @Value("${server.port:8080}")
    private String serverPort;

    /**
     * Configuración principal de OpenAPI.
     *
     * @return instancia configurada de OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(servers())
                .addSecurityItem(securityRequirement())
                .components(components());
    }

    /**
     * Información general de la API.
     *
     * @return información de la API
     */
    private Info apiInfo() {
        return new Info()
                .title(appName)
                .version(appVersion)
                .description(buildDescription())
                .contact(contact())
                .license(license());
    }

    /**
     * Construye la descripción enriquecida de la API.
     *
     * @return descripción en formato Markdown
     */
    private String buildDescription() {
        return """
                ## 📋 Descripción
                %s
                
                ## ✨ Características
                - ✅ **Autenticación JWT**: Seguridad basada en tokens
                - ✅ **Paginación y Filtrado**: Consultas optimizadas
                - ✅ **Validaciones Robustas**: Validación de datos de entrada
                - ✅ **Manejo de Errores Estandarizado**: Respuestas consistentes
                - ✅ **Internacionalización (i18n)**: Soporte multi-idioma
                - ✅ **Versionamiento de API**: Control de versiones
                
                ## 🔐 Autenticación
                Para usar los endpoints protegidos:
                1. Obtén un token en `/api/auth/login`
                2. Haz clic en el botón **"Authorize"** 🔓 en la parte superior
                3. Ingresa: `Bearer {tu-token-jwt}`
                4. Haz clic en **"Authorize"** y luego **"Close"**
                
                ## 📊 Códigos de Estado HTTP
                | Código | Descripción |
                |--------|-------------|
                | `200 OK` | ✅ Solicitud exitosa |
                | `201 Created` | ✅ Recurso creado exitosamente |
                | `204 No Content` | ✅ Solicitud exitosa sin contenido |
                | `400 Bad Request` | ❌ Error en los datos enviados |
                | `401 Unauthorized` | ❌ No autenticado (token inválido/expirado) |
                | `403 Forbidden` | ❌ Sin permisos para realizar la acción |
                | `404 Not Found` | ❌ Recurso no encontrado |
                | `422 Unprocessable Entity` | ❌ Error de validación |
                | `500 Internal Server Error` | ❌ Error interno del servidor |
                
                ## 🌐 Internacionalización
                La API soporta múltiples idiomas. Incluye el header:
                ```
                Accept-Language: es
                ```
                Idiomas soportados: `es` (Español), `en` (English), `pt` (Português)
                
                ## 📄 Formato de Respuesta
                Todas las respuestas siguen el formato estándar:
                ```json
                {
                  "success": true,
                  "message": "Operación exitosa",
                  "data": { ... },
                  "timestamp": "2026-01-17T10:30:00Z"
                }
                ```
                """.formatted(appDescription);
    }

    /**
     * Información de contacto del equipo de desarrollo.
     *
     * @return información de contacto
     */
    private Contact contact() {
        return new Contact()
                .name("Equipo de Desarrollo")
                .email("desarrollo@empresa.com")
                .url("https://empresa.com");
    }

    /**
     * Información de licencia del proyecto.
     *
     * @return información de licencia
     */
    private License license() {
        return new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0.html");
    }

    /**
     * Configuración de servidores disponibles.
     *
     * @return lista de servidores
     */
    private List<Server> servers() {
        return List.of(
                new Server()
                        .url("http://localhost:" + serverPort)
                        .description("🖥️ Servidor Local"),
                new Server()
                        .url("https://dev.empresa.com")
                        .description("🔧 Desarrollo"),
                new Server()
                        .url("https://staging.empresa.com")
                        .description("🧪 Staging"),
                new Server()
                        .url("https://api.empresa.com")
                        .description("🚀 Producción")
        );
    }

    /**
     * Requerimiento de seguridad global para todos los endpoints.
     *
     * @return requerimiento de seguridad
     */
    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement()
                .addList("Bearer Authentication");
    }

    /**
     * Componentes de seguridad para la API.
     *
     * @return componentes de OpenAPI
     */
    private Components components() {
        return new Components()
                .addSecuritySchemes("Bearer Authentication",
                        new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .description("""
                                        Ingresa el token JWT en el formato: **Bearer {token}**
                                        
                                        Ejemplo:
                                        ```
                                        Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
                                        ```
                                        """));
    }
}
