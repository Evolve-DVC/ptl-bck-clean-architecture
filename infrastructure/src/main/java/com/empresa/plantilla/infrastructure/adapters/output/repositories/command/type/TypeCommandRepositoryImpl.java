package com.empresa.plantilla.infrastructure.adapters.output.repositories.command.type;

import com.empresa.plantilla.domain.adapters.output.repository.command.type.ITypeCommandRepository;
import com.empresa.plantilla.domain.model.Type;
import com.empresa.plantilla.infrastructure.mapper.type.TypeInfrastructureMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Adaptador de escritura de Type para dominio.
 */
@Repository
@Transactional
public class TypeCommandRepositoryImpl implements ITypeCommandRepository {

    private final ITypeCommandJpaRepository jpaRepository;
    private final TypeInfrastructureMapper mapper;

    public TypeCommandRepositoryImpl(ITypeCommandJpaRepository jpaRepository,
                                     TypeInfrastructureMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Type save(Type entity) {
        return mapper.entityToModel(jpaRepository.save(mapper.modelToEntity(entity)));
    }

    @Override
    public Iterable<Type> saveAll(Iterable<Type> entities) {
        return mapper.toModelList(jpaRepository.saveAll(mapper.toEntityList(entities)));
    }

    @Override
    public Type update(Type entity) {
        return mapper.entityToModel(jpaRepository.save(mapper.modelToEntity(entity)));
    }

    @Override
    public Iterable<Type> updateAll(Iterable<Type> entities) {
        if (entities == null) {
            return List.of();
        }
        return mapper.toModelList(jpaRepository.saveAll(mapper.toEntityList(entities)));
    }

    @Override
    public void delete(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void deleteAll(Iterable<Long> ids) {
        if (ids == null) {
            return;
        }
        List<Long> idList;
        if (ids instanceof Collection) {
            idList = new ArrayList<>((Collection<Long>) ids);
        } else {
            idList = new ArrayList<>();
            for (Long id : ids) {
                idList.add(id);
            }
        }
        if (idList.isEmpty()) {
            return;
        }
        jpaRepository.deleteAllByIdInBatch(idList);
    }
}

