package com.empresa.plantilla.infrastructure.config.microservices.rest;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase de configuración para los microservicios.
 * Proporciona un mapa de configuraciones específicas para cada microservicio.
 * Utiliza anotaciones de Lombok para generar automáticamente los métodos getter y setter,
 * y la anotación @Slf4j para habilitar el registro de logs.
 */
@Slf4j
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "rest.services")
public class RestConfig {

    /**
     * Mapa que contiene las configuraciones de los microservicios.
     * La clave representa el nombre del microservicio y el valor su configuración asociada.
     */
    private Map<String, String> microservice = new HashMap<>();

    /**
     * Obtiene la configuración del microservicio especificado.
     *
     * @param name Nombre del microservicio
     * @return Configuración del microservicio
     */
    public String getServiceConfig(String name) {
                        String config = microservice.get(name);
        log.debug("Microservice Config for {}: {}", name, config);
        return config;
    }
}