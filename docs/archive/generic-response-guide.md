# Gu├¡a de Uso de GenericResponse

## Introducci├│n

`GenericResponse<T>` es la clase unificada para todas las respuestas de API en el proyecto. Reemplaza las clases
anteriores `SimpleResponse` y `BodyResponse`, proporcionando una soluci├│n m├ís flexible y completa.

## Estructura de GenericResponse

```java
public class GenericResponse<T> {
    private boolean ok;           // Indica ├®xito o error
    private Integer codigo;       // C├│digo HTTP
    private String mensaje;       // Mensaje descriptivo
    private T dato;              // Objeto ├║nico (opcional)
    private List<T> datos;       // Lista de objetos (opcional)
    private Integer conteo;      // Cantidad de items (opcional)
    private String totales;      // Informaci├│n de totales (opcional)
}
```

## Casos de Uso

### 1. Respuesta Simple con un Objeto

Para retornar un solo objeto:

```java
@GetMapping("/{id}")
public ResponseEntity<GenericResponse<UserDto>> getUserById(@PathVariable Long id) {
    UserDto user = userService.findById(id);
    
    GenericResponse<UserDto> response = GenericResponse.success(
        HttpStatus.OK.value(),
        "Usuario encontrado exitosamente",
        user
    );
    
    return ResponseEntity.ok(response);
}
```

**Respuesta JSON:**

```json
{
  "ok": true,
  "codigo": 200,
  "mensaje": "Usuario encontrado exitosamente",
  "dato": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com"
  }
}
```

### 2. Respuesta con Lista de Objetos

Para retornar una lista simple:

```java
@GetMapping
public ResponseEntity<GenericResponse<UserDto>> getAllUsers() {
    List<UserDto> users = userService.findAll();
    
    GenericResponse<UserDto> response = GenericResponse.success(
        HttpStatus.OK.value(),
        "Usuarios encontrados",
        users
    );
    
    return ResponseEntity.ok(response);
}
```

**Respuesta JSON:**

```json
{
  "ok": true,
  "codigo": 200,
  "mensaje": "Usuarios encontrados",
  "datos": [
    {
      "id": 1,
      "name": "John Doe",
      "email": "john@example.com"
    },
    {
      "id": 2,
      "name": "Jane Smith",
      "email": "jane@example.com"
    }
  ],
  "conteo": 2
}
```

### 3. Respuesta Paginada con Metadatos

Para retornar resultados paginados con informaci├│n adicional:

```java
@GetMapping("/paginated")
public ResponseEntity<GenericResponse<UserDto>> getUsersPaginated(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    
    Page<UserDto> userPage = userService.findAllPaginated(page, size);
    
    GenericResponse<UserDto> response = GenericResponse.successPaginated(
        HttpStatus.OK.value(),
        "Usuarios paginados recuperados",
        userPage.getContent(),
        (int) userPage.getTotalElements(),
        String.format("P├ígina %d de %d", page + 1, userPage.getTotalPages())
    );
    
    return ResponseEntity.ok(response);
}
```

**Respuesta JSON:**

```json
{
  "ok": true,
  "codigo": 200,
  "mensaje": "Usuarios paginados recuperados",
  "datos": [
    {
      "id": 1,
      "name": "John Doe",
      "email": "john@example.com"
    },
    {
      "id": 2,
      "name": "Jane Smith",
      "email": "jane@example.com"
    }
  ],
  "conteo": 100,
  "totales": "P├ígina 1 de 10"
}
```

### 4. Respuesta de Error

Para retornar errores:

```java
@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<GenericResponse<Void>> handleNotFound(ResourceNotFoundException ex) {
    GenericResponse<Void> response = GenericResponse.error(
        HttpStatus.NOT_FOUND.value(),
        ex.getMessage()
    );
    
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
}
```

**Respuesta JSON:**

```json
{
  "ok": false,
  "codigo": 404,
  "mensaje": "Usuario no encontrado"
}
```

### 5. Respuesta de Creaci├│n

Para operaciones POST que crean recursos:

```java
@PostMapping
public ResponseEntity<GenericResponse<UserDto>> createUser(@RequestBody CreateUserRequest request) {
    UserDto createdUser = userService.create(request);
    
    GenericResponse<UserDto> response = GenericResponse.success(
        HttpStatus.CREATED.value(),
        "Usuario creado exitosamente",
        createdUser
    );
    
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}
```

**Respuesta JSON:**

```json
{
  "ok": true,
  "codigo": 201,
  "mensaje": "Usuario creado exitosamente",
  "dato": {
    "id": 3,
    "name": "New User",
    "email": "newuser@example.com"
  }
}
```

### 6. Respuesta de Actualizaci├│n

Para operaciones PUT/PATCH:

```java
@PutMapping("/{id}")
public ResponseEntity<GenericResponse<UserDto>> updateUser(
        @PathVariable Long id,
        @RequestBody UpdateUserRequest request) {
    
    UserDto updatedUser = userService.update(id, request);
    
    GenericResponse<UserDto> response = GenericResponse.success(
        HttpStatus.OK.value(),
        "Usuario actualizado exitosamente",
        updatedUser
    );
    
    return ResponseEntity.ok(response);
}
```

### 7. Respuesta de Eliminaci├│n

Para operaciones DELETE:

```java
@DeleteMapping("/{id}")
public ResponseEntity<GenericResponse<Void>> deleteUser(@PathVariable Long id) {
    userService.delete(id);
    
    GenericResponse<Void> response = GenericResponse.success(
        HttpStatus.OK.value(),
        "Usuario eliminado exitosamente",
        (Void) null
    );
    
    return ResponseEntity.ok(response);
}
```

**Respuesta JSON:**

```json
{
  "ok": true,
  "codigo": 200,
  "mensaje": "Usuario eliminado exitosamente"
}
```

## Uso con Commands y Queries

### En Commands

```java
@PostMapping("/register")
public ResponseEntity<GenericResponse<RegistrationResult>> register(
        @RequestBody RegistrationRequest request) {
    
    // Crear contexto
    RegistrationContext context = RegistrationContext.from(request);
    
    // Ejecutar command
    registerCommand.setContext(context);
    RegistrationResult result = registerCommand.execute();
    
    // Retornar respuesta
    GenericResponse<RegistrationResult> response = GenericResponse.success(
        HttpStatus.CREATED.value(),
        "Usuario registrado exitosamente",
        result
    );
    
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}
```

### En Queries

```java
@GetMapping("/search")
public ResponseEntity<GenericResponse<UserDto>> searchUsers(
        @RequestParam String keyword,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    
    // Crear contexto de b├║squeda
    SearchUserContext context = SearchUserContext.builder()
            .keyword(keyword)
            .page(page)
            .size(size)
            .build();
    
    // Ejecutar query
    SearchUserResult result = searchUserQuery.execute(context);
    
    // Retornar respuesta paginada
    GenericResponse<UserDto> response = GenericResponse.successPaginated(
        HttpStatus.OK.value(),
        "B├║squeda completada",
        result.getUsers(),
        result.getTotalElements(),
        String.format("Encontrados %d usuarios", result.getTotalElements())
    );
    
    return ResponseEntity.ok(response);
}
```

## Manejo de Errores Global

### ErrorHandlerConfig

```java
@RestControllerAdvice
public class ErrorHandlerConfig extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<GenericResponse<Void>> handleDomainException(DomainException ex) {
        GenericResponse<Void> response = GenericResponse.error(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage()
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GenericResponse<Void>> handleNotFoundException(
            ResourceNotFoundException ex) {
        GenericResponse<Void> response = GenericResponse.error(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<GenericResponse<Map<String, String>>> handleValidationException(
            ValidationException ex) {
        
        Map<String, String> errors = ex.getErrors();
        
        GenericResponse<Map<String, String>> response = GenericResponse.<Map<String, String>>builder()
                .ok(false)
                .codigo(HttpStatus.BAD_REQUEST.value())
                .mensaje("Errores de validaci├│n")
                .dato(errors)
                .build();
        
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<Void>> handleGenericException(Exception ex) {
        log.error("Error inesperado", ex);
        
        GenericResponse<Void> response = GenericResponse.error(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Error interno del servidor"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
```

## M├®todos Factory Disponibles

### 1. success(codigo, mensaje, dato)

Crea una respuesta exitosa con un solo objeto.

### 2. success(codigo, mensaje, datos)

Crea una respuesta exitosa con una lista de objetos. Calcula autom├íticamente el `conteo`.

### 3. successPaginated(codigo, mensaje, datos, conteo, totales)

Crea una respuesta paginada con metadatos adicionales.

### 4. error(codigo, mensaje)

Crea una respuesta de error.

## Builder Pattern

Tambi├®n puedes usar el patr├│n builder para mayor flexibilidad:

```java
GenericResponse<UserDto> response = GenericResponse.<UserDto>builder()
        .ok(true)
        .codigo(HttpStatus.OK.value())
        .mensaje("Operaci├│n exitosa")
        .dato(userDto)
        .build();
```

## Mejores Pr├ícticas

### 1. Ô£à Usar los m├®todos factory

```java
// CORRECTO
GenericResponse<User> response = GenericResponse.success(200, "OK", user);
```

```java
// EVITAR (usar builder solo cuando sea necesario)
GenericResponse<User> response = new GenericResponse<>();
response.setOk(true);
response.setCodigo(200);
response.setMensaje("OK");
response.setDato(user);
```

### 2. Ô£à Usar tipos espec├¡ficos

```java
// CORRECTO
public ResponseEntity<GenericResponse<UserDto>> getUser() { }
```

```java
// EVITAR
public ResponseEntity<GenericResponse> getUser() { }  // Sin tipo gen├®rico
```

### 3. Ô£à Mensajes descriptivos

```java
// CORRECTO
GenericResponse.success(200, "Usuario creado exitosamente", user);
```

```java
// EVITAR
GenericResponse.success(200, "OK", user);
```

### 4. Ô£à C├│digos HTTP apropiados

```java
// CORRECTO
GenericResponse.success(HttpStatus.CREATED.value(), "Creado", user);
GenericResponse.error(HttpStatus.NOT_FOUND.value(), "No encontrado");
```

### 5. Ô£à Consistencia en paginaci├│n

```java
// CORRECTO - Siempre incluir conteo y totales en paginaci├│n
GenericResponse.successPaginated(200, "OK", users, totalElements, "P├ígina 1 de 5");
```

## Migraci├│n desde Clases Antiguas

### Desde SimpleResponse

**Antes:**

```java
SimpleResponse<User> response = SimpleResponse.<User>builder()
        .ok(true)
        .codigo(200)
        .mensaje("OK")
        .cuerpo(user)
        .build();
```

**Ahora:**

```java
GenericResponse<User> response = GenericResponse.success(200, "OK", user);
```

### Desde BodyResponse

**Antes:**

```java
BodyResponse<User> body = BodyResponse.<User>builder()
        .conteo(users.size())
        .datos(users)
        .totales("Total: 100")
        .build();

GenericResponse<User> response = GenericResponse.<User>builder()
        .ok(true)
        .codigo(200)
        .mensaje("OK")
        .cuerpo(body)
        .build();
```

**Ahora:**

```java
GenericResponse<User> response = GenericResponse.successPaginated(
    200, 
    "OK", 
    users, 
    100, 
    "Total: 100"
);
```

## Ventajas de GenericResponse

1. **Unificaci├│n**: Una sola clase para todos los tipos de respuesta
2. **Flexibilidad**: Maneja respuestas simples, listas y paginadas
3. **Serializaci├│n eficiente**: `@JsonInclude(NON_NULL)` omite campos null
4. **Type-safe**: Gen├®ricos fuertemente tipados
5. **Factory methods**: M├®todos est├íticos convenientes para casos comunes
6. **Builder pattern**: Flexibilidad para casos complejos
7. **Documentaci├│n clara**: JavaDoc completo

## ResponseBuilder - Helper para Respuestas

Para simplificar a├║n m├ís la creaci├│n de respuestas, se proporciona la clase `ResponseBuilder` que act├║a como una fachada
sobre `GenericResponse`.

### M├®todos Disponibles

#### Respuestas de ├ëxito

```java
// Objeto ├║nico
ResponseBuilder.success(user);
ResponseBuilder.success(user, "Usuario encontrado");

// Lista de objetos
ResponseBuilder.successList(users);
ResponseBuilder.successList(users, "Usuarios encontrados");

// Respuesta creada (HTTP 201)
ResponseBuilder.created(user);
ResponseBuilder.created(user, "Usuario creado");

// Sin contenido (HTTP 204)
ResponseBuilder.noContent();
ResponseBuilder.noContent("Eliminado exitosamente");
```

#### Respuestas Paginadas

```java
// Desde IPageableResult
ResponseBuilder.paginated(pageableResult);
ResponseBuilder.paginated(pageableResult, "Usuarios paginados");

// Desde lista manual
ResponseBuilder.paginatedFromList(users, pageNumber, pageSize);
```

#### Respuestas de Error

```java
// Error gen├®rico
ResponseBuilder.error("Mensaje de error");
ResponseBuilder.error(exception);
ResponseBuilder.error(exception, 500);
ResponseBuilder.error(400, "Solicitud inv├ílida");

// Errores espec├¡ficos
ResponseBuilder.badRequest("Datos inv├ílidos");           // 400
ResponseBuilder.unauthorized("No autenticado");          // 401
ResponseBuilder.forbidden("Sin permisos");               // 403
ResponseBuilder.notFound("Usuario no encontrado");       // 404
```

### Ejemplos de Uso con ResponseBuilder

#### En un Controller REST

```java
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<UserDto>> getUser(@PathVariable Long id) {
        UserDto user = userService.findById(id);
        return ResponseEntity.ok(ResponseBuilder.success(user));
    }

    @GetMapping
    public ResponseEntity<GenericResponse<UserDto>> getAllUsers() {
        List<UserDto> users = userService.findAll();
        return ResponseEntity.ok(ResponseBuilder.successList(users, "Usuarios recuperados"));
    }

    @GetMapping("/paginated")
    public ResponseEntity<GenericResponse<UserDto>> getUsersPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        IPageableResult<UserDto> result = userService.findAllPaginated(page, size);
        return ResponseEntity.ok(ResponseBuilder.paginated(result));
    }

    @PostMapping
    public ResponseEntity<GenericResponse<UserDto>> createUser(@RequestBody CreateUserRequest request) {
        UserDto user = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseBuilder.created(user, "Usuario creado exitosamente"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse<UserDto>> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {
        UserDto user = userService.update(id, request);
        return ResponseEntity.ok(ResponseBuilder.success(user, "Usuario actualizado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(ResponseBuilder.noContent("Usuario eliminado"));
    }
}
```

#### Con Commands

```java
@PostMapping("/register")
public ResponseEntity<GenericResponse<RegistrationResult>> register(
        @RequestBody RegistrationRequest request) {
    try {
        RegistrationContext context = RegistrationContext.from(request);
        registerCommand.setContext(context);
        RegistrationResult result = registerCommand.execute();
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseBuilder.created(result, "Registro exitoso"));
    } catch (DomainException e) {
        return ResponseEntity.badRequest()
                .body(ResponseBuilder.badRequest(e.getMessage()));
    }
}
```

#### Con Queries Paginadas

```java
@GetMapping("/search")
public ResponseEntity<GenericResponse<UserDto>> searchUsers(
        @RequestParam String keyword,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    
    SearchUserContext context = SearchUserContext.builder()
            .keyword(keyword)
            .page(page)
            .size(size)
            .build();
    
    IPageableResult<UserDto> result = searchUserQuery.execute(context);
    
    return ResponseEntity.ok(
            ResponseBuilder.paginated(result, "B├║squeda completada")
    );
}
```

#### En ErrorHandler

```java
@RestControllerAdvice
public class ErrorHandlerConfig extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<GenericResponse<Void>> handleDomainException(DomainException ex) {
        return ResponseEntity.badRequest()
                .body(ResponseBuilder.badRequest(ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GenericResponse<Void>> handleNotFoundException(
            ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseBuilder.notFound(ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<GenericResponse<Void>> handleUnauthorizedException(
            UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ResponseBuilder.unauthorized(ex.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<GenericResponse<Void>> handleForbiddenException(
            ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ResponseBuilder.forbidden(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<Void>> handleGenericException(Exception ex) {
        log.error("Error inesperado", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseBuilder.error(ex, HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
```

### Comparaci├│n: Antes vs Ahora

#### Antes (con clases antiguas y sin helper)

```java
// Respuesta simple
GenericResponse<User> response = GenericResponse.<User>builder()
        .ok(true)
        .codigo(200)
        .mensaje("Usuario encontrado")
        .cuerpo(BodyResponse.<User>builder()
                .conteo(1)
                .datos(Collections.singletonList(user))
                .build())
        .build();

// Respuesta paginada
GenericResponse<User> response = GenericResponse.<User>builder()
        .ok(true)
        .codigo(200)
        .mensaje("Usuarios paginados")
        .cuerpo(BodyResponse.<User>builder()
                .conteo(pageableResult.getTotalElements().intValue())
                .datos(pageableResult.getContent())
                .totales("P├ígina 1 de 10")
                .build())
        .build();
```

#### Ahora (con GenericResponse simplificado y ResponseBuilder)

```java
// Respuesta simple
GenericResponse<User> response = ResponseBuilder.success(user, "Usuario encontrado");

// Respuesta paginada
GenericResponse<User> response = ResponseBuilder.paginated(pageableResult);
```

**Reducci├│n de c├│digo: ~80%**

### Ventajas de usar ResponseBuilder

1. Ô£à **C├│digo m├ís limpio**: Menos verbosidad
2. Ô£à **Consistencia**: Respuestas estandarizadas en toda la aplicaci├│n
3. Ô£à **Mantenibilidad**: Cambios centralizados
4. Ô£à **Legibilidad**: M├®todos con nombres descriptivos
5. Ô£à **Type-safe**: Inferencia de tipos autom├ítica
6. Ô£à **HTTP Status**: C├│digos HTTP correctos autom├íticamente

### Cu├índo usar qu├®

| Caso de Uso        | M├®todo Recomendado                          |
|--------------------|---------------------------------------------|
| Retornar un objeto | `ResponseBuilder.success(obj)`              |
| Retornar una lista | `ResponseBuilder.successList(list)`         |
| Retornar paginado  | `ResponseBuilder.paginated(pageableResult)` |
| Crear recurso      | `ResponseBuilder.created(obj)`              |
| Eliminar recurso   | `ResponseBuilder.noContent()`               |
| Error gen├®rico     | `ResponseBuilder.error(mensaje)`            |
| No encontrado      | `ResponseBuilder.notFound(mensaje)`         |
| No autorizado      | `ResponseBuilder.unauthorized(mensaje)`     |
| Sin permisos       | `ResponseBuilder.forbidden(mensaje)`        |
| Datos inv├ílidos    | `ResponseBuilder.badRequest(mensaje)`       |

## Conclusi├│n

`GenericResponse<T>` junto con `ResponseBuilder` proporcionan una soluci├│n completa y simplificada para manejar
respuestas de API:

- **GenericResponse**: Clase unificada para todas las respuestas
- **ResponseBuilder**: Helper para crear respuestas de forma conveniente

Las clases `SimpleResponse` y `BodyResponse` est├ín marcadas como `@Deprecated` y se eliminar├ín en futuras versiones.

**Recomendaci├│n**: Usa siempre `ResponseBuilder` para crear respuestas en tus controllers y handlers de error.

