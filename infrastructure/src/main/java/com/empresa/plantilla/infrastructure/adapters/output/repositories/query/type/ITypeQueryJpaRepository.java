package com.empresa.plantilla.infrastructure.adapters.output.repositories.query.type;

import com.empresa.plantilla.infrastructure.entities.type.TypeEntity;
import com.empresa.plantilla.infrastructure.adapters.output.repositories.IGenericJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA de lectura para TypeEntity.
 */
@Repository
public interface ITypeQueryJpaRepository extends IGenericJpaRepository<TypeEntity, Long> {
}

