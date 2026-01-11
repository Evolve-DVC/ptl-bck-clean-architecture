package com.empresa.plantilla.infrastructure.config.jpa.command;

import com.empresa.plantilla.infrastructure.config.jpa.AbstractJpaConfig;
import com.empresa.plantilla.infrastructure.config.jpa.DBSecretDto;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Clase de configuración para JPA.
 * Proporciona la configuración necesaria para la conexión a la base de datos,
 * el manejo de entidades y la integración con Hibernate.
 */
@Configuration
@EnableJpaRepositories(
        basePackages = "com.empresa.plantilla.infrastructure.adapters.output.repositories.commands",
        entityManagerFactoryRef = "commandEntityManagerFactory",
        transactionManagerRef = "commandTransactionManager"
)
public class CommandJpaConfig extends AbstractJpaConfig {

    @Bean(name = "commandEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean commandEntityManagerFactory(
            @Qualifier("commandDataSource") DataSource dataSource,
            @Value("${spring.hibernate.hbm2ddl.auto}") String hbm2ddlAuto,
            @Value("${spring.jpa.properties.hibernate.default_schema}") String defaultSchema) {
        return createEntityManagerFactory(dataSource, hbm2ddlAuto, defaultSchema);
    }

    /**
     * Configura y devuelve una instancia de DataSource utilizando HikariCP.
     *
     * @param secret      el secreto de la base de datos que contiene las credenciales y la URL.
     * @param driverClass el nombre de la clase del controlador JDBC.
     * @return una instancia configurada de DataSource.
     */
    @Bean(name = "commandDataSource")
    public DataSource commandDataSource(
            @Qualifier("commandDBSecret") DBSecretDto secret,
            @Value("${spring.datasource.command.driverClassName}") String driverClass,
            @Value("${spring.datasource.command.hikari.maximum-pool-size}") int maximumPoolSize,
            @Value("${spring.datasource.command.hikari.minimum-idle}") int minimumIdle) {
        return createDataSource(secret, driverClass, "commandPool", maximumPoolSize, minimumIdle);
    }

    /**
     * Configura y devuelve una instancia de PlatformTransactionManager.
     *
     * @param entityManagerFactory la fábrica de EntityManager a utilizar.
     * @return una instancia configurada de PlatformTransactionManager.
     */
    @Bean(name = "commandTransactionManager")
    public PlatformTransactionManager commandTransactionManager(
            @Qualifier("commandEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return createTransactionManager(entityManagerFactory);
    }
}
