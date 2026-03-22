package com.empresa.plantilla.domain.command.type;

import com.empresa.plantilla.commons.exception.DomainException;
import com.empresa.plantilla.domain.adapters.output.services.type.ITypeService;
import com.empresa.plantilla.domain.adapters.output.services.typeCategory.ITypeCategoryService;
import com.empresa.plantilla.domain.command.UpdateCommandAbstract;
import com.empresa.plantilla.domain.constants.DomainErrors;
import com.empresa.plantilla.domain.model.Type;
import com.empresa.plantilla.domain.model.TypeCategory;

import java.util.concurrent.Executor;

/**
 * Comando para actualizar un tipo existente.
 * Aplica validaciones de integridad y unicidad previas a la actualización.
 */
public class TypeUpdateCommand extends UpdateCommandAbstract<Type, Long> {

    private final ITypeService typeService;
    private final ITypeCategoryService typeCategoryService;

    /**
     * Constructor para inicializar el comando con los servicios requeridos.
     *
     * @param typeService         servicio de dominio para tipos
     * @param typeCategoryService servicio de dominio para categorías
     * @param executor            ejecutor del flujo del comando
     */
    public TypeUpdateCommand(
            ITypeService typeService,
            ITypeCategoryService typeCategoryService,
            Executor executor) {
        super(typeService, executor);
        this.typeService = typeService;
        this.typeCategoryService = typeCategoryService;
    }

    /**
     * Valida que el tipo cumpla con las reglas de negocio para actualización.
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

        // Validar que la categoría ID no sea nula
        if (context.getTypeCategoryId() == null) {
            throw new DomainException(DomainErrors.ERROR_TYPE_CATEGORY_ID_EMPTY);
        }

        // Validar que la categoría existe
        TypeCategory category = this.typeCategoryService.getElement(context.getTypeCategoryId());
        if (category == null) {
            throw new DomainException(DomainErrors.ERROR_TYPE_CATEGORY_NOT_FOUND);
        }

        // Validar que el nombre no esté vacío
        if (context.getName() == null || context.getName().isBlank()) {
            throw new DomainException(DomainErrors.ERROR_TYPE_NAME_EMPTY);
        }

        // Validar que el código no esté vacío
        if (context.getCode() == null || context.getCode().isBlank()) {
            throw new DomainException(DomainErrors.ERROR_TYPE_CODE_EMPTY);
        }

        // Validar que el código sea único dentro de la categoría (excepto el actual)
        Type filter = Type.builder()
                .typeCategoryId(context.getTypeCategoryId())
                .code(context.getCode())
                .build();
        var existing = this.typeService.getComboSencillo(filter);
        if (!existing.isEmpty() && !existing.getFirst().getId().equals(context.getId())) {
            throw new DomainException(DomainErrors.ERROR_TYPE_CODE_DUPLICATE);
        }
    }
}
