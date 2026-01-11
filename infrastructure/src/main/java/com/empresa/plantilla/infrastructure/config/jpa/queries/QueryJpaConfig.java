package com.empresa.plantilla.infrastructure.config.jpa.queries;

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
        basePackages = "com.empresa.plantilla.infrastructure.adapters.output.repositories.queries",
        entityManagerFactoryRef = "queryEntityManagerFactory",
        transactionManagerRef = "queryTransactionManager"
)
public class QueryJpaConfig extends AbstractJpaConfig {

    /**
     * Configura y devuelve una instancia de LocalContainerEntityManagerFactoryBean.
     *
     * @param dataSource    la fuente de datos a utilizar.
     * @param hbm2ddlAuto   la estrategia de generación del esquema de la base de datos.
     * @param defaultSchema el esquema por defecto a utilizar.
     * @return una instancia configurada de LocalContainerEntityManagerFactoryBean.
     */
    @Bean(name = "queryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean queryEntityManagerFactory(
            @Qualifier("queryDataSource") DataSource dataSource,
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
    @Bean(name = "queryDataSource")
    public DataSource queryDataSource(
            @Qualifier("queryDBSecret") DBSecretDto secret,
            @Value("${spring.datasource.query.driverClassName}") String driverClass,
            @Value("${spring.datasource.query.hikari.maximum-pool-size}") int maximumPoolSize,
            @Value("${spring.datasource.query.hikari.minimum-idle}") int minimumIdle) {
        return createDataSource(secret, driverClass, "queryPool", maximumPoolSize, minimumIdle);
    }

    /**
     * Configura y devuelve una instancia de PlatformTransactionManager.
     *
     * @param entityManagerFactory la fábrica de EntityManager a utilizar.
     * @return una instancia configurada de PlatformTransactionManager.
     */
    @Bean(name = "queryTransactionManager")
    public PlatformTransactionManager queryTransactionManager(
            @Qualifier("queryEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return createTransactionManager(entityManagerFactory);
    }
}
