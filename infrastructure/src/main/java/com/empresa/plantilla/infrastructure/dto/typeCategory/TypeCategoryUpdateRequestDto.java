package com.empresa.plantilla.infrastructure.dto.typeCategory;

import com.empresa.plantilla.infrastructure.constants.InfrastructureErrors;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO record de entrada para actualizar TypeCategory.
 */
public record TypeCategoryUpdateRequestDto(
        @NotNull(message = "{" + InfrastructureErrors.VALIDATION_CATEGORY_ID_REQUIRED + "}")
        Long id,

        @NotBlank(message = "{" + InfrastructureErrors.VALIDATION_CATEGORY_NAME_REQUIRED + "}")
        @Size(min = 1, max = 150, message = "{" + InfrastructureErrors.VALIDATION_CATEGORY_NAME_SIZE + "}")
        String name,

        @NotBlank(message = "{" + InfrastructureErrors.VALIDATION_CATEGORY_CODE_REQUIRED + "}")
        @Size(min = 1, max = 80, message = "{" + InfrastructureErrors.VALIDATION_CATEGORY_CODE_SIZE + "}")
        String code,

        @Size(max = 500, message = "{" + InfrastructureErrors.VALIDATION_CATEGORY_DESCRIPTION_SIZE + "}")
        String description,

        Boolean active
) {
}

