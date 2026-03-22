package com.empresa.plantilla.domain.query.typeCategory;

import com.empresa.plantilla.domain.adapters.output.services.typeCategory.ITypeCategoryService;
import com.empresa.plantilla.domain.model.TypeCategory;
import com.empresa.plantilla.domain.query.ModelQueryAbstract;

/**
 * Query para obtener una categoria de tipo por su identificador.
 */
public class GetTypeCategoryByIdQuery extends ModelQueryAbstract<TypeCategory, Long> {

    /**
     * Construye la consulta de categoría por id.
     *
     * @param typeCategoryService servicio de dominio para categorías
     */
    public GetTypeCategoryByIdQuery(ITypeCategoryService typeCategoryService) {
        super(typeCategoryService);
    }
}

