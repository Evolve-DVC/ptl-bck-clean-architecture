package com.empresa.plantilla.commons.services.i18;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Servicio para gestionar mensajes internacionalizados.
 * Proporciona métodos para obtener mensajes desde los archivos de recursos (messages.properties)
 * según el locale actual del usuario.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageSource messageSource;

    /**
     * Obtiene un mensaje internacionalizado según la clave proporcionada.
     * Utiliza el locale actual del contexto de Spring.
     *
     * @param key la clave del mensaje en los archivos de properties
     * @return el mensaje traducido
     */
    public String getMessage(String key) {
        return getMessage(key, (Object[]) null);
    }

    /**
     * Obtiene un mensaje internacionalizado con parámetros.
     * Los parámetros se sustituyen en los placeholders {0}, {1}, etc.
     *
     * @param key    la clave del mensaje en los archivos de properties
     * @param params los parámetros a sustituir en el mensaje
     * @return el mensaje traducido con los parámetros sustituidos
     */
    public String getMessage(String key, Object... params) {
        try {
            Locale locale = LocaleContextHolder.getLocale();
            return messageSource.getMessage(key, params, locale);
        } catch (Exception e) {
            log.warn("No se encontró traducción para la clave '{}'. Retornando la clave como mensaje.", key);
            return key;
        }
    }

    /**
     * Obtiene un mensaje internacionalizado con un locale específico.
     *
     * @param key    la clave del mensaje en los archivos de properties
     * @param locale el locale a utilizar
     * @return el mensaje traducido
     */
    public String getMessage(String key, Locale locale) {
        return getMessage(key, locale, (Object[]) null);
    }

    /**
     * Obtiene un mensaje internacionalizado con parámetros y un locale específico.
     *
     * @param key    la clave del mensaje en los archivos de properties
     * @param locale el locale a utilizar
     * @param params los parámetros a sustituir en el mensaje
     * @return el mensaje traducido con los parámetros sustituidos
     */
    public String getMessage(String key, Locale locale, Object... params) {
        try {
            return messageSource.getMessage(key, params, locale);
        } catch (Exception e) {
            log.warn("No se encontró traducción para la clave '{}' en locale '{}'. Retornando la clave como mensaje.", key, locale);
            return key;
        }
    }
}

