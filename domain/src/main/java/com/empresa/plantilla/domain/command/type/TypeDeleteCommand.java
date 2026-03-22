package com.empresa.plantilla.domain.command.type;

import com.empresa.plantilla.commons.exception.DomainException;
import com.empresa.plantilla.domain.adapters.output.services.type.ITypeService;
import com.empresa.plantilla.domain.command.DeleteCommandAbstract;
import com.empresa.plantilla.domain.constants.DomainErrors;
import com.empresa.plantilla.domain.model.Type;

import java.util.concurrent.Executor;

/**
 * Comando para eliminar un tipo.
 * Valida que el contexto incluya identificador antes de ejecutar el borrado.
 */
public class TypeDeleteCommand extends DeleteCommandAbstract<Type, Long> {

    /**
     * Constructor para inicializar el comando con el servicio de Type.
     *
     * @param typeService servicio de dominio para tipos
     * @param executor    ejecutor del flujo del comando
     */
    public TypeDeleteCommand(ITypeService typeService, Executor executor) {
        super(typeService, executor);
    }

    /**
     * Valida que el tipo tenga ID antes de eliminarlo.
     *
     * @throws DomainException cuando la validación de negocio falla
     */
    @Override
    protected void preProcess() throws DomainException {
        super.preProcess();

        Type context = getContext();

        // Validar que tenga ID
        if (context.getId() == null) {
            throw new DomainException(DomainErrors.ERROR_CONTEXTO_EMPTY);
        }
    }
}
