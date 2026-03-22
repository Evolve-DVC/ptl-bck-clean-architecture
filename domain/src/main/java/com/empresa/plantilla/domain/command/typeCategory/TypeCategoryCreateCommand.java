package com.empresa.plantilla.domain.command.typeCategory;

import com.empresa.plantilla.commons.exception.DomainException;
import com.empresa.plantilla.domain.adapters.output.services.typeCategory.ITypeCategoryService;
import com.empresa.plantilla.domain.command.CreateCommandAbstract;
import com.empresa.plantilla.domain.constants.DomainErrors;
import com.empresa.plantilla.domain.model.TypeCategory;

import java.util.concurrent.Executor;

/**
 * Comando para crear una nueva categoría de tipo.
 * Aplica validaciones de obligatoriedad y unicidad antes de persistir.
 */
public class TypeCategoryCreateCommand extends CreateCommandAbstract<TypeCategory, Long> {

    private final ITypeCategoryService typeCategoryService;

    /**
     * Constructor para inicializar el comando con el servicio de TypeCategory.
     *
     * @param typeCategoryService servicio de dominio para categorías
     * @param executor            ejecutor del flujo del comando
     */
    public TypeCategoryCreateCommand(ITypeCategoryService typeCategoryService, Executor executor) {
        super(typeCategoryService, executor);
        this.typeCategoryService = typeCategoryService;
    }

    /**
     * Valida que la categoría cumpla con las reglas de negocio.
     *
     * @throws DomainException cuando la validación de negocio falla
     */
    @Override
    protected void preProcess() throws DomainException {
        super.preProcess();

        TypeCategory context = getContext();

        // Validar que el nombre no esté vacío
        if (context.getName() == null || context.getName().isBlank()) {
            throw new DomainException(DomainErrors.ERROR_TYPE_CATEGORY_NAME_EMPTY);
        }

        // Validar que el código no esté vacío
        if (context.getCode() == null || context.getCode().isBlank()) {
            throw new DomainException(DomainErrors.ERROR_TYPE_CATEGORY_CODE_EMPTY);
        }

        // Validar que el código sea único
        TypeCategory filter = TypeCategory.builder().code(context.getCode()).build();
        var existing = this.typeCategoryService.getComboSencillo(filter);
        if (!existing.isEmpty()) {
            throw new DomainException(DomainErrors.ERROR_TYPE_CATEGORY_CODE_DUPLICATE);
        }
    }
}
