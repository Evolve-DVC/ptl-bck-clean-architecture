package com.empresa.plantilla.infrastructure.dto.typeCategory;

/**
 * DTO record de filtros para consultas de TypeCategory.
 */
public record TypeCategoryFilterDto(
        Long id,
        String name,
        String code,
        Boolean active
) {
}

