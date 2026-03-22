package com.empresa.plantilla.infrastructure.controller.rest.type;

import com.empresa.plantilla.commons.dto.pageable.PageContext;
import com.empresa.plantilla.commons.dto.response.GenericResponse;
import com.empresa.plantilla.commons.helper.ApiResponseBuilder;
import com.empresa.plantilla.commons.services.i18.MessageService;
import com.empresa.plantilla.commons.services.pageable.IPageableResult;
import com.empresa.plantilla.domain.command.type.TypeCreateCommand;
import com.empresa.plantilla.domain.command.type.TypeDeleteCommand;
import com.empresa.plantilla.domain.command.type.TypeUpdateCommand;
import com.empresa.plantilla.domain.model.Type;
import com.empresa.plantilla.domain.query.type.GetTypeByIdQuery;
import com.empresa.plantilla.domain.query.type.GetTypesByTypeCategoryPaginadoQuery;
import com.empresa.plantilla.domain.query.type.GetTypesByTypeCategoryQuery;
import com.empresa.plantilla.infrastructure.adapters.output.services.pageable.PageableResultImpl;
import com.empresa.plantilla.infrastructure.constants.RestConstants;
import com.empresa.plantilla.infrastructure.dto.type.TypeCreateRequestDto;
import com.empresa.plantilla.infrastructure.dto.type.TypeFilterDto;
import com.empresa.plantilla.infrastructure.dto.type.TypeResponseDto;
import com.empresa.plantilla.infrastructure.dto.type.TypeUpdateRequestDto;
import com.empresa.plantilla.infrastructure.mapper.type.TypeInfrastructureMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para Type.
 */
@RestController
@RequestMapping(RestConstants.API_TYPE)
@Tag(name = RestConstants.TAG_TYPE, description = RestConstants.DOC_TYPE_CONTROLLER)
public class TypeRestController {

    private final TypeInfrastructureMapper mapper;
    private final ApiResponseBuilder responseBuilder;
    private final MessageService messageService;
    private final TypeCreateCommand typeCreateCommand;
    private final TypeUpdateCommand typeUpdateCommand;
    private final TypeDeleteCommand typeDeleteCommand;
    private final GetTypeByIdQuery getTypeByIdQuery;
    private final GetTypesByTypeCategoryQuery getTypesByTypeCategoryQuery;
    private final GetTypesByTypeCategoryPaginadoQuery getTypesByTypeCategoryPaginadoQuery;

    public TypeRestController(TypeInfrastructureMapper mapper,
                              ApiResponseBuilder responseBuilder,
                              MessageService messageService,
                              TypeCreateCommand typeCreateCommand,
                              TypeUpdateCommand typeUpdateCommand,
                              TypeDeleteCommand typeDeleteCommand,
                              GetTypeByIdQuery getTypeByIdQuery,
                              GetTypesByTypeCategoryQuery getTypesByTypeCategoryQuery,
                              GetTypesByTypeCategoryPaginadoQuery getTypesByTypeCategoryPaginadoQuery) {
        this.mapper = mapper;
        this.responseBuilder = responseBuilder;
        this.messageService = messageService;
        this.typeCreateCommand = typeCreateCommand;
        this.typeUpdateCommand = typeUpdateCommand;
        this.typeDeleteCommand = typeDeleteCommand;
        this.getTypeByIdQuery = getTypeByIdQuery;
        this.getTypesByTypeCategoryQuery = getTypesByTypeCategoryQuery;
        this.getTypesByTypeCategoryPaginadoQuery = getTypesByTypeCategoryPaginadoQuery;
    }

    @PostMapping
    @Operation(summary = RestConstants.DOC_TYPE_CREATE)
    @Transactional(rollbackFor = Exception.class)
    public GenericResponse<TypeResponseDto> create(@Valid @RequestBody TypeCreateRequestDto request) {
        typeCreateCommand.setAsync(false);
        typeCreateCommand.setContext(mapper.fromCreateDto(request));
        Type created = typeCreateCommand.execute();
        return responseBuilder.success(
                mapper.toResponseDto(created),
                messageService.getMessage(RestConstants.MSG_TYPE_CREATED)
        );
    }

    @PutMapping
    @Operation(summary = RestConstants.DOC_TYPE_UPDATE)
    @Transactional(rollbackFor = Exception.class)
    public GenericResponse<TypeResponseDto> update(@Valid @RequestBody TypeUpdateRequestDto request) {
        typeUpdateCommand.setAsync(false);
        typeUpdateCommand.setContext(mapper.fromUpdateDto(request));
        Type updated = typeUpdateCommand.execute();
        return responseBuilder.success(
                mapper.toResponseDto(updated),
                messageService.getMessage(RestConstants.MSG_TYPE_UPDATED)
        );
    }

    @DeleteMapping(RestConstants.PATH_ID)
    @Operation(summary = RestConstants.DOC_TYPE_DELETE)
    @Transactional(rollbackFor = Exception.class)
    public GenericResponse<TypeResponseDto> delete(@PathVariable Long id) {
        typeDeleteCommand.setAsync(false);
        typeDeleteCommand.setContext(Type.builder().id(id).build());
        Type deleted = typeDeleteCommand.execute();
        return responseBuilder.success(
                mapper.toResponseDto(deleted),
                messageService.getMessage(RestConstants.MSG_TYPE_DELETED)
        );
    }

    @GetMapping(RestConstants.PATH_ID)
    @Operation(summary = RestConstants.DOC_TYPE_GET_BY_ID)
    public GenericResponse<TypeResponseDto> getById(@PathVariable Long id) {
        Type result = getTypeByIdQuery.execute(id);

        return responseBuilder.success(
                mapper.toResponseDto(result),
                messageService.getMessage(RestConstants.MSG_TYPE_FOUND)
        );
    }

    @GetMapping(RestConstants.PATH_COMBO)
    @Operation(summary = RestConstants.DOC_TYPE_COMBO)
    public GenericResponse<TypeResponseDto> combo(
            @Parameter(description = "Filtro: id")
            @RequestParam(required = false) Long id,
            @Parameter(description = "Filtro: typeCategoryId")
            @RequestParam(required = false) Long typeCategoryId,
            @Parameter(description = "Filtro: name")
            @RequestParam(required = false) String name,
            @Parameter(description = "Filtro: code")
            @RequestParam(required = false) String code,
            @Parameter(description = "Filtro: active")
            @RequestParam(required = false) Boolean active) {
        Type context = mapper.fromFilterDto(new TypeFilterDto(id, typeCategoryId, name, code, active));
        List<Type> result = getTypesByTypeCategoryQuery.execute(context);

        return responseBuilder.successList(
                mapper.toResponseDtoList(result),
                messageService.getMessage(RestConstants.MSG_TYPE_LIST)
        );
    }

    @GetMapping(RestConstants.PATH_PAGINADO)
    @Operation(summary = RestConstants.DOC_TYPE_PAGINADO)
    public GenericResponse<TypeResponseDto> paginado(
            @Parameter(description = "Filtro: id")
            @RequestParam(required = false) Long id,
            @Parameter(description = "Filtro: typeCategoryId")
            @RequestParam(required = false) Long typeCategoryId,
            @Parameter(description = "Filtro: name")
            @RequestParam(required = false) String name,
            @Parameter(description = "Filtro: code")
            @RequestParam(required = false) String code,
            @Parameter(description = "Filtro: active")
            @RequestParam(required = false) Boolean active,
            @Parameter(description = "Numero de pagina (base 0)")
            @RequestParam(required = false) Integer pageNumber,
            @Parameter(description = "Tamanio de pagina")
            @RequestParam(required = false) Integer pageSize,
            @Parameter(description = "Campo de ordenamiento")
            @RequestParam(required = false) String sortBy,
            @Parameter(description = "Direccion de ordenamiento")
            @RequestParam(required = false) String sortDir,
            @Parameter(description = "Tipo de filtro")
            @RequestParam(required = false) String filterType) {
        PageContext<Type> context = PageContext.<Type>builder()
                .data(mapper.fromFilterDto(new TypeFilterDto(id, typeCategoryId, name, code, active)))
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .sortDir(sortDir)
                .filterType(filterType)
                .build();

        IPageableResult<Type> result = getTypesByTypeCategoryPaginadoQuery.execute(context);

        IPageableResult<TypeResponseDto> mapped = new PageableResultImpl<>(
                mapper.toResponseDtoList(result.getContent()),
                result.getPageNumber(),
                result.getPageSize(),
                result.getTotalElements()
        );

        return responseBuilder.paginated(
                mapped,
                messageService.getMessage(RestConstants.MSG_TYPE_PAGE)
        );
    }
}

