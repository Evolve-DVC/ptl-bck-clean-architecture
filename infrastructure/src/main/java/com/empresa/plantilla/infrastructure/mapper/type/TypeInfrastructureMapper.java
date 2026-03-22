package com.empresa.plantilla.infrastructure.mapper.type;

import com.empresa.plantilla.domain.model.Type;
import com.empresa.plantilla.infrastructure.entities.type.TypeEntity;
import com.empresa.plantilla.infrastructure.dto.type.TypeCreateRequestDto;
import com.empresa.plantilla.infrastructure.dto.type.TypeFilterDto;
import com.empresa.plantilla.infrastructure.dto.type.TypeResponseDto;
import com.empresa.plantilla.infrastructure.dto.type.TypeUpdateRequestDto;
import com.empresa.plantilla.infrastructure.mapper.IGenericMapper;
import org.mapstruct.Mapper;

import java.util.List;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper de infraestructura para Type usando MapStruct.
 * MapStruct genera automáticamente la implementación de esta interfaz.
 */
@Mapper(componentModel = "spring")
public interface TypeInfrastructureMapper extends IGenericMapper<Type, TypeEntity, Long> {

    @Override
    @Mapping(target = "typeCategory", ignore = true)
    TypeEntity modelToEntity(Type model);

    /**
     * Convierte TypeCreateRequestDto a Type (modelo de dominio).
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Type fromCreateDto(TypeCreateRequestDto dto);

    /**
     * Convierte TypeUpdateRequestDto a Type (modelo de dominio).
     */
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Type fromUpdateDto(TypeUpdateRequestDto dto);

    /**
     * Convierte TypeFilterDto a Type (modelo de dominio).
     */
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Type fromFilterDto(TypeFilterDto dto);

    /**
     * Convierte Type a TypeResponseDto.
     */
    TypeResponseDto toResponseDto(Type model);

    /**
     * Convierte lista de Type a lista de TypeResponseDto.
     */
    List<TypeResponseDto> toResponseDtoList(List<Type> models);

    /**
     * Extrae la clave (ID) del modelo.
     */
    @Named("extractKey")
    default Long toKey(Type model) {
        return model != null ? model.getId() : null;
    }
}

