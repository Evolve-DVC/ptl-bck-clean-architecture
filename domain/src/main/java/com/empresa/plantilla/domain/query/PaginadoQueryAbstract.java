package com.empresa.plantilla.domain.query;

import com.empresa.plantilla.commons.dto.pageable.PageContext;
import com.empresa.plantilla.commons.exception.DomainException;
import com.empresa.plantilla.commons.query.QueryAbstract;
import com.empresa.plantilla.commons.services.IGenericService;
import com.empresa.plantilla.commons.services.pageable.IPageableResult;
import com.empresa.plantilla.domain.constants.DomainErrors;

/**
 * Plantilla base para consultas paginadas.
 *
 * @param <M> tipo de modelo consultado
 * @param <K> tipo de identificador del modelo
 */
public abstract class PaginadoQueryAbstract<M, K> extends QueryAbstract<PageContext<M>, IPageableResult<M>> {
    private final IGenericService<M, K> service;

    /**
     * Construye la query paginada.
     *
     * @param service servicio de dominio para consultas paginadas
     */
    protected PaginadoQueryAbstract(IGenericService<M, K> service) {
        this.service = service;
    }

    /**
     * Valida que el contexto paginado sea obligatorio.
     *
     * @param context contexto de paginación y filtros
     * @throws DomainException cuando el contexto es nulo
     */
    @Override
    public void preProcess(PageContext<M> context) {
        if (context == null) {
            throw new DomainException(DomainErrors.ERROR_CONTEXTO_EMPTY);
        }
    }

    /**
     * Ejecuta la consulta paginada en el servicio.
     *
     * @param context contexto de paginación y filtros
     * @return resultado paginado con metadatos de página
     */
    @Override
    public IPageableResult<M> process(PageContext<M> context) {
        return this.service.getComboGrande(context.getData(), context.getPageNumber(), context.getPageSize(), context.getSortBy(), context.getSortDir(), context.getFilterType());
    }
}
