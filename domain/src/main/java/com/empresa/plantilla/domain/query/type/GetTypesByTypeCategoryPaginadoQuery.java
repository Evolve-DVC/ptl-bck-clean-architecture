package com.empresa.plantilla.domain.query.type;

import com.empresa.plantilla.commons.dto.pageable.PageContext;
import com.empresa.plantilla.commons.exception.DomainException;
import com.empresa.plantilla.domain.adapters.output.services.type.ITypeService;
import com.empresa.plantilla.domain.constants.DomainErrors;
import com.empresa.plantilla.domain.model.Type;
import com.empresa.plantilla.domain.query.PaginadoQueryAbstract;
import org.springframework.stereotype.Component;

/**
 * Query paginado de Type filtrado por categoria.
 */
@Component
public class GetTypesByTypeCategoryPaginadoQuery extends PaginadoQueryAbstract<Type, Long> {

    /**
     * Construye la consulta paginada de tipos por categoría.
     *
     * @param typeService servicio de dominio para tipos
     */
    public GetTypesByTypeCategoryPaginadoQuery(ITypeService typeService) {
        super(typeService);
    }

    /**
     * Exige que el filtro incluya una categoría para paginar tipos.
     *
     * @param context contexto de consulta paginada
     * @throws DomainException cuando no se informa la categoría
     */
    @Override
    public void preProcess(PageContext<Type> context) {
        super.preProcess(context);
        if (context.getData() == null || context.getData().getTypeCategoryId() == null) {
            throw new DomainException(DomainErrors.ERROR_TYPE_CATEGORY_ID_EMPTY);
        }
    }
}

