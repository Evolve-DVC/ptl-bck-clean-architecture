package com.empresa.plantilla.domain.query.typeCategory;

import com.empresa.plantilla.domain.adapters.output.services.typeCategory.ITypeCategoryService;
import com.empresa.plantilla.domain.model.TypeCategory;
import com.empresa.plantilla.domain.query.PaginadoQueryAbstract;

/**
 * Query paginado para obtener TypeCategory con filtros y ordenamiento.
 */
public class GetAllTypeCategoriesPaginadoQuery extends PaginadoQueryAbstract<TypeCategory, Long> {

    /**
     * Construye la consulta paginada de categorías.
     *
     * @param typeCategoryService servicio de dominio para categorías
     */
    public GetAllTypeCategoriesPaginadoQuery(ITypeCategoryService typeCategoryService) {
        super(typeCategoryService);
    }
}

