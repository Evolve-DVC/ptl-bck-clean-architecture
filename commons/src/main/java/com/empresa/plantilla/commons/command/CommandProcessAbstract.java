package com.empresa.plantilla.commons.command;

import com.empresa.plantilla.commons.exception.DomainException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Clase abstracta que define el patrón de procesamiento de comandos.
 * 
 * <p>Esta clase proporciona una estructura base para ejecutar comandos de forma
 * síncrona o asíncrona, siguiendo un flujo de preprocesamiento, procesamiento y
 * postprocesamiento.</p>
 * 
 * @param <C> tipo del contexto del comando
 * @param <R> tipo del resultado del comando
 */
@Slf4j
@Component
public abstract class CommandProcessAbstract<C, R> {
    /**
     * Resultado del procesamiento del comando.
     */
    @Setter
    @Getter
    protected R result;

    /**
     * Contexto del comando que contiene los datos necesarios para su ejecución.
     */
    @Setter
    @Getter
    protected C context;

    /**
     * Indica si el comando ha sido ejecutado.
     */
    @Setter
    @Getter
    protected boolean isExecuted = false;

    /**
     * Indica si el comando ha pasado la validación en el preprocesamiento.
     */
    @Setter
    @Getter
    protected boolean isValid = false;

    /**
     * Indica si el comando debe ejecutarse de forma asíncrona.
     */
    @Setter
    @Getter
    protected boolean isAsync = false;

    /**
     * Executor para la ejecución asíncrona de comandos.
     */
    @Setter
    private Executor executor;

    /**
     * Constructor que inyecta el executor para la ejecución asíncrona.
     * 
     * @param executor executor configurado para tareas asíncronas
     */
    @Autowired
    protected CommandProcessAbstract(@Qualifier("asyncExecutor") Executor executor) {
        this.executor = executor;
    }

    /**
     * Metodo de preprocesamiento que debe ser implementado por las clases concretas.
     * Se ejecuta antes del procesamiento principal y típicamente incluye validaciones.
     * 
     * @throws DomainException si ocurre un error de dominio
     * @throws ParseException si ocurre un error de parseo
     */
    protected abstract void preProcess() throws DomainException, ParseException;

    /**
     * Metodo principal de procesamiento que debe ser implementado por las clases concretas.
     * Contiene la lógica de negocio del comando.
     * 
     * @throws DomainException si ocurre un error de dominio
     */
    protected abstract void process() throws DomainException;

    /**
     * Metodo de postprocesamiento que debe ser implementado por las clases concretas.
     * Se ejecuta después del procesamiento principal.
     * 
     * @throws DomainException si ocurre un error de dominio
     */
    protected abstract void postProcess() throws DomainException;

    /**
     * Ejecuta el comando de forma síncrona o asíncrona según la configuración.
     * 
     * @return el resultado del procesamiento del comando
     * @throws DomainException sí ocurre un error durante la ejecución
     */
    public R execute() throws DomainException {
        if (isAsync) {
            return executeAsync();
        } else {
            return executeSync();
        }
    }

    /**
     * Ejecuta el comando de forma síncrona siguiendo el flujo:
     * preprocesamiento -> procesamiento -> postprocesamiento.
     * 
     * @return el resultado del procesamiento del comando
     * @throws DomainException sí ocurre un error durante la ejecución
     */
    private R executeSync() throws DomainException {
        try {
            preProcess();
            if (isValid) {
                process();
            }
            if (isExecuted) {
                postProcess();
            }
        } catch (DomainException | ParseException e) {
            log.error("Error in command process {}: {}", this.getClass().getSimpleName(), e.getMessage(), e);
            throw new DomainException(e.getMessage());
        }
        return getResult();
    }

    /**
     * Ejecuta el comando de forma asíncrona utilizando CompletableFuture.
     * 
     * @return el resultado del procesamiento del comando
     * @throws DomainException sí ocurre un error durante la ejecución asíncrona
     */
    private R executeAsync() throws DomainException {
        CompletableFuture<R> future = CompletableFuture.supplyAsync(() -> {
            try {
                return executeSync();
            } catch (DomainException e) {
                throw new RuntimeException(e);
            }
        }, executor);

        try {
            return future.get();
        } catch (Exception e) {
            log.error("Error in async command process {}: {}", this.getClass().getSimpleName(), e.getMessage(), e);
            throw new DomainException(e.getMessage());
        }
    }
}
