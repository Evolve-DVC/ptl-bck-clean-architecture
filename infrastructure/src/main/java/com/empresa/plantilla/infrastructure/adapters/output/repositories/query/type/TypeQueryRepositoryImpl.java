package com.empresa.plantilla.infrastructure.adapters.output.repositories.query.type;

import com.empresa.plantilla.domain.adapters.output.repository.query.type.ITypeQueryRepository;
import com.empresa.plantilla.domain.model.Type;
import com.empresa.plantilla.infrastructure.entities.type.TypeEntity;
import com.empresa.plantilla.infrastructure.mapper.type.TypeInfrastructureMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de lectura de Type para dominio.
 */
@Repository
public class TypeQueryRepositoryImpl implements ITypeQueryRepository {

    private final ITypeQueryJpaRepository jpaRepository;
    private final TypeInfrastructureMapper mapper;

    public TypeQueryRepositoryImpl(ITypeQueryJpaRepository jpaRepository,
                                   TypeInfrastructureMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Page<Type> findAll(Example<Type> example, Pageable pageable) {
        TypeEntity probe = mapper.modelToEntity(example.getProbe());
        Example<TypeEntity> entityExample = Example.of(probe, example.getMatcher());
        Page<TypeEntity> page = jpaRepository.findAll(entityExample, pageable);
        List<Type> content = mapper.toModelList(page.getContent());
        return new PageImpl<>(content, pageable, page.getTotalElements());
    }

    @Override
    public List<Type> findAll(Example<Type> example) {
        TypeEntity probe = mapper.modelToEntity(example.getProbe());
        Example<TypeEntity> entityExample = Example.of(probe, example.getMatcher());
        return mapper.toModelList(jpaRepository.findAll(entityExample));
    }

    @Override
    public Optional<Type> findById(Long id) {
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

