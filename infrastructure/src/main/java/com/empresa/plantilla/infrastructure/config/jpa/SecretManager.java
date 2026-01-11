package com.empresa.plantilla.infrastructure.config.jpa;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecretManager {

    /**
     * Configura y devuelve una instancia de DBSecretDto para la base de datos de comandos.
     *
     * @param url      la URL de la base de datos.
     * @param username el nombre de usuario para la conexión a la base de datos.
     * @param password la contraseña para la conexión a la base de datos.
     * @return una instancia configurada de DBSecretDto.
     */
    @Bean("commandDBSecret")
    public DBSecretDto commandDBSecret(
            @Value("${spring.datasource.command.url}") String url,
            @Value("${spring.datasource.command.username}") String username,
            @Value("${spring.datasource.command.password}") String password) {
        return DBSecretDto.builder()
                .url(url)
                .username(username)
                .password(password)
                .build();
    }

    /**
     * Configura y devuelve una instancia de DBSecretDto para la base de datos de consultas.
     *
     * @param url      la URL de la base de datos.
     * @param username el nombre de usuario para la conexión a la base de datos.
     * @param password la contraseña para la conexión a la base de datos.
     * @return una instancia configurada de DBSecretDto.
     */
    @Bean("queryDBSecret")
    public DBSecretDto queryDBSecret(
            @Value("${spring.datasource.query.url}") String url,
            @Value("${spring.datasource.query.username}") String username,
            @Value("${spring.datasource.query.password}") String password) {
        return DBSecretDto.builder()
                .url(url)
                .username(username)
                .password(password)
                .build();
    }
}
