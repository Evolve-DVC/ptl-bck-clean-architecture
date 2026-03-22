package com.empresa.plantilla.infrastructure.adapters.output.services.type;

import com.empresa.plantilla.domain.adapters.output.repository.command.type.ITypeCommandRepository;
import com.empresa.plantilla.domain.adapters.output.repository.query.type.ITypeQueryRepository;
import com.empresa.plantilla.domain.adapters.output.services.type.ITypeService;
import com.empresa.plantilla.domain.model.Type;
import com.empresa.plantilla.infrastructure.adapters.output.services.GenericServiceImpl;
import com.empresa.plantilla.infrastructure.entities.type.TypeEntity;
import com.empresa.plantilla.infrastructure.mapper.type.TypeInfrastructureMapper;
import org.springframework.stereotype.Service;

/**
 * Servicio de infraestructura para Type.
 */
@Service
public class TypeServiceImpl extends GenericServiceImpl<Type, TypeEntity, Long> implements ITypeService {

    private final ITypeCommandRepository commandRepository;
    private final ITypeQueryRepository queryRepository;
    private final TypeInfrastructureMapper mapper;

    public TypeServiceImpl(ITypeCommandRepository commandRepository,
                           ITypeQueryRepository queryRepository,
                           TypeInfrastructureMapper mapper) {
        this.commandRepository = commandRepository;
        this.queryRepository = queryRepository;
        this.mapper = mapper;
    }

    @Override
    protected ITypeCommandRepository getCommandRepository() {
        return commandRepository;
    }

    @Override
    protected ITypeQueryRepository getQueryRepository() {
        return queryRepository;
    }

    @Override
    protected TypeInfrastructureMapper getMapper() {
        return mapper;
    }
}

