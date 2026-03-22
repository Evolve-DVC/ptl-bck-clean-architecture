package com.empresa.plantilla.infrastructure.adapters.output.repositories.query.typeCategory;

import com.empresa.plantilla.domain.adapters.output.repository.query.typeCategory.ITypeCategoryQueryRepository;
import com.empresa.plantilla.domain.model.TypeCategory;
import com.empresa.plantilla.infrastructure.entities.typeCategory.TypeCategoryEntity;
import com.empresa.plantilla.infrastructure.mapper.typeCategory.TypeCategoryInfrastructureMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de lectura de TypeCategory para dominio.
 */
@Repository
public class TypeCategoryQueryRepositoryImpl implements ITypeCategoryQueryRepository {

    private final ITypeCategoryQueryJpaRepository jpaRepository;
    private final TypeCategoryInfrastructureMapper mapper;

    public TypeCategoryQueryRepositoryImpl(ITypeCategoryQueryJpaRepository jpaRepository,
                                           TypeCategoryInfrastructureMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Page<TypeCategory> findAll(Example<TypeCategory> example, Pageable pageable) {
        TypeCategoryEntity probe = mapper.modelToEntity(example.getProbe());
        Example<TypeCategoryEntity> entityExample = Example.of(probe, example.getMatcher());
        Page<TypeCategoryEntity> page = jpaRepository.findAll(entityExample, pageable);
        List<TypeCategory> content = mapper.toModelList(page.getContent());
        return new PageImpl<>(content, pageable, page.getTotalElements());
    }

    @Override
    public List<TypeCategory> findAll(Example<TypeCategory> example) {
        TypeCategoryEntity probe = mapper.modelToEntity(example.getProbe());
        Example<TypeCategoryEntity> entityExample = Example.of(probe, example.getMatcher());
        return mapper.toModelList(jpaRepository.findAll(entityExample));
    }

    @Override
    public Optional<TypeCategory> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::entityToModel);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public Long getNextValSequence() {
        return jpaRepository.getNextValSequence();
    }
}

