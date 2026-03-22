## Plan: Infrastructure Type y TypeCategory

Implementar la capa `infrastructure` para `Type` y `TypeCategory` conectando puertos de dominio existentes con JPA y
REST, evitando duplicación de componentes globales. Adicionalmente, centralizar constantes por responsabilidad: errores
de infraestructura en `InfrastructureErrors`, metadatos/comentarios de tablas y columnas en `EntitiesConstants`, y
constantes de rutas/documentación Swagger/REST en `RestConstants`. Todas las claves deben resolverse por i18n (
`messages.properties`, `messages_en.properties`, `messages_pt.properties`).

### Steps

1. Corregir el escaneo JPA en `AbstractJpaConfig` y definir en `EntitiesConstants` las constantes de nombres/comentarios
   de tablas y columnas para `TypeEntity` y `TypeCategoryEntity`.
2. Crear `TypeEntity` y `TypeCategoryEntity` reutilizando `EntitiesConstants` para `@Table`, `@Column` y metadata
   descriptiva, evitando literales hardcodeados repetidos.
3. Implementar repositorios command/query JPA para ambos agregados reutilizando `IGenericJpaRepository`, sin crear
   contratos CRUD nuevos ni duplicar lógica.
4. Construir adaptadores de repositorio y servicios (`TypeServiceImpl`, `TypeCategoryServiceImpl`) extendiendo
   `GenericServiceImpl` y reutilizando `ITypeService` e `ITypeCategoryService`.
5. Definir DTOs mínimos por endpoint (entrada/salida/filtro) y mappers (`IGenericMapper` + mappers DTO↔dominio),
   transportando solo los datos necesarios para optimizar mapping y payload.
6. Exponer controladores REST y documentación Swagger usando `RestConstants` (paths, tags, operation
   summary/description), ejecutando comandos/queries de dominio y respondiendo con `ApiResponseBuilder`.
7. Centralizar claves de error de infraestructura en `InfrastructureErrors` y garantizar traducción en ES/EN/PT para
   errores y textos REST/Swagger; cerrar con checks `:infrastructure:compileJava`, `:domain:test`,
   `:infrastructure:test`, `:application:test`.

### Further Considerations

1. `InfrastructureErrors` debe contener claves de i18n (no textos fijos) para que `ErrorHandlerConfig` traduzca por
   locale.
2. `EntitiesConstants` debe ser la única fuente de verdad para nombres/comentarios de esquema y columnas.
3. `RestConstants` debe centralizar prefijos de rutas, nombres de recursos y textos de documentación para mantener
   consistencia y reducir duplicación.

