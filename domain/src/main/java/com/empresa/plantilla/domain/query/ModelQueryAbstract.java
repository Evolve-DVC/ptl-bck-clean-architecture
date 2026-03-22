package com.empresa.plantilla.domain.query;

import com.empresa.plantilla.commons.exception.DomainException;
import com.empresa.plantilla.commons.query.QueryAbstract;
import com.empresa.plantilla.commons.services.IGenericService;
import com.empresa.plantilla.domain.constants.DomainErrors;

/**
 * Plantilla base para consultas de un único modelo por identificador.
 *
 * @param <T> tipo del modelo consultado
 * @param <K> tipo del identificador de consulta
 */
public abstract class ModelQueryAbstract<T, K> extends QueryAbstract<K, T> {
    protected final IGenericService<T, K> service;

    /**
     * Construye la query con el servicio de lectura.
     *
     * @param service servicio de dominio para consultar elementos
     */
    protected ModelQueryAbstract(IGenericService<T, K> service) {
        this.service = service;
    }

    /**
     * Valida que el identificador de consulta sea obligatorio.
     *
     * @param id identificador recibido por la consulta
     * @throws DomainException cuando el identificador es nulo
     */
    @Override
    public void preProcess(K id) {
        if (id == null) {
            throw new DomainException(DomainErrors.ERROR_CONTEXTO_EMPTY);
        }
    }

    /**
     * Ejecuta la consulta de un elemento por identificador.
     *
     * @param id identificador del elemento
     * @return modelo encontrado o {@code null} cuando no existe
     */
    @Override
    public T process(K id) {
        return this.service.getElement(id);
    }
}
