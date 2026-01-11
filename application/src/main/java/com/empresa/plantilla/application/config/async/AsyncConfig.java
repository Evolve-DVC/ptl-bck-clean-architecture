package com.empresa.plantilla.application.config.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Clase de configuración para habilitar la ejecución asíncrona en la aplicación.
 * Define un bean de tipo Executor que se utiliza para manejar tareas asíncronas.
 */
@EnableAsync
@Configuration
public class AsyncConfig {

    /**
     * Crea y configura un bean de tipo ThreadPoolTaskExecutor para manejar tareas asíncronas.
     * Este bean se registra con el nombre "asyncExecutor" y se utiliza como el ejecutor predeterminado
     * para métodos anotados con @Async en la aplicación.
     *
     * @return Un Executor configurado para manejar tareas asíncronas.
     */
    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Configura el número mínimo de hilos en el pool.
        executor.setCorePoolSize(5);

        // Configura el número máximo de hilos en el pool.
        executor.setMaxPoolSize(10);

        // Configura la capacidad de la cola para tareas pendientes.
        executor.setQueueCapacity(500);

        // Configura el prefijo para los nombres de los hilos creados por este executor.
        executor.setThreadNamePrefix("savThread-");

        // Inicializa el executor con la configuración especificada.
        executor.initialize();

        return executor;
    }
}