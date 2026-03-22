# BCK-Plantilla - Microservicio Base

Plantilla base para microservicios Spring Boot con arquitectura hexagonal y mejores practicas.

## Documentacion de API

### Acceso a Swagger UI

**Swagger UI (Interfaz Interactiva)**

```
http://localhost:8080/swagger-ui.html
```

**OpenAPI JSON**

```
http://localhost:8080/v3/api-docs
```

### Autenticacion en Swagger

1. Obtener Token: Ejecutar `POST /api/auth/login`
2. Autorizar: Clic en boton "Authorize"
3. Ingresar Token: `Bearer {tu-token}`
4. Probar Endpoints protegidos

### Internacionalizacion

Incluye el header `Accept-Language`:

- `es` - Español (por defecto)
- `en` - English
- `pt` - Português

## Inicio Rapido

```bash
./gradlew bootRun
```

## Guias de Desarrollo

### Documentacion consolidada

- **[Indice de Documentacion](./docs/DOCUMENTATION_INDEX.md)** - Vista general y criterio de consolidacion
- **[Funcionalidades Implementadas](./docs/FUNCIONALIDADES_IMPLEMENTADAS.md)** - Estado real agrupado por funcionalidad
- **[PR Doc Checklist](./docs/PR_DOC_CHECKLIST.md)** - Checklist para mantener docs alineadas en cada PR

### Pendientes HU / proceso no implementado

- **[Seguridad JWT](./docs/security-implementation.md)** - Backlog de implementacion de seguridad
- **[Plan Infra Type/TypeCategory](./docs/plan-infrastructureTypeTypeCategory.prompt.md)** - HU/proceso pendiente

## Arquitectura

```
application/        # Capa de aplicacion (Controllers, Config)
infrastructure/     # Infraestructura (Repositories, APIs externas)
domain/            # Logica de negocio (Entities, Services)
commons/           # Componentes compartidos (DTOs, Utilities)
```

### Patron Arquitectonico

- **Arquitectura Hexagonal** (Ports & Adapters)
- **CQRS Pattern** - Separacion de comandos y queries
- **Repository Pattern** - Abstraccion de acceso a datos

## Caracteristicas

- ✅ OpenAPI 3.0 / Swagger UI - Documentacion interactiva
- ✅ Arquitectura Hexagonal - Separacion clara de capas
- ✅ Internacionalizacion (i18n) - Soporte ES, EN, PT
- ✅ Validaciones Robustas - Bean Validation (JSR-380)
- ✅ Manejo de Errores Estandarizado - Respuestas consistentes
- ✅ CQRS Pattern - Separacion de escritura y lectura
- ✅ Spring Data JPA - Persistencia simplificada
- ✅ PostgreSQL - Base de datos por defecto
- ✅ Spring Boot 4.0 - Framework moderno

## Compilar y Ejecutar

### Compilar

```bash
./gradlew clean build
```

### Ejecutar

```bash
./gradlew bootRun
```

### Ejecutar Tests

```bash
./gradlew test
```

## Configuracion

### Perfiles

- `dev` - Desarrollo (por defecto)
- `staging` - Staging/QA
- `prod` - Produccion

### Cambiar Perfil

```properties
# application.properties
spring.profiles.active=dev
```

## Licencia

Apache 2.0


