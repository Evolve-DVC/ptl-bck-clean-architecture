package com.empresa.plantilla.domain.command.typeCategory;

import com.empresa.plantilla.commons.exception.DomainException;
import com.empresa.plantilla.domain.adapters.output.services.typeCategory.ITypeCategoryService;
import com.empresa.plantilla.domain.command.UpdateCommandAbstract;
import com.empresa.plantilla.domain.constants.DomainErrors;
import com.empresa.plantilla.domain.model.TypeCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

/**
 * Comando para actualizar una categoría de tipo existente.
 * Aplica validaciones de integridad y unicidad antes de actualizar.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TypeCategoryUpdateCommand extends UpdateCommandAbstract<TypeCategory, Long> {

    private final ITypeCategoryService typeCategoryService;

    /**
     * Constructor para inicializar el comando con el servicio de TypeCategory.
     *
     * @param typeCategoryService servicio de dominio para categorías
     * @param executor            ejecutor del flujo del comando
     */
    @Autowired
    public TypeCategoryUpdateCommand(ITypeCategoryService typeCategoryService,
                                     @Qualifier("asyncExecutor") Executor executor) {
        super(typeCategoryService, executor);
        this.typeCategoryService = typeCategoryService;
    }

    /**
     * Valida que la categoría cumpla con las reglas de negocio para actualización.
     *
     * @throws DomainException cuando la validación de negocio falla
     */
    @Override
    protected void preProcess() throws DomainException {
        super.preProcess();

        TypeCategory context = getContext();

        // Validar que tenga ID
        if (context.getId() == null) {
            throw new DomainException(DomainErrors.ERROR_CONTEXTO_EMPTY);
        }

        // Validar que el nombre no esté vacío
        if (context.getName() == null || context.getName().isBlank()) {
            throw new DomainException(DomainErrors.ERROR_TYPE_CATEGORY_NAME_EMPTY);
        }

        // Validar que el código no esté vacío
        if (context.getCode() == null || context.getCode().isBlank()) {
            throw new DomainException(DomainErrors.ERROR_TYPE_CATEGORY_CODE_EMPTY);
        }

        // Validar que el código sea único (excepto el actual)
        TypeCategory filter = TypeCategory.builder().code(context.getCode()).build();
        var existing = this.typeCategoryService.getComboSencillo(filter);
        if (!existing.isEmpty() && !existing.getFirst().getId().equals(context.getId())) {
            throw new DomainException(DomainErrors.ERROR_TYPE_CATEGORY_CODE_DUPLICATE);
        }
    }
}
