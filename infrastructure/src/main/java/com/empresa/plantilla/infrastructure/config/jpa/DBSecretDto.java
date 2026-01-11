package com.empresa.plantilla.infrastructure.config.jpa;

import lombok.Builder;
import lombok.Getter;

/**
 * Clase que representa los secretos de configuración de la base de datos.
 * Proporciona información como la URL de conexión, el nombre de usuario y la contraseña.
 * Utiliza las anotaciones de Lombok para simplificar la creación de objetos y el acceso a sus propiedades.
 */
@Builder
@Getter
public class DBSecretDto {
    /**
     * URL de conexión a la base de datos.
     */
    private final String url;

    /**
     * Nombre de usuario para la conexión a la base de datos.
     */
    private final String username;

    /**
     * Contraseña para la conexión a la base de datos.
     */
    private final String password;
}