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
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String DEFAULT_SORT_BY = "id";
    private static final String DEFAULT_SORT_DIR = "asc";
    private static final String DEFAULT_FILTER_TYPE = "CONTAINING";

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
        if (context.getPageNumber() == null) {
            context.setPageNumber(DEFAULT_PAGE_NUMBER);
        }
        if (context.getPageSize() == null) {
            context.setPageSize(DEFAULT_PAGE_SIZE);
        }
        if (context.getSortBy() == null || context.getSortBy().isBlank()) {
            context.setSortBy(DEFAULT_SORT_BY);
        }
        if (context.getSortDir() == null || context.getSortDir().isBlank()) {
            context.setSortDir(DEFAULT_SORT_DIR);
        }
        if (context.getFilterType() == null || context.getFilterType().isBlank()) {
            context.setFilterType(DEFAULT_FILTER_TYPE);
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
