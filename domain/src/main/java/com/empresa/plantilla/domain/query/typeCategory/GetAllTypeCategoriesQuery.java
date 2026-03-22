package com.empresa.plantilla.domain.query.typeCategory;

import com.empresa.plantilla.domain.adapters.output.services.typeCategory.ITypeCategoryService;
import com.empresa.plantilla.domain.model.TypeCategory;
import com.empresa.plantilla.domain.query.ComboQueryAbstract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Query para obtener todas las categorias de tipos aplicando filtros y ordenamiento.
 * Retorna el listado completo (no paginado).
 */
@Component
public class GetAllTypeCategoriesQuery extends ComboQueryAbstract<TypeCategory, Long> {

    /**
     * Construye la consulta combo de categorías.
     *
     * @param typeCategoryService servicio de dominio para categorías
     */
    @Autowired
    public GetAllTypeCategoriesQuery(ITypeCategoryService typeCategoryService) {
        super(typeCategoryService);
    }
}

