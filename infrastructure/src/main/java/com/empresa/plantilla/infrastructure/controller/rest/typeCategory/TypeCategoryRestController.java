package com.empresa.plantilla.infrastructure.controller.rest.typeCategory;

import com.empresa.plantilla.commons.dto.pageable.PageContext;
import com.empresa.plantilla.commons.dto.response.GenericResponse;
import com.empresa.plantilla.commons.helper.ApiResponseBuilder;
import com.empresa.plantilla.commons.services.i18.MessageService;
import com.empresa.plantilla.commons.services.pageable.IPageableResult;
import com.empresa.plantilla.infrastructure.adapters.output.services.pageable.PageableResultImpl;
import com.empresa.plantilla.domain.command.typeCategory.TypeCategoryCreateCommand;
import com.empresa.plantilla.domain.command.typeCategory.TypeCategoryDeleteCommand;
import com.empresa.plantilla.domain.command.typeCategory.TypeCategoryUpdateCommand;
import com.empresa.plantilla.domain.model.TypeCategory;
import com.empresa.plantilla.domain.query.typeCategory.GetAllTypeCategoriesPaginadoQuery;
import com.empresa.plantilla.domain.query.typeCategory.GetAllTypeCategoriesQuery;
import com.empresa.plantilla.domain.query.typeCategory.GetTypeCategoryByIdQuery;
import com.empresa.plantilla.infrastructure.constants.RestConstants;
import com.empresa.plantilla.infrastructure.dto.typeCategory.TypeCategoryCreateRequestDto;
import com.empresa.plantilla.infrastructure.dto.typeCategory.TypeCategoryFilterDto;
import com.empresa.plantilla.infrastructure.dto.typeCategory.TypeCategoryResponseDto;
import com.empresa.plantilla.infrastructure.dto.typeCategory.TypeCategoryUpdateRequestDto;
import com.empresa.plantilla.infrastructure.mapper.typeCategory.TypeCategoryInfrastructureMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(RestConstants.API_TYPE_CATEGORY)
@Tag(name = RestConstants.TAG_TYPE_CATEGORY, description = RestConstants.DOC_TYPE_CATEGORY_CONTROLLER)
public class TypeCategoryRestController {

    private final TypeCategoryInfrastructureMapper mapper;
    private final ApiResponseBuilder responseBuilder;
    private final MessageService messageService;
    private final TypeCategoryCreateCommand typeCategoryCreateCommand;
    private final TypeCategoryUpdateCommand typeCategoryUpdateCommand;
    private final TypeCategoryDeleteCommand typeCategoryDeleteCommand;
    private final GetTypeCategoryByIdQuery getTypeCategoryByIdQuery;
    private final GetAllTypeCategoriesQuery getAllTypeCategoriesQuery;
    private final GetAllTypeCategoriesPaginadoQuery getAllTypeCategoriesPaginadoQuery;

    public TypeCategoryRestController(TypeCategoryInfrastructureMapper mapper,
                                      ApiResponseBuilder responseBuilder,
                                      MessageService messageService,
                                      TypeCategoryCreateCommand typeCategoryCreateCommand,
                                      TypeCategoryUpdateCommand typeCategoryUpdateCommand,
                                      TypeCategoryDeleteCommand typeCategoryDeleteCommand,
                                      GetTypeCategoryByIdQuery getTypeCategoryByIdQuery,
                                      GetAllTypeCategoriesQuery getAllTypeCategoriesQuery,
                                      GetAllTypeCategoriesPaginadoQuery getAllTypeCategoriesPaginadoQuery) {
        this.mapper = mapper;
        this.responseBuilder = responseBuilder;
        this.messageService = messageService;
        this.typeCategoryCreateCommand = typeCategoryCreateCommand;
        this.typeCategoryUpdateCommand = typeCategoryUpdateCommand;
        this.typeCategoryDeleteCommand = typeCategoryDeleteCommand;
        this.getTypeCategoryByIdQuery = getTypeCategoryByIdQuery;
        this.getAllTypeCategoriesQuery = getAllTypeCategoriesQuery;
        this.getAllTypeCategoriesPaginadoQuery = getAllTypeCategoriesPaginadoQuery;
    }

    @PostMapping
    @Operation(summary = RestConstants.DOC_TYPE_CATEGORY_CREATE)
    @Transactional(rollbackFor = Exception.class)
    public GenericResponse<TypeCategoryResponseDto> create(@Valid @RequestBody TypeCategoryCreateRequestDto request) {
        typeCategoryCreateCommand.setAsync(false);
        typeCategoryCreateCommand.setContext(mapper.fromCreateDto(request));
        TypeCategory created = typeCategoryCreateCommand.execute();

        return responseBuilder.success(
                mapper.toResponseDto(created),
                messageService.getMessage(RestConstants.MSG_TYPE_CATEGORY_CREATED)
        );
    }

    @PutMapping
    @Operation(summary = RestConstants.DOC_TYPE_CATEGORY_UPDATE)
    @Transactional(rollbackFor = Exception.class)
    public GenericResponse<TypeCategoryResponseDto> update(@Valid @RequestBody TypeCategoryUpdateRequestDto request) {
        typeCategoryUpdateCommand.setAsync(false);
        typeCategoryUpdateCommand.setContext(mapper.fromUpdateDto(request));
        TypeCategory updated = typeCategoryUpdateCommand.execute();

        return responseBuilder.success(
                mapper.toResponseDto(updated),
                messageService.getMessage(RestConstants.MSG_TYPE_CATEGORY_UPDATED)
        );
    }

    @DeleteMapping(RestConstants.PATH_ID)
    @Operation(summary = RestConstants.DOC_TYPE_CATEGORY_DELETE)
    @Transactional(rollbackFor = Exception.class)
    public GenericResponse<TypeCategoryResponseDto> delete(@PathVariable Long id) {
        typeCategoryDeleteCommand.setAsync(false);
        typeCategoryDeleteCommand.setContext(TypeCategory.builder().id(id).build());
        TypeCategory deleted = typeCategoryDeleteCommand.execute();

        return responseBuilder.success(
                mapper.toResponseDto(deleted),
                messageService.getMessage(RestConstants.MSG_TYPE_CATEGORY_DELETED)
        );
    }

    @GetMapping(RestConstants.PATH_ID)
    @Operation(summary = RestConstants.DOC_TYPE_CATEGORY_GET_BY_ID)
    public GenericResponse<TypeCategoryResponseDto> getById(@PathVariable Long id) {
        TypeCategory result = getTypeCategoryByIdQuery.execute(id);

        return responseBuilder.success(
                mapper.toResponseDto(result),
                messageService.getMessage(RestConstants.MSG_TYPE_CATEGORY_FOUND)
        );
    }

    @GetMapping(RestConstants.PATH_COMBO)
    @Operation(summary = RestConstants.DOC_TYPE_CATEGORY_COMBO)
    public GenericResponse<TypeCategoryResponseDto> combo(
            @Parameter(description = "Filtro: id")
            @RequestParam(required = false) Long id,
            @Parameter(description = "Filtro: name")
            @RequestParam(required = false) String name,
            @Parameter(description = "Filtro: code")
            @RequestParam(required = false) String code,
            @Parameter(description = "Filtro: active")
            @RequestParam(required = false) Boolean active) {
        TypeCategory context = mapper.fromFilterDto(new TypeCategoryFilterDto(id, name, code, active));
        List<TypeCategory> result = getAllTypeCategoriesQuery.execute(context);

        return responseBuilder.successList(
                mapper.toResponseDtoList(result),
                messageService.getMessage(RestConstants.MSG_TYPE_CATEGORY_LIST)
        );
    }

    @GetMapping(RestConstants.PATH_PAGINADO)
    @Operation(summary = RestConstants.DOC_TYPE_CATEGORY_PAGINADO)
    public GenericResponse<TypeCategoryResponseDto> paginado(
            @Parameter(description = "Filtro: id")
            @RequestParam(required = false) Long id,
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
        PageContext<TypeCategory> context = PageContext.<TypeCategory>builder()
                .data(mapper.fromFilterDto(new TypeCategoryFilterDto(id, name, code, active)))
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .sortDir(sortDir)
                .filterType(filterType)
                .build();

        IPageableResult<TypeCategory> result = getAllTypeCategoriesPaginadoQuery.execute(context);

        IPageableResult<TypeCategoryResponseDto> mapped = new PageableResultImpl<>(
                mapper.toResponseDtoList(result.getContent()),
                result.getPageNumber(),
                result.getPageSize(),
                result.getTotalElements()
        );

        return responseBuilder.paginated(
                mapped,
                messageService.getMessage(RestConstants.MSG_TYPE_CATEGORY_PAGE)
        );
    }
}

