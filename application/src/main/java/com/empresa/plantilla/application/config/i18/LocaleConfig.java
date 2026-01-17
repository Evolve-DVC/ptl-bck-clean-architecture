package com.empresa.plantilla.application.config.i18;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Arrays;
import java.util.Locale;

/**
 * Clase de configuración para la internacionalización (i18n).
 * Define los beans necesarios para resolver el locale (idioma) de las solicitudes
 * y para cargar los mensajes desde los archivos de propiedades.
 */
@Configuration
public class LocaleConfig implements WebMvcConfigurer {

    /**
     * Configura el bean {@link LocaleResolver} que determina el idioma del usuario para cada solicitud.
     * <p>
     * Utiliza {@link AcceptHeaderLocaleResolver} que es más adecuado para APIs REST stateless.
     * Lee el idioma del header HTTP "Accept-Language" enviado por el cliente.
     * Si no se proporciona el header o el idioma no es soportado, se utiliza el español como valor predeterminado.
     * <p>
     * Idiomas soportados: español (es), inglés (en), portugués (pt).
     *
     * @return una instancia de {@code LocaleResolver} configurada para gestionar el idioma desde el header Accept-Language.
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(Locale.forLanguageTag("es")); // Idioma por defecto
        localeResolver.setSupportedLocales(Arrays.asList(
                Locale.forLanguageTag("es"),
                Locale.forLanguageTag("en"),
                Locale.forLanguageTag("pt")
        ));
        return localeResolver;
    }

    /**
     * Configura el bean {@link LocaleChangeInterceptor} que permite cambiar el idioma actual mediante un parámetro de query.
     * <p>
     * Este interceptor es opcional y funciona como alternativa al header Accept-Language.
     * Se activa cuando detecta el parámetro "lang" en la URL. Por ejemplo: {@code /api/recurso?lang=en}
     * cambiará el idioma a inglés para esa solicitud.
     * <p>
     * Nota: Para APIs REST stateless, se recomienda usar el header Accept-Language en lugar de este parámetro.
     *
     * @return una instancia de {@code LocaleChangeInterceptor} configurada.
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang"); // Parámetro opcional para cambiar el idioma, ej.: ?lang=en
        return lci;
    }

    /**
     * Registra el interceptor de cambio de locale.
     * Este método es llamado automáticamente por Spring MVC para configurar interceptores.
     *
     * @param registry el registro de interceptores de Spring MVC
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    /**
     * Configura el bean {@link ResourceBundleMessageSource} que gestiona la carga de mensajes de internacionalización.
     * <p>
     * Este bean se configura para buscar archivos de propiedades con el nombre base "messages" en el classpath
     * (ej. {@code messages.properties}, {@code messages_en.properties}, {@code messages_pt.properties}).
     * Utiliza la codificación UTF-8 para asegurar la correcta visualización de caracteres especiales.
     *
     * @return una instancia de {@code MessageSource} configurada para resolver mensajes.
     */
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages"); // Busca archivos messages.properties, messages_es.properties, etc.
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true); // Devuelve la clave si no encuentra el mensaje
        return messageSource;
    }
}
