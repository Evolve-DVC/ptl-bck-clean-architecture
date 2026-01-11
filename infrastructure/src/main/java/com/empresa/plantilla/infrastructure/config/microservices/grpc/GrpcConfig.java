package com.empresa.plantilla.infrastructure.config.microservices.grpc;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase de configuración para los servicios gRPC.
 * Proporciona un mapa de configuraciones específicas para cada servicio gRPC.
 * Utiliza anotaciones de Lombok para generar automáticamente los métodos getter y setter,
 * y la anotación @Slf4j para habilitar el registro de logs.
 */
@Slf4j
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "grpc.services")
public class GrpcConfig {
    /**
     * Mapa de configuraciones gRPC
     */
    private Map<String, String> grpc = new HashMap<>();

    /**
     * Obtiene el host del servicio gRPC especificado
     *
     * @param name Nombre del servicio gRPC
     * @return Host del servicio gRPC
     */
    public String getServiceHost(String name) {
                        String host = grpc.get(name);
        log.debug("gRPC Service Host for {}: {}", name, host);
        return host;
    }
}
