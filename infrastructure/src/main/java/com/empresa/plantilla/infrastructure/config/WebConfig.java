package com.empresa.plantilla.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * Configuración web principal de la aplicación.
 * <p>
 * Esta clase implementa {@link WebMvcConfigurer} para personalizar la configuración de Spring MVC,
 * incluyendo el manejo de CORS y el registro de interceptores de solicitudes.
 */
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String[] allowedOrigins;
    private final LocaleChangeInterceptor localeChangeInterceptor;

    /**
     * Constructor para inyectar las dependencias de configuración necesarias.
     *
     * @param allowedOrigins          Arreglo de orígenes permitidos para CORS, inyectado desde la propiedad {@code cors.allowedOrigins}.
     * @param localeChangeInterceptor El interceptor que maneja el cambio de idioma, inyectado desde el contexto de Spring.
     */
    public WebConfig(@Value("${cors.allowedOrigins}") String[] allowedOrigins, LocaleChangeInterceptor localeChangeInterceptor) {
        this.allowedOrigins = allowedOrigins;
        this.localeChangeInterceptor = localeChangeInterceptor;
    }

    /**
     * Registra los interceptores de la aplicación.
     * <p>
     * Añade el {@link LocaleChangeInterceptor} al registro de interceptores de Spring MVC, permitiendo
     * que intercepte las solicitudes entrantes para cambiar el idioma de la sesión si se encuentra el
     * parámetro "lang".
     *
     * @param registry el registro de interceptores de Spring MVC.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor);
    }

    /**
     * Configura los mapeos de CORS (Cross-Origin Resource Sharing) para toda la aplicación.
     * <p>
     * Permite solicitudes desde los orígenes definidos en la propiedad {@code cors.allowedOrigins},
     * habilitando los métodos HTTP y cabeceras más comunes para facilitar la integración con clientes web.
     *
     * @param registry el registro de CORS utilizado para configurar los mapeos.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
