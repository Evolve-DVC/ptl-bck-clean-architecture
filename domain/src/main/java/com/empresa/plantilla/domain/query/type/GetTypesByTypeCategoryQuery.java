package com.empresa.plantilla.domain.query.type;

import com.empresa.plantilla.domain.adapters.output.services.type.ITypeService;
import com.empresa.plantilla.domain.model.Type;
import com.empresa.plantilla.domain.query.ComboQueryAbstract;

/**
 * Query para obtener todos los tipos por categoria aplicando filtros y ordenamiento.
 * Retorna el listado completo (no paginado).
 */
public class GetTypesByTypeCategoryQuery extends ComboQueryAbstract<Type, Long> {

    /**
     * Construye la consulta combo de tipos por categoría.
     *
     * @param typeService servicio de dominio para tipos
     */
    public GetTypesByTypeCategoryQuery(ITypeService typeService) {
        super(typeService);
    }
}

