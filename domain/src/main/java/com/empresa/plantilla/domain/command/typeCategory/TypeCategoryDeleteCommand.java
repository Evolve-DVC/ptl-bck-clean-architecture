package com.empresa.plantilla.domain.command.typeCategory;

import com.empresa.plantilla.commons.exception.DomainException;
import com.empresa.plantilla.domain.adapters.output.services.type.ITypeService;
import com.empresa.plantilla.domain.adapters.output.services.typeCategory.ITypeCategoryService;
import com.empresa.plantilla.domain.command.DeleteCommandAbstract;
import com.empresa.plantilla.domain.constants.DomainErrors;
import com.empresa.plantilla.domain.model.Type;
import com.empresa.plantilla.domain.model.TypeCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

/**
 * Comando para eliminar una categoría de tipo.
 * Exige que no existan tipos asociados antes de permitir la eliminación.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TypeCategoryDeleteCommand extends DeleteCommandAbstract<TypeCategory, Long> {
    private final ITypeService typeService;

    /**
     * Constructor para inicializar el comando con los servicios requeridos.
     *
     * @param typeCategoryService servicio de dominio para categorías
     * @param typeService         servicio de dominio para tipos
     * @param executor            ejecutor del flujo del comando
     */
    @Autowired
    public TypeCategoryDeleteCommand(
            ITypeCategoryService typeCategoryService,
            ITypeService typeService,
            @Qualifier("asyncExecutor") Executor executor) {
        super(typeCategoryService, executor);
        this.typeService = typeService;
    }

    /**
     * Valida que la categoría no tenga tipos asociados antes de eliminarla.
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

        // Validar que NO existan Types asociados
        Type filter = Type.builder().typeCategoryId(context.getId()).build();
        var associatedTypes = this.typeService.getComboSencillo(filter);
        if (!associatedTypes.isEmpty()) {
            throw new DomainException(DomainErrors.ERROR_TYPE_CATEGORY_HAS_TYPES);
        }
    }
}
