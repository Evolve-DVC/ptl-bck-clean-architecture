package com.empresa.plantilla.domain.query.type;

import com.empresa.plantilla.domain.adapters.output.services.type.ITypeService;
import com.empresa.plantilla.domain.model.Type;
import com.empresa.plantilla.domain.query.ModelQueryAbstract;

/**
 * Query para obtener un tipo por su identificador.
 */
public class GetTypeByIdQuery extends ModelQueryAbstract<Type, Long> {

    /**
     * Construye la consulta de tipo por id.
     *
     * @param typeService servicio de dominio para tipos
     */
    public GetTypeByIdQuery(ITypeService typeService) {
        super(typeService);
    }
}

