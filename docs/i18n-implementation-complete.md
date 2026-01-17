# Internacionalización (i18n) - Implementación Completa

## 📋 Resumen de Cambios Realizados

### 1. **Renombrado de Clase para Evitar Conflictos**
- ✅ **Clase renombrada**: `ResponseBuilder` → `ApiResponseBuilder`
- ✅ **Motivo**: Conflicto con el bean `responseBuilder` de SpringDoc
- ✅ **Ubicación**: `commons/src/main/java/com/empresa/plantilla/commons/helper/ApiResponseBuilder.java`
- ✅ **Referencias actualizadas** en `ErrorHandlerConfig`

### 2. **Configuración de LocaleConfig Mejorada**
- ✅ **Implementación de `WebMvcConfigurer`**: Permite registrar interceptores
- ✅ **Registro del `LocaleChangeInterceptor`**: Ahora funciona correctamente
- ✅ **MessageSource mejorado**: Agregado `setUseCodeAsDefaultMessage(true)` para evitar errores si falta una clave

## 🎯 Funcionalidades Implementadas

### 1. **Soporte Multi-idioma**
Tu API ahora soporta **3 idiomas**:
- 🇪🇸 **Español** (predeterminado)
- 🇬🇧 **Inglés**
- 🇧🇷 **Portugués**

### 2. **Dos Formas de Cambiar el Idioma**

#### **Opción 1: Header Accept-Language (Recomendado para APIs REST)**
```http
GET /api/recurso
Accept-Language: en
```

#### **Opción 2: Parámetro de Query**
```http
GET /api/recurso?lang=en
```

### 3. **Mensajes Internacionalizados Disponibles**

#### **Mensajes de Éxito**
```properties
success.operation
success.created
success.no.content
success.paginated
success.no.results
success.page.info
```

#### **Errores Generales**
```properties
error.internal.server
error.bad.request
error.not.found
error.unauthorized
error.forbidden
error.null.pointer
```

#### **Errores de Validación**
```properties
error.validation.prefix
error.constraint.violation
error.illegal.argument
error.type.mismatch
error.json.invalid
error.method.not.supported
error.media.type.not.supported
error.parameter.missing
error.endpoint.not.found
```

#### **Errores de Base de Datos**
```properties
error.data.integrity
error.fk.constraint
```

#### **Errores de Dominio**
```properties
error.domain.valid.enum
error.domain.valid.id.empty
error.domain.valid.contexto.null
error.domain.valid.create.empty
error.domain.valid.update.empty
```

#### **Errores de Infraestructura**
```properties
error.infrastructure.no.registro.by.id
```

## 🛠️ Cómo Usar

### **1. Usando ApiResponseBuilder (Recomendado)**

```java
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final ApiResponseBuilder responseBuilder;
    private final UsuarioService usuarioService;

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<Usuario>> obtenerUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(responseBuilder.success(usuario));
    }

    @GetMapping
    public ResponseEntity<GenericResponse<Usuario>> listarUsuarios(
            @PageableDefault Pageable pageable) {
        Page<Usuario> usuarios = usuarioService.listar(pageable);
        IPageableResult<Usuario> pageableResult = // convertir Page a IPageableResult
        return ResponseEntity.ok(responseBuilder.paginated(pageableResult));
    }

    @PostMapping
    public ResponseEntity<GenericResponse<Usuario>> crearUsuario(@Valid @RequestBody UsuarioDTO dto) {
        Usuario usuario = usuarioService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseBuilder.created(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<Void>> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.ok(responseBuilder.noContent());
    }
}
```

### **2. Usando MessageService Directamente**

```java
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final MessageService messageService;

    public void validar(Usuario usuario) {
        if (usuario.getEmail() == null) {
            throw new DomainException(
                messageService.getMessage(MessageKeys.ERROR_DOMAIN_VALID_ID_EMPTY)
            );
        }
    }

    public void enviarNotificacion(String email) {
        String mensaje = messageService.getMessage(
            "notificacion.bienvenida", 
            usuario.getNombre()
        );
        // enviar email...
    }
}
```

### **3. Manejo Automático de Excepciones**

El `ErrorHandlerConfig` ya maneja automáticamente todas las excepciones y devuelve respuestas internacionalizadas:

```java
@Service
public class ProductoService {

    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
            .orElseThrow(() -> new InfrastructureException(
                messageService.getMessage(
                    MessageKeys.ERROR_INFRASTRUCTURE_NO_REGISTRO_BY_ID, 
                    id
                )
            ));
    }
}
```

## 📝 Agregar Nuevos Mensajes

### **1. Agregar la clave en MessageKeys.java**
```java
public class MessageKeys {
    // ...existing keys...
    public static final String PRODUCTO_CREADO = "producto.creado";
    public static final String PRODUCTO_ACTUALIZADO = "producto.actualizado";
}
```

### **2. Agregar en los archivos de propiedades**

**messages.properties** (español):
```properties
producto.creado = Producto creado exitosamente
producto.actualizado = Producto actualizado: {0}
```

**messages_en.properties** (inglés):
```properties
producto.creado = Product created successfully
producto.actualizado = Product updated: {0}
```

**messages_pt.properties** (portugués):
```properties
produto.creado = Produto criado com sucesso
produto.actualizado = Produto atualizado: {0}
```

### **3. Usar el nuevo mensaje**
```java
String mensaje = messageService.getMessage(MessageKeys.PRODUCTO_CREADO);
String mensajeConParam = messageService.getMessage(
    MessageKeys.PRODUCTO_ACTUALIZADO, 
    producto.getNombre()
);
```

## 🧪 Ejemplos de Respuestas

### **Respuesta Exitosa en Español**
```http
GET /api/usuarios/1
Accept-Language: es
```
```json
{
  "errorCode": 200,
  "mensaje": "Operación exitosa",
  "obj": {
    "id": 1,
    "nombre": "Juan Pérez",
    "email": "juan@example.com"
  }
}
```

### **Respuesta Exitosa en Inglés**
```http
GET /api/usuarios/1
Accept-Language: en
```
```json
{
  "errorCode": 200,
  "mensaje": "Operation successful",
  "obj": {
    "id": 1,
    "nombre": "Juan Pérez",
    "email": "juan@example.com"
  }
}
```

### **Error de Validación en Portugués**
```http
POST /api/usuarios
Accept-Language: pt
Content-Type: application/json

{
  "email": "invalid-email"
}
```
```json
{
  "errorCode": 400,
  "mensaje": "Erros de validação: O e-mail deve ser válido",
  "obj": null
}
```

## 📂 Estructura de Archivos

```
application/
├── src/main/resources/
│   ├── messages.properties          # Mensajes en español (predeterminado)
│   ├── messages_en.properties       # Mensajes en inglés
│   └── messages_pt.properties       # Mensajes en portugués
└── src/main/java/
    └── com/empresa/plantilla/application/config/
        ├── i18/
        │   └── LocaleConfig.java    # Configuración i18n
        └── exception/
            └── ErrorHandlerConfig.java  # Manejo de errores internacionalizados

commons/
└── src/main/java/
    └── com/empresa/plantilla/commons/
        ├── constants/
        │   └── MessageKeys.java     # Constantes de claves de mensajes
        ├── helper/
        │   └── ApiResponseBuilder.java  # Constructor de respuestas
        └── services/i18/
            └── MessageService.java  # Servicio de mensajes
```

## ✅ Verificación de la Implementación

### **Estado de Componentes**
- ✅ `LocaleConfig` - Configurado correctamente con `WebMvcConfigurer`
- ✅ `ApiResponseBuilder` - Renombrado y funcionando
- ✅ `ErrorHandlerConfig` - Actualizado con nuevas referencias
- ✅ `MessageService` - Operativo
- ✅ Archivos de propiedades - 3 idiomas configurados
- ✅ Compilación - Exitosa (sin errores)

### **Funcionalidades Listas**
- ✅ Cambio de idioma por header `Accept-Language`
- ✅ Cambio de idioma por parámetro `?lang=`
- ✅ Respuestas automáticas internacionalizadas
- ✅ Manejo de errores internacionalizado
- ✅ Mensajes parametrizados (con {0}, {1}, etc.)
- ✅ Fallback a español si falta un idioma

## 🚀 Próximos Pasos (Opcional)

1. **Agregar más idiomas**: Simplemente crea `messages_fr.properties`, `messages_de.properties`, etc.
2. **Validaciones personalizadas**: Crea mensajes específicos para tus entidades de dominio
3. **Mensajes de negocio**: Agrega mensajes específicos de tu lógica de negocio
4. **Testing i18n**: Crear tests para verificar los mensajes en diferentes idiomas

## 📚 Referencias

- [Spring i18n Documentation](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-servlet/localeresolver.html)
- [MessageSource JavaDoc](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/MessageSource.html)
- [Accept-Language Header](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept-Language)

---

**Fecha de implementación**: 2026-01-17  
**Estado**: ✅ Completo y funcional  
**Autor**: GitHub Copilot
