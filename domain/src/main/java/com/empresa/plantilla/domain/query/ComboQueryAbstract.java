package com.empresa.plantilla.domain.query;

import com.empresa.plantilla.commons.exception.DomainException;
import com.empresa.plantilla.commons.query.QueryAbstract;
import com.empresa.plantilla.commons.services.IGenericService;
import com.empresa.plantilla.domain.constants.DomainErrors;

import java.util.List;

/**
 * Plantilla base para consultas tipo combo (listado completo).
 *
 * @param <T> tipo del contexto de filtro y del elemento de salida
 * @param <K> tipo de identificador del modelo
 */
public abstract class ComboQueryAbstract<T, K> extends QueryAbstract<T, List<T>> {
    private final IGenericService<T, K> service;

    /**
     * Construye la query de combo.
     *
     * @param service servicio de dominio para consultas de listado
     */
    protected ComboQueryAbstract(IGenericService<T, K> service) {
        this.service = service;
    }

    /**
     * Valida que el contexto de filtro sea obligatorio.
     *
     * @param context filtro de entrada de la consulta
     * @throws DomainException cuando el contexto es nulo
     */
    @Override
    public void preProcess(T context) {
        if (context == null) {
            throw new DomainException(DomainErrors.ERROR_CONTEXTO_EMPTY);
        }
    }

    /**
     * Ejecuta la consulta de listado completo.
     *
     * @param context filtro de entrada de la consulta
     * @return listado completo filtrado y ordenado
     */
    @Override
    public List<T> process(T context) {
        return this.service.getComboSencillo(context);
    }
}
