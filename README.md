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

## Guias Disponibles

- [Documentacion de API con Swagger](./docs/swagger-documentation-guide.md)
- [Seguridad JWT](./docs/security-implementation.md)
- [Internacionalizacion](./docs/i18n-guide.md)
- [Transacciones](./docs/transactional-guide.md)
- [Respuestas Genericas](./docs/generic-response-guide.md)

## Arquitectura

```
application/        # Capa de aplicacion
infrastructure/     # Infraestructura
domain/            # Logica de negocio
commons/           # Componentes compartidos
```

## Caracteristicas

- OpenAPI 3.0 / Swagger UI
- Arquitectura Hexagonal
- Internacionalizacion (i18n)
- Validaciones Robustas
- CQRS Pattern
- Spring Data JPA
- PostgreSQL

## Licencia

Apache 2.0
