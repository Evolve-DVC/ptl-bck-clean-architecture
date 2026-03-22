package com.empresa.plantilla.infrastructure.adapters.output.repositories.command.typeCategory;

import com.empresa.plantilla.infrastructure.adapters.output.repositories.IGenericJpaRepository;
import com.empresa.plantilla.infrastructure.entities.typeCategory.TypeCategoryEntity;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA de escritura para TypeCategoryEntity.
 */
@Repository
public interface ITypeCategoryCommandJpaRepository extends IGenericJpaRepository<TypeCategoryEntity, Long> {
}

