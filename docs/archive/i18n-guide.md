# Gu├¡a de Internacionalizaci├│n (i18n)

## ­ƒôï ├ìndice

1. [Descripci├│n General](#descripci├│n-general)
2. [Arquitectura](#arquitectura)
3. [Configuraci├│n](#configuraci├│n)
4. [Uso B├ísico](#uso-b├ísico)
5. [Ejemplos Pr├ícticos](#ejemplos-pr├ícticos)
6. [Claves de Mensajes Disponibles](#claves-de-mensajes-disponibles)
7. [Agregar Nuevos Idiomas](#agregar-nuevos-idiomas)
8. [Mejores Pr├ícticas](#mejores-pr├ícticas)

---

## Descripci├│n General

El sistema de internacionalizaci├│n permite que la API responda en diferentes idiomas seg├║n las preferencias del cliente.

**Idiomas soportados:**

- ­ƒç¬­ƒç© Espa├▒ol (es) - **Por defecto**
- ­ƒç¼­ƒçº Ingl├®s (en)
- ­ƒçº­ƒçÀ Portugu├®s (pt)

**Caracter├¡sticas:**

- Ô£à Mensajes de ├®xito internacionalizados
- Ô£à Mensajes de error internacionalizados
- Ô£à Detecci├│n autom├ítica del idioma desde header HTTP
- Ô£à Par├ímetros din├ímicos en mensajes
- Ô£à Fallback al idioma por defecto
- Ô£à API REST stateless (sin sesiones)

---

## Arquitectura

### Componentes Principales

```
ÔöîÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÉ
Ôöé           Cliente (REST API)                     Ôöé
Ôöé  Header: Accept-Language: en, es, pt            Ôöé
ÔööÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔö¼ÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÿ
                   Ôöé
                   v
ÔöîÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÉ
Ôöé         LocaleResolver (Config)                  Ôöé
Ôöé  - AcceptHeaderLocaleResolver                    Ôöé
Ôöé  - Idiomas soportados: es, en, pt               Ôöé
ÔööÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔö¼ÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÿ
                   Ôöé
                   v
ÔöîÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÉ
Ôöé         MessageService                           Ôöé
Ôöé  - Resuelve mensajes seg├║n locale                Ôöé
Ôöé  - Soporta par├ímetros din├ímicos                  Ôöé
ÔööÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔö¼ÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÿ
                   Ôöé
                   v
ÔöîÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÉ
Ôöé     Archivos de Recursos (.properties)          Ôöé
Ôöé  - messages.properties (espa├▒ol)                 Ôöé
Ôöé  - messages_en.properties (ingl├®s)               Ôöé
Ôöé  - messages_pt.properties (portugu├®s)            Ôöé
ÔööÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÇÔöÿ
```

### Clases Clave

| Clase                | Ubicaci├│n                      | Funci├│n                            |
|----------------------|--------------------------------|------------------------------------|
| `LocaleConfig`       | `application/config/i18`       | Configura resoluci├│n de idioma     |
| `MessageService`     | `application/config/i18`       | Servicio para obtener mensajes     |
| `MessageKeys`        | `commons/constants`            | Constantes con claves de mensajes  |
| `ResponseBuilder`    | `commons/helper`               | Constructor de respuestas con i18n |
| `ErrorHandlerConfig` | `application/config/exception` | Manejo de errores con i18n         |

---

## Configuraci├│n

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

**Ubicaci├│n:** `application/src/main/resources/`

- `messages.properties` - Espa├▒ol (por defecto)
- `messages_en.properties` - Ingl├®s
- `messages_pt.properties` - Portugu├®s

**Formato:**

```properties
# Sintaxis b├ísica
clave.mensaje=Texto del mensaje
# Con par├ímetros (usar {0}, {1}, etc.)
error.not.found=No se encontr├│ el recurso con ID: {0}
error.type.mismatch=El par├ímetro ''{0}'' debe ser de tipo {1}
```

---

## Uso B├ísico

### 1. Especificar el Idioma desde el Cliente

#### Usando Header HTTP (Recomendado)

```bash
# Ingl├®s
curl -H "Accept-Language: en" http://localhost:8080/api/resource

# Espa├▒ol
curl -H "Accept-Language: es" http://localhost:8080/api/resource

# Portugu├®s
curl -H "Accept-Language: pt" http://localhost:8080/api/resource
```

#### Usando Par├ímetro Query (Alternativa)

```bash
curl http://localhost:8080/api/resource?lang=en
curl http://localhost:8080/api/resource?lang=es
curl http://localhost:8080/api/resource?lang=pt
```

### 2. Usar MessageService en tu C├│digo

```java
@Service
@RequiredArgsConstructor
public class MiServicio {
    
    private final MessageService messageService;
    
    public void miMetodo() {
        // Mensaje simple
        String msg = messageService.getMessage(MessageKeys.SUCCESS_OPERATION);
        
        // Mensaje con par├ímetros
        String errorMsg = messageService.getMessage(
            MessageKeys.ERROR_INFRASTRUCTURE_NO_REGISTRO_BY_ID, 
            "12345"
        );
        // Resultado: "No se encontr├│ ning├║n registro con el ID: 12345"
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
        
        // Respuesta exitosa (mensaje internacionalizado autom├íticamente)
        return ResponseEntity.ok(responseBuilder.success(data));
    }
    
    @PostMapping("/resource")
    public ResponseEntity<GenericResponse<MyDto>> createResource(@RequestBody MyDto dto) {
        MyDto created = service.create(dto);
        
        // Respuesta de creaci├│n (HTTP 201)
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

## Ejemplos Pr├ícticos

### Ejemplo 1: Respuesta de ├ëxito

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

**Con espa├▒ol:**

```bash
curl -H "Accept-Language: es" http://localhost:8080/api/users/1
```

```json
{
  "code": 200,
  "message": "Operaci├│n exitosa",
  "data": {
    "id": 1,
    "name": "John Doe"
  }
}
```

### Ejemplo 2: Manejo de Errores

**Request inv├ílido:**

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

### Ejemplo 3: Paginaci├│n

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
  "details": "P├ígina 1 de 5"
}
```

### Ejemplo 4: Lanzar Excepci├│n Personalizada

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

### Mensajes de ├ëxito

| Clave                | Espa├▒ol                         | Ingl├®s                               | Portugu├®s                          |
|----------------------|---------------------------------|--------------------------------------|------------------------------------|
| `SUCCESS_OPERATION`  | Operaci├│n exitosa               | Operation successful                 | Opera├º├úo bem-sucedida              |
| `SUCCESS_CREATED`    | Recurso creado exitosamente     | Resource created successfully        | Recurso criado com sucesso         |
| `SUCCESS_NO_CONTENT` | Operaci├│n exitosa sin contenido | Successful operation with no content | Opera├º├úo bem-sucedida sem conte├║do |
| `SUCCESS_PAGINATED`  | Resultados paginados            | Paginated results                    | Resultados paginados               |
| `SUCCESS_NO_RESULTS` | No se encontraron resultados    | No results found                     | Nenhum resultado encontrado        |
| `SUCCESS_PAGE_INFO`  | P├ígina {0} de {1}               | Page {0} of {1}                      | P├ígina {0} de {1}                  |

### Errores Generales

| Clave                   | Par├ímetros | Descripci├│n                |
|-------------------------|------------|----------------------------|
| `ERROR_INTERNAL_SERVER` | -          | Error interno del servidor |
| `ERROR_BAD_REQUEST`     | -          | Solicitud incorrecta       |
| `ERROR_NOT_FOUND`       | -          | Recurso no encontrado      |
| `ERROR_UNAUTHORIZED`    | -          | No autorizado              |
| `ERROR_FORBIDDEN`       | -          | Acceso prohibido           |
| `ERROR_NULL_POINTER`    | -          | Referencia nula detectada  |

### Errores de Validaci├│n

| Clave                            | Par├ímetros                 | Ejemplo                                       |
|----------------------------------|----------------------------|-----------------------------------------------|
| `ERROR_VALIDATION_PREFIX`        | -                          | "Errores de validaci├│n"                       |
| `ERROR_CONSTRAINT_VIOLATION`     | {0} errores                | "Errores de validaci├│n: name: required"       |
| `ERROR_ILLEGAL_ARGUMENT`         | {0} mensaje                | "Argumento inv├ílido: valor no permitido"      |
| `ERROR_TYPE_MISMATCH`            | {0} param, {1} tipo        | "El par├ímetro 'id' debe ser de tipo Long"     |
| `ERROR_JSON_INVALID`             | -                          | "Formato JSON inv├ílido"                       |
| `ERROR_METHOD_NOT_SUPPORTED`     | {0} m├®todo, {1} soportados | "M├®todo HTTP 'DELETE' no soportado"           |
| `ERROR_MEDIA_TYPE_NOT_SUPPORTED` | {0} tipo                   | "Tipo de contenido 'text/plain' no soportado" |
| `ERROR_PARAMETER_MISSING`        | {0} nombre                 | "Par├ímetro requerido 'id' no proporcionado"   |
| `ERROR_ENDPOINT_NOT_FOUND`       | {0} URL                    | "Endpoint '/api/invalid' no encontrado"       |

### Errores de Dominio

| Clave                              | Descripci├│n            |
|------------------------------------|------------------------|
| `ERROR_DOMAIN_VALID_ENUM`          | Valor de enum inv├ílido |
| `ERROR_DOMAIN_VALID_ID_EMPTY`      | ID vac├¡o               |
| `ERROR_DOMAIN_VALID_CONTEXTO_NULL` | Contexto vac├¡o         |
| `ERROR_DOMAIN_VALID_CREATE_EMPTY`  | Campo createBy vac├¡o   |
| `ERROR_DOMAIN_VALID_UPDATE_EMPTY`  | Campo updateBy vac├¡o   |

### Errores de Infraestructura

| Clave                                    | Par├ímetros | Ejemplo                                           |
|------------------------------------------|------------|---------------------------------------------------|
| `ERROR_INFRASTRUCTURE_NO_REGISTRO_BY_ID` | {0} ID     | "No se encontr├│ ning├║n registro con el ID: 12345" |

### Errores de Base de Datos

| Clave                  | Descripci├│n                      |
|------------------------|----------------------------------|
| `ERROR_DATA_INTEGRITY` | Violaci├│n de integridad de datos |
| `ERROR_FK_CONSTRAINT`  | Restricci├│n de clave for├ínea     |

---

## Agregar Nuevos Idiomas

### Paso 1: Crear Archivo de Properties

Crear archivo `messages_fr.properties` en `application/src/main/resources/`:

```properties
# MENSAJES DE ├ëXITO
success.operation = Op├®ration r├®ussie
success.created = Ressource cr├®├®e avec succ├¿s
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
            Locale.forLanguageTag("fr")  // ÔåÉ Agregar nuevo idioma
    ));
    return localeResolver;
}
```

### Paso 3: Probar

```bash
curl -H "Accept-Language: fr" http://localhost:8080/api/resource
```

---

## Mejores Pr├ícticas

### Ô£à DO

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

3. **Usar par├ímetros para mensajes din├ímicos**
   ```java
   messageService.getMessage(MessageKeys.ERROR_NOT_FOUND, "Usuario", userId);
   ```

4. **Mantener sincronizados todos los archivos de idiomas**
    - Asegurar que todas las claves existan en todos los idiomas

5. **Usar UTF-8 en archivos properties**
    - Ya configurado en `messageSource.setDefaultEncoding("UTF-8")`

### ÔØî DON'T

1. **No hardcodear mensajes en el c├│digo**
   ```java
   // ÔØî MAL
   return ResponseEntity.ok("Operaci├│n exitosa");
   
   // Ô£à BIEN
   return ResponseEntity.ok(
       responseBuilder.success(data)
   );
   ```

2. **No usar strings m├ígicos**
   ```java
   // ÔØî MAL
   messageService.getMessage("success.operation");
   
   // Ô£à BIEN
   messageService.getMessage(MessageKeys.SUCCESS_OPERATION);
   ```

3. **No concatenar mensajes manualmente**
   ```java
   // ÔØî MAL
   String msg = "Error con ID: " + id;
   
   // Ô£à BIEN
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
        assertEquals("Operaci├│n exitosa", msg);
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
        assertEquals("No se encontr├│ ning├║n registro con el ID: 12345", msg);
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
                .andExpect(jsonPath("$.message").value("Operaci├│n exitosa"));
    }
}
```

---

## Troubleshooting

### Problema: Los mensajes aparecen en ingl├®s en lugar de espa├▒ol

**Causa:** El cliente no est├í enviando el header `Accept-Language` o est├í configurado incorrectamente.

**Soluci├│n:**

```bash
# Asegurar que el header est├® presente
curl -H "Accept-Language: es" http://localhost:8080/api/resource
```

### Problema: Aparece la clave en lugar del mensaje

**Causa:** La clave no existe en el archivo de properties del idioma solicitado.

**Soluci├│n:** Verificar que la clave est├® presente en todos los archivos:

- `messages.properties`
- `messages_en.properties`
- `messages_pt.properties`

### Problema: Caracteres especiales aparecen mal codificados

**Causa:** El archivo properties no est├í en UTF-8.

**Soluci├│n:**

1. Asegurar que el archivo est├® guardado en UTF-8
2. Verificar que `messageSource.setDefaultEncoding("UTF-8")` est├® configurado

---

## Resumen

Ô£à **Sistema completamente implementado**

- MessageService para gesti├│n de mensajes
- MessageKeys con todas las constantes
- ResponseBuilder internacionalizado
- ErrorHandlerConfig internacionalizado
- Archivos de properties para es, en, pt

Ô£à **Listo para usar**

- Inyectar `MessageService` o `ResponseBuilder`
- Usar constantes de `MessageKeys`
- Especificar idioma con header `Accept-Language`

Ô£à **Extensible**

- Agregar nuevos idiomas f├ícilmente
- Agregar nuevas claves de mensajes
- Personalizar mensajes por proyecto

---

**Autor:** Sistema de Internacionalizaci├│n  
**Fecha:** 2025  
**Versi├│n:** 1.0

