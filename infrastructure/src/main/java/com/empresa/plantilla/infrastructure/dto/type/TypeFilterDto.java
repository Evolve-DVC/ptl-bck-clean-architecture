package com.empresa.plantilla.infrastructure.dto.type;

/**
 * DTO record de filtros para consultas de Type.
 */
public record TypeFilterDto(
        Long id,
        Long typeCategoryId,
        String name,
        String code,
        Boolean active
) {
}

