package com.empresa.plantilla.infrastructure.dto.typeCategory;

import java.time.LocalDateTime;

/**
 * DTO record de salida para TypeCategory.
 */
public record TypeCategoryResponseDto(
        Long id,
        String name,
        String code,
        String description,
        Boolean active,
        String createBy,
        LocalDateTime createDate,
        String updateBy,
        LocalDateTime updateDate
) {
}

