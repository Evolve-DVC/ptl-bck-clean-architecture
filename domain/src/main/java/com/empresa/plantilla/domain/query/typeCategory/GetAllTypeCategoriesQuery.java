package com.empresa.plantilla.domain.query.typeCategory;

import com.empresa.plantilla.domain.adapters.output.services.typeCategory.ITypeCategoryService;
import com.empresa.plantilla.domain.model.TypeCategory;
import com.empresa.plantilla.domain.query.ComboQueryAbstract;

/**
 * Query para obtener todas las categorias de tipos aplicando filtros y ordenamiento.
 * Retorna el listado completo (no paginado).
 */
public class GetAllTypeCategoriesQuery extends ComboQueryAbstract<TypeCategory, Long> {

    /**
     * Construye la consulta combo de categorías.
     *
     * @param typeCategoryService servicio de dominio para categorías
     */
    public GetAllTypeCategoriesQuery(ITypeCategoryService typeCategoryService) {
        super(typeCategoryService);
    }
}

