# Guía de Internacionalización (i18n)

## 📋 Índice

1. [Descripción General](#descripción-general)
2. [Arquitectura](#arquitectura)
3. [Configuración](#configuración)
4. [Uso Básico](#uso-básico)
5. [Ejemplos Prácticos](#ejemplos-prácticos)
6. [Claves de Mensajes Disponibles](#claves-de-mensajes-disponibles)
7. [Agregar Nuevos Idiomas](#agregar-nuevos-idiomas)
8. [Mejores Prácticas](#mejores-prácticas)

---

## Descripción General

El sistema de internacionalización permite que la API responda en diferentes idiomas según las preferencias del cliente.

**Idiomas soportados:**

- 🇪🇸 Español (es) - **Por defecto**
- 🇬🇧 Inglés (en)
- 🇧🇷 Portugués (pt)

**Características:**

- ✅ Mensajes de éxito internacionalizados
- ✅ Mensajes de error internacionalizados
- ✅ Detección automática del idioma desde header HTTP
- ✅ Parámetros dinámicos en mensajes
- ✅ Fallback al idioma por defecto
- ✅ API REST stateless (sin sesiones)

---

## Arquitectura

### Componentes Principales

```
┌─────────────────────────────────────────────────┐
│           Cliente (REST API)                     │
│  Header: Accept-Language: en, es, pt            │
└──────────────────┬──────────────────────────────┘
                   │
                   v
┌─────────────────────────────────────────────────┐
│         LocaleResolver (Config)                  │
│  - AcceptHeaderLocaleResolver                    │
│  - Idiomas soportados: es, en, pt               │
└──────────────────┬──────────────────────────────┘
                   │
                   v
┌─────────────────────────────────────────────────┐
│         MessageService                           │
│  - Resuelve mensajes según locale                │
│  - Soporta parámetros dinámicos                  │
└──────────────────┬──────────────────────────────┘
                   │
                   v
┌─────────────────────────────────────────────────┐
│     Archivos de Recursos (.properties)          │
│  - messages.properties (español)                 │
│  - messages_en.properties (inglés)               │
│  - messages_pt.properties (portugués)            │
└─────────────────────────────────────────────────┘
```

### Clases Clave

| Clase                | Ubicación                      | Función                            |
|----------------------|--------------------------------|------------------------------------|
| `LocaleConfig`       | `application/config/i18`       | Configura resolución de idioma     |
| `MessageService`     | `application/config/i18`       | Servicio para obtener mensajes     |
| `MessageKeys`        | `commons/constants`            | Constantes con claves de mensajes  |
| `ResponseBuilder`    | `commons/helper`               | Constructor de respuestas con i18n |
| `ErrorHandlerConfig` | `application/config/exception` | Manejo de errores con i18n         |

---

## Configuración

### 1. LocaleConfig

```java
@Configuration
public class LocaleConfig {
    
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(Locale.forLanguageTag("es"));
        resolver.setSupportedLocales(Arrays.asList(
            Locale.forLanguageTag("es"),
            Locale.forLanguageTag("en"),
            Locale.forLanguageTag("pt")
        ));
        return resolver;
    }
    
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("messages");
        source.setDefaultEncoding("UTF-8");
        return source;
    }
}
```

### 2. Archivos de Propiedades

**Ubicación:** `application/src/main/resources/`

- `messages.properties` - Español (por defecto)
- `messages_en.properties` - Inglés
- `messages_pt.properties` - Portugués

**Formato:**

```properties
# Sintaxis básica
clave.mensaje = Texto del mensaje

# Con parámetros (usar {0}, {1}, etc.)
error.not.found = No se encontró el recurso con ID: {0}
error.type.mismatch = El parámetro ''{0}'' debe ser de tipo {1}
```

---

## Uso Básico

### 1. Especificar el Idioma desde el Cliente

#### Usando Header HTTP (Recomendado)

```bash
# Inglés
curl -H "Accept-Language: en" http://localhost:8080/api/resource

# Español
curl -H "Accept-Language: es" http://localhost:8080/api/resource

# Portugués
curl -H "Accept-Language: pt" http://localhost:8080/api/resource
```

#### Usando Parámetro Query (Alternativa)

```bash
curl http://localhost:8080/api/resource?lang=en
curl http://localhost:8080/api/resource?lang=es
curl http://localhost:8080/api/resource?lang=pt
```

### 2. Usar MessageService en tu Código

```java
@Service
@RequiredArgsConstructor
public class MiServicio {
    
    private final MessageService messageService;
    
    public void miMetodo() {
        // Mensaje simple
        String msg = messageService.getMessage(MessageKeys.SUCCESS_OPERATION);
        
        // Mensaje con parámetros
        String errorMsg = messageService.getMessage(
            MessageKeys.ERROR_INFRASTRUCTURE_NO_REGISTRO_BY_ID, 
            "12345"
        );
        // Resultado: "No se encontró ningún registro con el ID: 12345"
    }
}
```

### 3. Usar ResponseBuilder

```java
@RestController
public class MiController {
    
    private final ResponseBuilder responseBuilder;
    
    // IMPORTANTE: Usar @Qualifier para evitar conflicto con SpringDoc
    public MiController(@Qualifier("apiResponseBuilder") ResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }
    
    @GetMapping("/resource/{id}")
    public ResponseEntity<GenericResponse<MyDto>> getResource(@PathVariable Long id) {
        MyDto data = service.findById(id);
        
        // Respuesta exitosa (mensaje internacionalizado automáticamente)
        return ResponseEntity.ok(responseBuilder.success(data));
    }
    
    @PostMapping("/resource")
    public ResponseEntity<GenericResponse<MyDto>> createResource(@RequestBody MyDto dto) {
        MyDto created = service.create(dto);
        
        // Respuesta de creación (HTTP 201)
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(responseBuilder.created(created));
    }
    
    @GetMapping("/resources")
    public ResponseEntity<GenericResponse<MyDto>> listResources(Pageable pageable) {
        IPageableResult<MyDto> result = service.findAll(pageable);
        
        // Respuesta paginada
        return ResponseEntity.ok(responseBuilder.paginated(result));
    }
}
```

---

## Ejemplos Prácticos

### Ejemplo 1: Respuesta de Éxito

**Request:**

```bash
curl -H "Accept-Language: en" http://localhost:8080/api/users/1
```

**Response:**

```json
{
  "code": 200,
  "message": "Operation successful",
  "data": {
    "id": 1,
    "name": "John Doe"
  }
}
```

**Con español:**

```bash
curl -H "Accept-Language: es" http://localhost:8080/api/users/1
```

```json
{
  "code": 200,
  "message": "Operación exitosa",
  "data": {
    "id": 1,
    "name": "John Doe"
  }
}
```

### Ejemplo 2: Manejo de Errores

**Request inválido:**

```bash
curl -H "Accept-Language: en" -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": ""}'
```

**Response:**

```json
{
  "code": 400,
  "message": "Validation errors: name: must not be empty"
}
```

### Ejemplo 3: Paginación

**Request:**

```bash
curl -H "Accept-Language: pt" http://localhost:8080/api/users?page=0&size=10
```

**Response:**

```json
{
  "code": 200,
  "message": "Resultados paginados",
  "data": [...],
  "totalElements": 50,
  "details": "Página 1 de 5"
}
```

### Ejemplo 4: Lanzar Excepción Personalizada

```java
@Service
public class UserService {
    
    private final MessageService messageService;
    
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new InfrastructureException(
                messageService.getMessage(
                    MessageKeys.ERROR_INFRASTRUCTURE_NO_REGISTRO_BY_ID,
                    id
                )
            ));
    }
}
```

---

## Claves de Mensajes Disponibles

### Mensajes de Éxito

| Clave                | Español                         | Inglés                               | Portugués                          |
|----------------------|---------------------------------|--------------------------------------|------------------------------------|
| `SUCCESS_OPERATION`  | Operación exitosa               | Operation successful                 | Operação bem-sucedida              |
| `SUCCESS_CREATED`    | Recurso creado exitosamente     | Resource created successfully        | Recurso criado com sucesso         |
| `SUCCESS_NO_CONTENT` | Operación exitosa sin contenido | Successful operation with no content | Operação bem-sucedida sem conteúdo |
| `SUCCESS_PAGINATED`  | Resultados paginados            | Paginated results                    | Resultados paginados               |
| `SUCCESS_NO_RESULTS` | No se encontraron resultados    | No results found                     | Nenhum resultado encontrado        |
| `SUCCESS_PAGE_INFO`  | Página {0} de {1}               | Page {0} of {1}                      | Página {0} de {1}                  |

### Errores Generales

| Clave                   | Parámetros | Descripción                |
|-------------------------|------------|----------------------------|
| `ERROR_INTERNAL_SERVER` | -          | Error interno del servidor |
| `ERROR_BAD_REQUEST`     | -          | Solicitud incorrecta       |
| `ERROR_NOT_FOUND`       | -          | Recurso no encontrado      |
| `ERROR_UNAUTHORIZED`    | -          | No autorizado              |
| `ERROR_FORBIDDEN`       | -          | Acceso prohibido           |
| `ERROR_NULL_POINTER`    | -          | Referencia nula detectada  |

### Errores de Validación

| Clave                            | Parámetros                 | Ejemplo                                       |
|----------------------------------|----------------------------|-----------------------------------------------|
| `ERROR_VALIDATION_PREFIX`        | -                          | "Errores de validación"                       |
| `ERROR_CONSTRAINT_VIOLATION`     | {0} errores                | "Errores de validación: name: required"       |
| `ERROR_ILLEGAL_ARGUMENT`         | {0} mensaje                | "Argumento inválido: valor no permitido"      |
| `ERROR_TYPE_MISMATCH`            | {0} param, {1} tipo        | "El parámetro 'id' debe ser de tipo Long"     |
| `ERROR_JSON_INVALID`             | -                          | "Formato JSON inválido"                       |
| `ERROR_METHOD_NOT_SUPPORTED`     | {0} método, {1} soportados | "Método HTTP 'DELETE' no soportado"           |
| `ERROR_MEDIA_TYPE_NOT_SUPPORTED` | {0} tipo                   | "Tipo de contenido 'text/plain' no soportado" |
| `ERROR_PARAMETER_MISSING`        | {0} nombre                 | "Parámetro requerido 'id' no proporcionado"   |
| `ERROR_ENDPOINT_NOT_FOUND`       | {0} URL                    | "Endpoint '/api/invalid' no encontrado"       |

### Errores de Dominio

| Clave                              | Descripción            |
|------------------------------------|------------------------|
| `ERROR_DOMAIN_VALID_ENUM`          | Valor de enum inválido |
| `ERROR_DOMAIN_VALID_ID_EMPTY`      | ID vacío               |
| `ERROR_DOMAIN_VALID_CONTEXTO_NULL` | Contexto vacío         |
| `ERROR_DOMAIN_VALID_CREATE_EMPTY`  | Campo createBy vacío   |
| `ERROR_DOMAIN_VALID_UPDATE_EMPTY`  | Campo updateBy vacío   |

### Errores de Infraestructura

| Clave                                    | Parámetros | Ejemplo                                           |
|------------------------------------------|------------|---------------------------------------------------|
| `ERROR_INFRASTRUCTURE_NO_REGISTRO_BY_ID` | {0} ID     | "No se encontró ningún registro con el ID: 12345" |

### Errores de Base de Datos

| Clave                  | Descripción                      |
|------------------------|----------------------------------|
| `ERROR_DATA_INTEGRITY` | Violación de integridad de datos |
| `ERROR_FK_CONSTRAINT`  | Restricción de clave foránea     |

---

## Agregar Nuevos Idiomas

### Paso 1: Crear Archivo de Properties

Crear archivo `messages_fr.properties` en `application/src/main/resources/`:

```properties
# MENSAJES DE ÉXITO
success.operation = Opération réussie
success.created = Ressource créée avec succès
# ... resto de mensajes
```

### Paso 2: Actualizar LocaleConfig

```java
@Bean
public LocaleResolver localeResolver() {
    AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
    localeResolver.setDefaultLocale(Locale.forLanguageTag("es"));
    localeResolver.setSupportedLocales(Arrays.asList(
            Locale.forLanguageTag("es"),
            Locale.forLanguageTag("en"),
            Locale.forLanguageTag("pt"),
            Locale.forLanguageTag("fr")  // ← Agregar nuevo idioma
    ));
    return localeResolver;
}
```

### Paso 3: Probar

```bash
curl -H "Accept-Language: fr" http://localhost:8080/api/resource
```

---

## Mejores Prácticas

### ✅ DO

1. **Usar constantes de MessageKeys**
   ```java
   messageService.getMessage(MessageKeys.SUCCESS_OPERATION);
   ```

2. **Inyectar ResponseBuilder para respuestas**
   ```java
   @RequiredArgsConstructor
   public class MiController {
       private final ResponseBuilder responseBuilder;
   }
   ```

3. **Usar parámetros para mensajes dinámicos**
   ```java
   messageService.getMessage(MessageKeys.ERROR_NOT_FOUND, "Usuario", userId);
   ```

4. **Mantener sincronizados todos los archivos de idiomas**
    - Asegurar que todas las claves existan en todos los idiomas

5. **Usar UTF-8 en archivos properties**
    - Ya configurado en `messageSource.setDefaultEncoding("UTF-8")`

### ❌ DON'T

1. **No hardcodear mensajes en el código**
   ```java
   // ❌ MAL
   return ResponseEntity.ok("Operación exitosa");
   
   // ✅ BIEN
   return ResponseEntity.ok(
       responseBuilder.success(data)
   );
   ```

2. **No usar strings mágicos**
   ```java
   // ❌ MAL
   messageService.getMessage("success.operation");
   
   // ✅ BIEN
   messageService.getMessage(MessageKeys.SUCCESS_OPERATION);
   ```

3. **No concatenar mensajes manualmente**
   ```java
   // ❌ MAL
   String msg = "Error con ID: " + id;
   
   // ✅ BIEN
   String msg = messageService.getMessage(
       MessageKeys.ERROR_INFRASTRUCTURE_NO_REGISTRO_BY_ID, 
       id
   );
   ```

4. **No depender de sesiones HTTP**
    - El sistema usa `AcceptHeaderLocaleResolver` (stateless)
    - Cada request debe incluir `Accept-Language` header

---

## Testing

### Test de MessageService

```java
@SpringBootTest
class MessageServiceTest {
    
    @Autowired
    private MessageService messageService;
    
    @Test
    void testGetMessage_Spanish() {
        LocaleContextHolder.setLocale(Locale.forLanguageTag("es"));
        String msg = messageService.getMessage(MessageKeys.SUCCESS_OPERATION);
        assertEquals("Operación exitosa", msg);
    }
    
    @Test
    void testGetMessage_English() {
        LocaleContextHolder.setLocale(Locale.forLanguageTag("en"));
        String msg = messageService.getMessage(MessageKeys.SUCCESS_OPERATION);
        assertEquals("Operation successful", msg);
    }
    
    @Test
    void testGetMessage_WithParams() {
        LocaleContextHolder.setLocale(Locale.forLanguageTag("es"));
        String msg = messageService.getMessage(
            MessageKeys.ERROR_INFRASTRUCTURE_NO_REGISTRO_BY_ID, 
            "12345"
        );
        assertEquals("No se encontró ningún registro con el ID: 12345", msg);
    }
}
```

### Test de Controller con i18n

```java
@WebMvcTest(UserController.class)
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testGetUser_English() throws Exception {
        mockMvc.perform(get("/api/users/1")
                .header("Accept-Language", "en"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Operation successful"));
    }
    
    @Test
    void testGetUser_Spanish() throws Exception {
        mockMvc.perform(get("/api/users/1")
                .header("Accept-Language", "es"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Operación exitosa"));
    }
}
```

---

## Troubleshooting

### Problema: Los mensajes aparecen en inglés en lugar de español

**Causa:** El cliente no está enviando el header `Accept-Language` o está configurado incorrectamente.

**Solución:**

```bash
# Asegurar que el header esté presente
curl -H "Accept-Language: es" http://localhost:8080/api/resource
```

### Problema: Aparece la clave en lugar del mensaje

**Causa:** La clave no existe en el archivo de properties del idioma solicitado.

**Solución:** Verificar que la clave esté presente en todos los archivos:

- `messages.properties`
- `messages_en.properties`
- `messages_pt.properties`

### Problema: Caracteres especiales aparecen mal codificados

**Causa:** El archivo properties no está en UTF-8.

**Solución:**

1. Asegurar que el archivo esté guardado en UTF-8
2. Verificar que `messageSource.setDefaultEncoding("UTF-8")` esté configurado

---

## Resumen

✅ **Sistema completamente implementado**

- MessageService para gestión de mensajes
- MessageKeys con todas las constantes
- ResponseBuilder internacionalizado
- ErrorHandlerConfig internacionalizado
- Archivos de properties para es, en, pt

✅ **Listo para usar**

- Inyectar `MessageService` o `ResponseBuilder`
- Usar constantes de `MessageKeys`
- Especificar idioma con header `Accept-Language`

✅ **Extensible**

- Agregar nuevos idiomas fácilmente
- Agregar nuevas claves de mensajes
- Personalizar mensajes por proyecto

---

**Autor:** Sistema de Internacionalización  
**Fecha:** 2025  
**Versión:** 1.0

