package com.empresa.plantilla.infrastructure.dto.type;

import java.time.LocalDateTime;

/**
 * DTO record de salida para Type.
 */
public record TypeResponseDto(
        Long id,
        Long typeCategoryId,
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

