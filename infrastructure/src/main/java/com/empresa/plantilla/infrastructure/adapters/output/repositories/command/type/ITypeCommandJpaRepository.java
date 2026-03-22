package com.empresa.plantilla.infrastructure.adapters.output.repositories.command.type;

import com.empresa.plantilla.infrastructure.adapters.output.repositories.IGenericJpaRepository;
import com.empresa.plantilla.infrastructure.entities.type.TypeEntity;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA de escritura para TypeEntity.
 */
@Repository
public interface ITypeCommandJpaRepository extends IGenericJpaRepository<TypeEntity, Long> {
}

