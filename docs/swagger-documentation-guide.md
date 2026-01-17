# Guia Completa de Documentacion de API con Swagger/OpenAPI

Esta guia te ensena como documentar correctamente tus endpoints y DTOs usando anotaciones de OpenAPI 3.0.

## Tabla de Contenidos

1. [Configuracion Base](#configuracion-base)
2. [Documentar Controladores](#documentar-controladores)
3. [Documentar DTOs](#documentar-dtos)
4. [Documentar Operaciones](#documentar-operaciones)
5. [Documentar Parametros](#documentar-parametros)
6. [Documentar Respuestas](#documentar-respuestas)
7. [Ejemplos Completos](#ejemplos-completos)
8. [Mejores Practicas](#mejores-practicas)

---

## Configuracion Base

La configuracion de OpenAPI ya esta lista en `OpenApiConfig.java`. Solo debes documentar tus endpoints.

### Imports Necesarios

```java
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
```

---

## Documentar Controladores

### Nivel Basico

```java
@RestController
@RequestMapping("/api/products")
@Tag(name = "Productos", description = "Gestion de productos del catalogo")
public class ProductController {
    // ...
}
```

### Nivel Avanzado

```java
@RestController
@RequestMapping("/api/products")
@Tag(
    name = "Productos",
    description = "Endpoints para gestion completa de productos. " +
                  "Permite crear, consultar, actualizar y eliminar productos del catalogo."
)
@SecurityRequirement(name = "Bearer Authentication")
public class ProductController {
    // ...
}
```

**Explicacion:**
- `@Tag`: Agrupa los endpoints en Swagger UI
- `name`: Nombre del grupo
- `description`: Descripcion detallada
- `@SecurityRequirement`: Indica que requiere autenticacion

---

## Documentar DTOs

### DTO de Respuesta

```java
@Data
@Schema(description = "Informacion completa de un producto")
public class ProductResponse {

    @Schema(description = "ID unico del producto", example = "123")
    private Long id;

    @Schema(
        description = "Nombre del producto",
        example = "Laptop Dell XPS 15",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    private String name;

    @Schema(
        description = "Precio en USD",
        example = "1299.99",
        minimum = "0.01"
    )
    private Double price;

    @Schema(
        description = "Cantidad en stock",
        example = "50",
        minimum = "0"
    )
    private Integer stock;

    @Schema(
        description = "Estado del producto",
        example = "true",
        defaultValue = "true"
    )
    private Boolean active;
}
```

### DTO de Request

```java
@Data
@Schema(description = "Datos para crear un producto")
public class CreateProductRequest {

    @NotBlank(message = "{product.name.required}")
    @Size(min = 3, max = 100)
    @Schema(
        description = "Nombre del producto",
        example = "Laptop Dell XPS 15",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 3,
        maxLength = 100
    )
    private String name;

    @NotNull(message = "{product.price.required}")
    @DecimalMin(value = "0.01")
    @Schema(
        description = "Precio en USD",
        example = "1299.99",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minimum = "0.01"
    )
    private Double price;

    @Schema(
        description = "Categoria del producto",
        example = "Electronics",
        allowableValues = {"Electronics", "Clothing", "Food", "Books"}
    )
    private String category;
}
```

**Propiedades Importantes:**
- `description`: Descripcion del campo
- `example`: Valor de ejemplo
- `requiredMode`: Si es obligatorio
- `minimum/maximum`: Valores min/max
- `minLength/maxLength`: Longitud min/max
- `allowableValues`: Valores permitidos
- `defaultValue`: Valor por defecto
- `accessMode`: READ_ONLY, WRITE_ONLY, READ_WRITE

---

## Documentar Operaciones

### GET - Obtener un Recurso

```java
@Operation(
    summary = "Obtener producto por ID",
    description = "Retorna un producto especifico por su identificador unico"
)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Producto encontrado exitosamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ProductResponse.class)
        )
    ),
    @ApiResponse(
        responseCode = "404",
        description = "Producto no encontrado"
    ),
    @ApiResponse(
        responseCode = "401",
        description = "No autenticado"
    )
})
@GetMapping("/{id}")
public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
    return ResponseEntity.ok(productService.findById(id));
}
```

### POST - Crear un Recurso

```java
@Operation(
    summary = "Crear nuevo producto",
    description = "Crea un nuevo producto en el catalogo. Requiere rol ADMIN."
)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "201",
        description = "Producto creado exitosamente",
        content = @Content(schema = @Schema(implementation = ProductResponse.class))
    ),
    @ApiResponse(
        responseCode = "400",
        description = "Datos invalidos"
    ),
    @ApiResponse(
        responseCode = "403",
        description = "Sin permisos - Requiere rol ADMIN"
    )
})
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<ProductResponse> create(
    @Valid @RequestBody CreateProductRequest request
) {
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(productService.create(request));
}
```

### PUT - Actualizar un Recurso

```java
@Operation(
    summary = "Actualizar producto",
    description = "Actualiza un producto existente completamente"
)
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Producto actualizado"),
    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
    @ApiResponse(responseCode = "403", description = "Sin permisos")
})
@PutMapping("/{id}")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<ProductResponse> update(
    @PathVariable Long id,
    @Valid @RequestBody UpdateProductRequest request
) {
    return ResponseEntity.ok(productService.update(id, request));
}
```

### DELETE - Eliminar un Recurso

```java
@Operation(
    summary = "Eliminar producto",
    description = "Elimina un producto del catalogo (eliminacion logica)"
)
@ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Producto eliminado"),
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
})
@DeleteMapping("/{id}")
@ResponseStatus(HttpStatus.NO_CONTENT)
public ResponseEntity<Void> delete(@PathVariable Long id) {
    productService.delete(id);
    return ResponseEntity.noContent().build();
}
```

---

## Documentar Parametros

### Path Parameters

```java
@GetMapping("/{id}")
public ResponseEntity<ProductResponse> getById(
    @Parameter(
        name = "id",
        description = "ID unico del producto",
        required = true,
        example = "123"
    )
    @PathVariable Long id
) {
    // ...
}
```

### Query Parameters

```java
@GetMapping
public ResponseEntity<Page<ProductResponse>> search(
    @Parameter(description = "Filtro por nombre", example = "Laptop")
    @RequestParam(required = false) String name,

    @Parameter(description = "Categoria del producto", example = "Electronics")
    @RequestParam(required = false) String category,

    @Parameter(description = "Precio minimo", example = "100.00")
    @RequestParam(required = false) Double minPrice,

    @Parameter(description = "Precio maximo", example = "1000.00")
    @RequestParam(required = false) Double maxPrice,

    @Parameter(hidden = true)  // Oculta el parametro en Swagger
    @PageableDefault(size = 20) Pageable pageable
) {
    // ...
}
```

### Request Body

```java
@PostMapping
public ResponseEntity<ProductResponse> create(
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Datos del producto a crear",
        required = true,
        content = @Content(
            schema = @Schema(implementation = CreateProductRequest.class),
            examples = @ExampleObject(
                name = "Crear producto ejemplo",
                value = """
                    {
                      "name": "Laptop Dell XPS 15",
                      "price": 1299.99,
                      "stock": 50,
                      "category": "Electronics"
                    }
                    """
            )
        )
    )
    @Valid @RequestBody CreateProductRequest request
) {
    // ...
}
```

### Headers

```java
@GetMapping
public ResponseEntity<List<ProductResponse>> getAll(
    @Parameter(
        name = "Accept-Language",
        description = "Idioma de respuesta",
        example = "es",
        in = ParameterIn.HEADER
    )
    @RequestHeader(value = "Accept-Language", required = false) String language
) {
    // ...
}
```

---

## Documentar Respuestas

### Respuesta Simple

```java
@ApiResponse(
    responseCode = "200",
    description = "Operacion exitosa"
)
```

### Respuesta con Schema

```java
@ApiResponse(
    responseCode = "200",
    description = "Producto encontrado",
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = ProductResponse.class)
    )
)
```

### Respuesta con Ejemplo

```java
@ApiResponse(
    responseCode = "200",
    description = "Producto encontrado",
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = ProductResponse.class),
        examples = @ExampleObject(
            name = "Producto ejemplo",
            value = """
                {
                  "id": 123,
                  "name": "Laptop Dell XPS 15",
                  "price": 1299.99,
                  "stock": 50,
                  "active": true
                }
                """
        )
    )
)
```

### Multiples Respuestas

```java
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Exitoso",
        content = @Content(schema = @Schema(implementation = ProductResponse.class))
    ),
    @ApiResponse(
        responseCode = "400",
        description = "Datos invalidos",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    ),
    @ApiResponse(
        responseCode = "401",
        description = "No autenticado"
    ),
    @ApiResponse(
        responseCode = "404",
        description = "No encontrado"
    )
})
```

---

## Ejemplos Completos

### Controlador Completo

```java
package com.empresa.plantilla.infrastructure.controller;

import com.empresa.plantilla.commons.dto.example.CreateProductRequest;
import com.empresa.plantilla.commons.dto.example.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@Tag(
    name = "Productos",
    description = "Endpoints para gestion de productos del catalogo"
)
@SecurityRequirement(name = "Bearer Authentication")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
        summary = "Listar productos",
        description = "Retorna una lista paginada de productos con filtros opcionales"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista recuperada exitosamente"
        )
    })
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAll(
        @Parameter(description = "Filtro por nombre") 
        @RequestParam(required = false) String name,
        
        @Parameter(hidden = true)
        @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(productService.findAll(name, pageable));
    }

    @Operation(
        summary = "Obtener producto por ID",
        description = "Retorna un producto especifico"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(
        @Parameter(description = "ID del producto", example = "123")
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @Operation(
        summary = "Crear producto",
        description = "Crea un nuevo producto. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Creado"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos"),
        @ApiResponse(responseCode = "403", description = "Sin permisos")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> create(
        @Valid @RequestBody CreateProductRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.create(request));
    }

    @Operation(summary = "Actualizar producto")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> update(
        @PathVariable Long id,
        @Valid @RequestBody UpdateProductRequest request
    ) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    @Operation(summary = "Eliminar producto")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

---

## Mejores Practicas

### 1. Siempre Documenta

- **Controllers**: Usa `@Tag` para agrupar
- **Endpoints**: Usa `@Operation` con summary y description
- **DTOs**: Usa `@Schema` en la clase y campos
- **Respuestas**: Documenta todos los codigos posibles

### 2. Proporciona Ejemplos

```java
@Schema(description = "Precio", example = "99.99")
private Double price;
```

### 3. Indica Campos Obligatorios

```java
@Schema(
    description = "Nombre",
    requiredMode = Schema.RequiredMode.REQUIRED
)
private String name;
```

### 4. Documenta Validaciones

```java
@Schema(
    description = "Stock",
    minimum = "0",
    maximum = "9999"
)
private Integer stock;
```

### 5. Oculta Campos Internos

```java
@Schema(hidden = true)
private String internalField;

@Parameter(hidden = true)
@PageableDefault Pageable pageable;
```

### 6. Usa Enums

```java
@Schema(
    description = "Estado",
    allowableValues = {"ACTIVE", "INACTIVE", "PENDING"}
)
private String status;
```

### 7. Documenta READ_ONLY

```java
@Schema(
    description = "ID",
    accessMode = Schema.AccessMode.READ_ONLY
)
private Long id;
```

### 8. Agrupa Respuestas Comunes

```java
@ApiResponses(value = {
    @ApiResponse(responseCode = "401", description = "No autenticado"),
    @ApiResponse(responseCode = "403", description = "Sin permisos"),
    @ApiResponse(responseCode = "500", description = "Error interno")
})
```

### 9. Documenta Paginacion

```java
@Operation(description = "Retorna resultados paginados")
@GetMapping
public ResponseEntity<Page<ProductResponse>> getAll(
    @Parameter(hidden = true) Pageable pageable
) {
    // ...
}
```

### 10. Usa Markdown en Descriptions

```java
@Operation(
    description = """
        Crea un nuevo producto.
        
        ## Requisitos
        - Rol ADMIN requerido
        - Nombre unico
        - Precio mayor a 0
        
        ## Ejemplo
        ```json
        {
          "name": "Producto",
          "price": 99.99
        }
        ```
        """
)
```

---

## Checklist de Documentacion

Al crear un nuevo endpoint, verifica:

- [ ] Controlador tiene `@Tag`
- [ ] Endpoint tiene `@Operation` con summary
- [ ] Todos los parametros tienen `@Parameter`
- [ ] Request body esta documentado
- [ ] Respuestas principales documentadas (200, 400, 401, 404, 500)
- [ ] DTOs tienen `@Schema` en campos
- [ ] Ejemplos proporcionados
- [ ] Validaciones documentadas (min, max, required)
- [ ] Campos READ_ONLY marcados
- [ ] Seguridad configurada si aplica

---

## Recursos

- [OpenAPI Specification](https://swagger.io/specification/)
- [SpringDoc Documentation](https://springdoc.org/)
- [Swagger Annotations](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Annotations)

---

**Fecha**: 2026-01-17  
**Version**: 1.0.0
