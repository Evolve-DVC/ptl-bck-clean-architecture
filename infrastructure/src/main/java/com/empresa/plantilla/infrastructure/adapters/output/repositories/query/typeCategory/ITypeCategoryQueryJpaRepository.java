package com.empresa.plantilla.infrastructure.adapters.output.repositories.query.typeCategory;

import com.empresa.plantilla.infrastructure.entities.typeCategory.TypeCategoryEntity;
import com.empresa.plantilla.infrastructure.adapters.output.repositories.IGenericJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA de lectura para TypeCategoryEntity.
 */
@Repository
public interface ITypeCategoryQueryJpaRepository extends IGenericJpaRepository<TypeCategoryEntity, Long> {
}

