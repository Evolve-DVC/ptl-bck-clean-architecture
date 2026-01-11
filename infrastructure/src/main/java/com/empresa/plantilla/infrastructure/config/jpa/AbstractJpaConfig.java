package com.empresa.plantilla.infrastructure.config.jpa;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Clase abstracta base para configuraciones JPA.
 * Contiene la lógica común para configurar EntityManager, DataSource y TransactionManager.
 */
public abstract class AbstractJpaConfig {

    /**
     * Crea y configura una instancia de LocalContainerEntityManagerFactoryBean.
     *
     * @param dataSource    la fuente de datos a utilizar.
     * @param hbm2ddlAuto   la estrategia de generación del esquema de la base de datos.
     * @param defaultSchema el esquema por defecto a utilizar.
     * @return una instancia configurada de LocalContainerEntityManagerFactoryBean.
     */
    protected LocalContainerEntityManagerFactoryBean createEntityManagerFactory(
            DataSource dataSource,
            String hbm2ddlAuto,
            String defaultSchema) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("co.com.dvc.infrastructure.entities");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", hbm2ddlAuto);
        properties.setProperty("hibernate.default_schema", defaultSchema);
        em.setJpaProperties(properties);

        return em;
    }

    /**
     * Crea y configura una instancia de DataSource utilizando HikariCP.
     *
     * @param secret      el secreto de la base de datos que contiene las credenciales y la URL.
     * @param driverClass el nombre de la clase del controlador JDBC.
     * @param poolName    el nombre del pool de conexiones.
     * @return una instancia configurada de DataSource.
     */
    protected DataSource createDataSource(
            DBSecretDto secret,
            String driverClass,
            String poolName,
            int maximumPoolSize,
            int minimumIdle) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(secret.getUrl());
        config.setUsername(secret.getUsername());
        config.setPassword(secret.getPassword());
        config.setDriverClassName(driverClass);
        config.setPoolName(poolName);
        config.setMaximumPoolSize(maximumPoolSize);
        config.setMinimumIdle(minimumIdle);
        return new HikariDataSource(config);
    }

    /**
     * Crea y configura una instancia de PlatformTransactionManager.
     *
     * @param entityManagerFactory la fábrica de EntityManager a utilizar.
     * @return una instancia configurada de PlatformTransactionManager.
     */
    protected PlatformTransactionManager createTransactionManager(
            EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
