package com.empresa.plantilla.infrastructure.mapper.typeCategory;

import com.empresa.plantilla.domain.model.TypeCategory;
import com.empresa.plantilla.infrastructure.entities.typeCategory.TypeCategoryEntity;
import com.empresa.plantilla.infrastructure.dto.typeCategory.TypeCategoryCreateRequestDto;
import com.empresa.plantilla.infrastructure.dto.typeCategory.TypeCategoryFilterDto;
import com.empresa.plantilla.infrastructure.dto.typeCategory.TypeCategoryResponseDto;
import com.empresa.plantilla.infrastructure.dto.typeCategory.TypeCategoryUpdateRequestDto;
import com.empresa.plantilla.infrastructure.mapper.IGenericMapper;
import org.mapstruct.Mapper;

import java.util.List;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper de infraestructura para TypeCategory usando MapStruct.
 * MapStruct genera automáticamente la implementación de esta interfaz.
 */
@Mapper(componentModel = "spring")
public interface TypeCategoryInfrastructureMapper extends IGenericMapper<TypeCategory, TypeCategoryEntity, Long> {

    @Override
    @Mapping(target = "types", ignore = true)
    TypeCategoryEntity modelToEntity(TypeCategory model);

    /**
     * Convierte TypeCategoryCreateRequestDto a TypeCategory (modelo de dominio).
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    TypeCategory fromCreateDto(TypeCategoryCreateRequestDto dto);

    /**
     * Convierte TypeCategoryUpdateRequestDto a TypeCategory (modelo de dominio).
     */
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    TypeCategory fromUpdateDto(TypeCategoryUpdateRequestDto dto);

    /**
     * Convierte TypeCategoryFilterDto a TypeCategory (modelo de dominio).
     */
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    TypeCategory fromFilterDto(TypeCategoryFilterDto dto);

    /**
     * Convierte TypeCategory a TypeCategoryResponseDto.
     */
    TypeCategoryResponseDto toResponseDto(TypeCategory model);

    /**
     * Convierte lista de TypeCategory a lista de TypeCategoryResponseDto.
     */
    List<TypeCategoryResponseDto> toResponseDtoList(List<TypeCategory> models);

    /**
     * Extrae la clave (ID) del modelo.
     */
    @Named("extractKey")
    default Long toKey(TypeCategory model) {
        return model != null ? model.getId() : null;
    }
}

