# Resumen de Simplificación de Clases de Respuesta

## Cambios Realizados

### 1. GenericResponse Unificado ✅

Se simplificó y unificó la clase `GenericResponse<T>` para manejar todos los tipos de respuesta:

**Antes** (3 clases):
- `GenericResponse<T>` con `BodyResponse<T>`
- `BodyResponse<T>` como contenedor interno
- `SimpleResponse<T>` como alternativa

**Ahora** (1 clase):
- `GenericResponse<T>` unificado con:
  - `dato`: Para objeto único
  - `datos`: Para lista de objetos
  - `conteo`: Para paginación
  - `totales`: Para metadatos

### 2. ResponseBuilder Helper ✅

Se creó una clase utilitaria `ResponseBuilder` con métodos estáticos convenientes:

#### Métodos de Éxito
```java
ResponseBuilder.success(obj)
ResponseBuilder.success(obj, mensaje)
ResponseBuilder.successList(list)
ResponseBuilder.successList(list, mensaje)
ResponseBuilder.created(obj)
ResponseBuilder.noContent()
```

#### Métodos de Paginación
```java
ResponseBuilder.paginated(pageableResult)
ResponseBuilder.paginated(pageableResult, mensaje)
ResponseBuilder.paginatedFromList(list, pageNumber, pageSize)
```

#### Métodos de Error
```java
ResponseBuilder.error(mensaje)
ResponseBuilder.error(exception, errorCode)
ResponseBuilder.badRequest(mensaje)
ResponseBuilder.notFound(mensaje)
ResponseBuilder.unauthorized(mensaje)
ResponseBuilder.forbidden(mensaje)
```

### 3. Estructura del Proyecto

```
commons/
├── src/main/java/com/empresa/plantilla/commons/
│   ├── dto/response/
│   │   ├── GenericResponse.java       ✅ Unificado y simplificado
│   │   ├── BodyResponse.java          ⚠️  @Deprecated
│   │   └── SimpleResponse.java        ⚠️  @Deprecated
│   └── helper/
│       └── ResponseBuilder.java       ✅ Nuevo helper
└── docs/
    └── generic-response-guide.md      ✅ Documentación completa
```

## Comparación de Código

### Antes - Respuesta Simple
```java
SimpleResponse<User> response = SimpleResponse.<User>builder()
        .ok(true)
        .codigo(200)
        .mensaje("Usuario encontrado")
        .cuerpo(user)
        .build();
```

### Ahora - Respuesta Simple
```java
GenericResponse<User> response = ResponseBuilder.success(user, "Usuario encontrado");
```

**Reducción: ~75%**

---

### Antes - Respuesta Paginada
```java
GenericResponse<User> response = GenericResponse.<User>builder()
        .ok(true)
        .codigo(200)
        .mensaje("Usuarios paginados")
        .cuerpo(BodyResponse.<User>builder()
                .conteo(pageableResult.getTotalElements().intValue())
                .datos(pageableResult.getContent())
                .totales("Página 1 de 10")
                .build())
        .build();
```

### Ahora - Respuesta Paginada
```java
GenericResponse<User> response = ResponseBuilder.paginated(pageableResult);
```

**Reducción: ~85%**

## Ventajas de la Simplificación

### 1. Menos Clases
- ✅ De 3 clases a 1 clase principal
- ✅ 1 helper para facilitar el uso
- ✅ Menor complejidad

### 2. Código Más Limpio
- ✅ Menos verbosidad
- ✅ Métodos factory convenientes
- ✅ API más intuitiva

### 3. Mantenibilidad
- ✅ Cambios centralizados
- ✅ Menos duplicación
- ✅ Más fácil de entender

### 4. Flexibilidad
- ✅ Una clase para todos los casos
- ✅ `@JsonInclude(NON_NULL)` omite campos vacíos
- ✅ Type-safe con genéricos

### 5. Consistencia
- ✅ Misma estructura en toda la API
- ✅ Códigos HTTP estandarizados
- ✅ Mensajes consistentes

## Ejemplos de Uso

### En Controllers

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<UserDto>> getUser(@PathVariable Long id) {
        UserDto user = userService.findById(id);
        return ResponseEntity.ok(ResponseBuilder.success(user));
    }

    @GetMapping
    public ResponseEntity<GenericResponse<UserDto>> getAllUsers() {
        List<UserDto> users = userService.findAll();
        return ResponseEntity.ok(ResponseBuilder.successList(users));
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
                .body(ResponseBuilder.created(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(ResponseBuilder.noContent());
    }
}
```

### En Error Handlers

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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<Void>> handleGenericException(Exception ex) {
        log.error("Error inesperado", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseBuilder.error(ex));
    }
}
```

## Respuestas JSON

### Respuesta Simple
```json
{
  "ok": true,
  "codigo": 200,
  "mensaje": "Usuario encontrado",
  "dato": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com"
  }
}
```

### Respuesta con Lista
```json
{
  "ok": true,
  "codigo": 200,
  "mensaje": "Usuarios encontrados",
  "datos": [
    {"id": 1, "name": "John"},
    {"id": 2, "name": "Jane"}
  ],
  "conteo": 2
}
```

### Respuesta Paginada
```json
{
  "ok": true,
  "codigo": 200,
  "mensaje": "Resultados paginados",
  "datos": [...],
  "conteo": 100,
  "totales": "Página 1 de 10"
}
```

### Respuesta de Error
```json
{
  "ok": false,
  "codigo": 404,
  "mensaje": "Usuario no encontrado"
}
```

## Migración

### Clases Deprecadas

Las siguientes clases están marcadas como `@Deprecated` y se eliminarán en versiones futuras:

- `BodyResponse<T>` → Usar `GenericResponse<T>` directamente
- `SimpleResponse<T>` → Usar `GenericResponse<T>` directamente

### Guía de Migración

**De SimpleResponse a ResponseBuilder:**
```java
// Antes
SimpleResponse<User> response = SimpleResponse.<User>builder()
        .ok(true)
        .codigo(200)
        .mensaje("OK")
        .cuerpo(user)
        .build();

// Ahora
GenericResponse<User> response = ResponseBuilder.success(user, "OK");
```

**De GenericResponse + BodyResponse a ResponseBuilder:**
```java
// Antes
GenericResponse<User> response = GenericResponse.<User>builder()
        .ok(true)
        .codigo(200)
        .mensaje("OK")
        .cuerpo(BodyResponse.<User>builder()
                .conteo(users.size())
                .datos(users)
                .build())
        .build();

// Ahora
GenericResponse<User> response = ResponseBuilder.successList(users, "OK");
```

## Documentación

Para más detalles, consulta:
- `docs/generic-response-guide.md` - Guía completa de uso
- `docs/transactional-guide.md` - Guía de transacciones

## Resumen de Archivos Creados/Modificados

### Creados ✨
- `commons/src/main/java/com/empresa/plantilla/commons/helper/ResponseBuilder.java`
- `docs/generic-response-guide.md`
- `docs/simplification-summary.md` (este archivo)

### Modificados ✏️
- `commons/src/main/java/com/empresa/plantilla/commons/dto/response/GenericResponse.java`
- `commons/src/main/java/com/empresa/plantilla/commons/dto/response/BodyResponse.java` (@Deprecated)
- `commons/src/main/java/com/empresa/plantilla/commons/dto/response/SimpleResponse.java` (@Deprecated)
- `commons/build.gradle` (agregadas dependencias de Jackson, Spring Web, Mockito)

## Conclusión

La simplificación reduce significativamente la complejidad del código:

- ✅ **Menos clases**: 3 → 1
- ✅ **Menos líneas de código**: Reducción promedio del 80%
- ✅ **Más legible**: API clara e intuitiva
- ✅ **Más mantenible**: Cambios centralizados
- ✅ **Mejor documentado**: Guías completas

**Recomendación**: Usa siempre `ResponseBuilder` para crear respuestas en tu API.

