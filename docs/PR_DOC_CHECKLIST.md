# PR Doc Checklist

Usa esta checklist en cada PR para mantener la documentación alineada con el código.

## Checklist rápida

- [ ] Si cambias `controller`, actualizaste la sección **API REST y flujo de capas** en `docs/FUNCIONALIDADES_IMPLEMENTADAS.md`.
- [ ] Si cambias respuestas (`ApiResponseBuilder` o contratos), actualizaste **Respuestas unificadas**.
- [ ] Si cambias `ErrorHandlerConfig` o excepciones, actualizaste **Manejo de errores**.
- [ ] Si cambias entidades/repos/JPA/scripts, actualizaste **Persistencia y modelo de datos** y/o **Scripts SQL**.
- [ ] Si cambias validaciones/i18n/properties, actualizaste **Validaciones e internacionalización**.
- [ ] Si agregaste una funcionalidad nueva, la incorporaste en `docs/FUNCIONALIDADES_IMPLEMENTADAS.md` bajo el bloque funcional correspondiente.
- [ ] Si detectaste docs obsoletos, los eliminaste o los fusionaste en el documento consolidado.

## Regla de oro

Si un cambio altera comportamiento observable de API o persistencia, debe quedar documentado en el mismo PR.

## Alcance de documentos vigentes

- `docs/FUNCIONALIDADES_IMPLEMENTADAS.md` (canónico técnico-funcional)
- `docs/DOCUMENTATION_INDEX.md` (navegación)
- `docs/security-implementation.md` (HU/proceso pendiente)
- `docs/plan-infrastructureTypeTypeCategory.prompt.md` (HU/proceso pendiente)

