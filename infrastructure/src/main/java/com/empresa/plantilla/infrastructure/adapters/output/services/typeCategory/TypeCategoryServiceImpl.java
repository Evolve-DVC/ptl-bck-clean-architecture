package com.empresa.plantilla.infrastructure.adapters.output.services.typeCategory;

import com.empresa.plantilla.domain.adapters.output.repository.command.typeCategory.ITypeCategoryCommandRepository;
import com.empresa.plantilla.domain.adapters.output.repository.query.typeCategory.ITypeCategoryQueryRepository;
import com.empresa.plantilla.domain.adapters.output.services.typeCategory.ITypeCategoryService;
import com.empresa.plantilla.domain.model.TypeCategory;
import com.empresa.plantilla.infrastructure.adapters.output.services.GenericServiceImpl;
import com.empresa.plantilla.infrastructure.entities.typeCategory.TypeCategoryEntity;
import com.empresa.plantilla.infrastructure.mapper.typeCategory.TypeCategoryInfrastructureMapper;
import org.springframework.stereotype.Service;

/**
 * Servicio de infraestructura para TypeCategory.
 */
@Service
public class TypeCategoryServiceImpl extends GenericServiceImpl<TypeCategory, TypeCategoryEntity, Long> implements ITypeCategoryService {

    private final ITypeCategoryCommandRepository commandRepository;
    private final ITypeCategoryQueryRepository queryRepository;
    private final TypeCategoryInfrastructureMapper mapper;

    public TypeCategoryServiceImpl(ITypeCategoryCommandRepository commandRepository,
                                   ITypeCategoryQueryRepository queryRepository,
                                   TypeCategoryInfrastructureMapper mapper) {
        this.commandRepository = commandRepository;
        this.queryRepository = queryRepository;
        this.mapper = mapper;
    }

    @Override
    protected ITypeCategoryCommandRepository getCommandRepository() {
        return commandRepository;
    }

    @Override
    protected ITypeCategoryQueryRepository getQueryRepository() {
        return queryRepository;
    }

    @Override
    protected TypeCategoryInfrastructureMapper getMapper() {
        return mapper;
    }
}

