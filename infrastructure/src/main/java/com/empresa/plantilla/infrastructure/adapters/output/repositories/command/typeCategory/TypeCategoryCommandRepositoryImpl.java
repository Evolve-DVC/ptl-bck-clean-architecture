package com.empresa.plantilla.infrastructure.adapters.output.repositories.command.typeCategory;

import com.empresa.plantilla.domain.adapters.output.repository.command.typeCategory.ITypeCategoryCommandRepository;
import com.empresa.plantilla.domain.model.TypeCategory;
import com.empresa.plantilla.infrastructure.mapper.typeCategory.TypeCategoryInfrastructureMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Adaptador de escritura de TypeCategory para dominio.
 */
@Repository
@Transactional
public class TypeCategoryCommandRepositoryImpl implements ITypeCategoryCommandRepository {

    private final ITypeCategoryCommandJpaRepository jpaRepository;
    private final TypeCategoryInfrastructureMapper mapper;

    public TypeCategoryCommandRepositoryImpl(ITypeCategoryCommandJpaRepository jpaRepository,
                                             TypeCategoryInfrastructureMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public TypeCategory save(TypeCategory entity) {
        return mapper.entityToModel(jpaRepository.save(mapper.modelToEntity(entity)));
    }

    @Override
    public Iterable<TypeCategory> saveAll(Iterable<TypeCategory> entities) {
        return mapper.toModelList(jpaRepository.saveAll(mapper.toEntityList(entities)));
    }

    @Override
    public TypeCategory update(TypeCategory entity) {
        return mapper.entityToModel(jpaRepository.save(mapper.modelToEntity(entity)));
    }

    @Override
    public Iterable<TypeCategory> updateAll(Iterable<TypeCategory> entities) {
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

