package com.empresa.plantilla.domain.command;

import com.empresa.plantilla.commons.command.CommandProcessAbstract;
import com.empresa.plantilla.commons.exception.DomainException;
import com.empresa.plantilla.commons.services.IGenericService;
import com.empresa.plantilla.domain.constants.DomainErrors;

import java.util.concurrent.Executor;

/**
 * Plantilla base para comandos de eliminación en dominio.
 *
 * @param <C> tipo de contexto de entrada y salida
 * @param <K> tipo de identificador de la entidad
 */
public abstract class DeleteCommandAbstract<C, K> extends CommandProcessAbstract<C, C> {
    private final IGenericService<C, K> service;

    /**
     * Crea la base del comando de eliminación.
     *
     * @param service  servicio de persistencia del dominio
     * @param executor ejecutor para el flujo de procesamiento
     */
    protected DeleteCommandAbstract(IGenericService<C, K> service, Executor executor) {
        super(executor);
        this.service = service;
    }

    /**
     * Valida el contexto obligatorio antes de procesar.
     *
     * @throws DomainException cuando el contexto es nulo
     */
    @Override
    protected void preProcess() throws DomainException {
        if (getContext() == null) {
            throw new DomainException(DomainErrors.ERROR_CONTEXTO_EMPTY);
        }
        setValid(true);
    }

    /**
     * Ejecuta la operación de eliminación en el servicio.
     *
     * @throws DomainException cuando ocurre un error de dominio
     */
    @Override
    protected void process() throws DomainException {
        setResult(this.service.delete(getContext()));
        setExecuted(true);
    }

    /**
     * Hook de postproceso sin comportamiento por defecto.
     *
     * @throws DomainException cuando ocurre un error de dominio
     */
    @Override
    protected void postProcess() throws DomainException {
        // No hay postProcess aquí
    }
}