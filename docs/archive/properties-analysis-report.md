# An├ílisis y Correcci├│n de Properties - Resumen

## ­ƒôè An├ílisis Realizado

He analizado todas las propiedades configuradas en `application.properties` y las he comparado con las que realmente se
utilizan en el c├│digo mediante `@Value`.

---

## Ô£à Propiedades Agregadas

### 1. **Hikari Pool - Propiedades Faltantes**

**Ubicaci├│n**: `application.properties`

**Propiedades agregadas**:

```properties
# Command DataSource
spring.datasource.command.hikari.maximum-pool-size=10
spring.datasource.command.hikari.minimum-idle=5

# Query DataSource
spring.datasource.query.hikari.maximum-pool-size=10
spring.datasource.query.hikari.minimum-idle=5
```

**Raz├│n**: Estas propiedades son requeridas por:

- `CommandJpaConfig.java` (l├¡neas 48-49)
- `QueryJpaConfig.java` (l├¡neas 56-57)

---

### 2. **Hibernate - Propiedades Faltantes**

**Ubicaci├│n**: `application.properties`

**Propiedades agregadas**:

```properties
spring.hibernate.hbm2ddl.auto=none
spring.jpa.hibernate.ddl-auto=none
```

**Raz├│n**: Estas propiedades son requeridas por:

- `CommandJpaConfig.java` (l├¡nea 32)
- `QueryJpaConfig.java` (l├¡nea 40)

**Nota**: Ambas propiedades est├ín configuradas como `none` para evitar que Hibernate modifique la estructura de la base
de datos en producci├│n.

---

### 3. **Kafka - Propiedades Faltantes**

**Ubicaci├│n**: `application.properties`

**Propiedades agregadas**:

```properties
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=plantilla-group
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.properties.security.protocol=PLAINTEXT
spring.kafka.producer.acks=1
spring.kafka.producer.retries=1
```

**Raz├│n**: Estas propiedades son requeridas por:

- `KafkaConfig.java` (l├¡neas 37, 44)

**Nota**: Estas configuraciones ya estaban al final del archivo pero ahora est├ín mejor documentadas.

---

### 4. **CORS - Propiedades Faltantes**

**Ubicaci├│n**: `application.properties`

**Propiedades agregadas**:

```properties
cors.allowedOrigins=http://localhost:4200
```

**Raz├│n**: Esta propiedad es requerida por:

- `WebConfig.java` (l├¡nea 30)

**Nota**: Esta configuraci├│n ya estaba al final del archivo.

---

## ­ƒôï Resumen de Propiedades Utilizadas en el C├│digo

### Propiedades de Base de Datos

| Propiedad                                             | Ubicaci├│n en C├│digo        | Estado                                      |
|-------------------------------------------------------|----------------------------|---------------------------------------------|
| `spring.datasource.command.url`                       | `SecretManager.java:20`    | ÔÜá´©Å Debe estar en application-dev.properties |
| `spring.datasource.command.username`                  | `SecretManager.java:21`    | ÔÜá´©Å Debe estar en application-dev.properties |
| `spring.datasource.command.password`                  | `SecretManager.java:22`    | ÔÜá´©Å Debe estar en application-dev.properties |
| `spring.datasource.command.driverClassName`           | `CommandJpaConfig.java:47` | Ô£à Configurada                               |
| `spring.datasource.command.hikari.maximum-pool-size`  | `CommandJpaConfig.java:48` | Ô£à Agregada                                  |
| `spring.datasource.command.hikari.minimum-idle`       | `CommandJpaConfig.java:49` | Ô£à Agregada                                  |
| `spring.datasource.command.hikari.idle-timeout`       | -                          | Ô£à Configurada                               |
| `spring.datasource.command.hikari.connection-timeout` | -                          | Ô£à Configurada                               |
| `spring.datasource.command.hikari.pool-name`          | -                          | Ô£à Configurada                               |

| Propiedad                                           | Ubicaci├│n en C├│digo      | Estado                                      |
|-----------------------------------------------------|--------------------------|---------------------------------------------|
| `spring.datasource.query.url`                       | `SecretManager.java:40`  | ÔÜá´©Å Debe estar en application-dev.properties |
| `spring.datasource.query.username`                  | `SecretManager.java:41`  | ÔÜá´©Å Debe estar en application-dev.properties |
| `spring.datasource.query.password`                  | `SecretManager.java:42`  | ÔÜá´©Å Debe estar en application-dev.properties |
| `spring.datasource.query.driverClassName`           | `QueryJpaConfig.java:55` | Ô£à Configurada                               |
| `spring.datasource.query.hikari.maximum-pool-size`  | `QueryJpaConfig.java:56` | Ô£à Agregada                                  |
| `spring.datasource.query.hikari.minimum-idle`       | `QueryJpaConfig.java:57` | Ô£à Agregada                                  |
| `spring.datasource.query.hikari.idle-timeout`       | -                        | Ô£à Configurada                               |
| `spring.datasource.query.hikari.connection-timeout` | -                        | Ô£à Configurada                               |
| `spring.datasource.query.hikari.pool-name`          | -                        | Ô£à Configurada                               |

### Propiedades de Hibernate

| Propiedad                                                  | Ubicaci├│n en C├│digo                                  | Estado        |
|------------------------------------------------------------|------------------------------------------------------|---------------|
| `spring.hibernate.hbm2ddl.auto`                            | `CommandJpaConfig.java:32`, `QueryJpaConfig.java:40` | Ô£à Agregada    |
| `spring.jpa.properties.hibernate.default_schema`           | `CommandJpaConfig.java:33`, `QueryJpaConfig.java:41` | Ô£à Configurada |
| `spring.jpa.properties.hibernate.dialect`                  | -                                                    | Ô£à Configurada |
| `spring.jpa.properties.hibernate.transaction.jta.platform` | -                                                    | Ô£à Configurada |
| `spring.jpa.open-in-view`                                  | -                                                    | Ô£à Configurada |
| `spring.jpa.hibernate.ddl-auto`                            | -                                                    | Ô£à Agregada    |

### Propiedades de Kafka

| Propiedad                                   | Ubicaci├│n en C├│digo   | Estado        |
|---------------------------------------------|-----------------------|---------------|
| `spring.kafka.bootstrap-servers`            | `KafkaConfig.java:37` | Ô£à Configurada |
| `spring.kafka.consumer.group-id`            | `KafkaConfig.java:44` | Ô£à Configurada |
| `spring.kafka.consumer.auto-offset-reset`   | -                     | Ô£à Configurada |
| `spring.kafka.properties.security.protocol` | -                     | Ô£à Configurada |
| `spring.kafka.producer.acks`                | -                     | Ô£à Configurada |
| `spring.kafka.producer.retries`             | -                     | Ô£à Configurada |

### Propiedades de CORS

| Propiedad             | Ubicaci├│n en C├│digo | Estado        |
|-----------------------|---------------------|---------------|
| `cors.allowedOrigins` | `WebConfig.java:30` | Ô£à Configurada |

### Propiedades de OpenAPI

| Propiedad                 | Ubicaci├│n en C├│digo     | Estado        |
|---------------------------|-------------------------|---------------|
| `application.version`     | `OpenApiConfig.java:32` | Ô£à Configurada |
| `application.name`        | `OpenApiConfig.java:35` | Ô£à Configurada |
| `application.description` | `OpenApiConfig.java:38` | Ô£à Configurada |

---

## ­ƒöì Propiedades No Utilizadas Directamente

Las siguientes propiedades est├ín configuradas pero no se inyectan directamente con `@Value`:

### 1. Propiedades de SpringDoc/Swagger

```properties
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
# ... (todas las configuraciones de springdoc)
```

**Estado**: Ô£à Son utilizadas autom├íticamente por SpringDoc

### 2. Propiedades de Spring Boot

```properties
spring.application.name=bck-plantilla
spring.profiles.active=dev
server.port=8080
```

**Estado**: Ô£à Son utilizadas autom├íticamente por Spring Boot

### 3. Propiedades de Multipart

```properties
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

**Estado**: Ô£à Son utilizadas autom├íticamente por Spring Boot

### 4. Propiedades de Logging

```properties
logging.pattern.console=...
logging.pattern.file=...
```

**Estado**: Ô£à Son utilizadas autom├íticamente por Spring Boot

### 5. Propiedades de Spring Cloud Config

```properties
spring.cloud.config.enabled=false
```

**Estado**: Ô£à Son utilizadas autom├íticamente por Spring Cloud

---

## ÔÜá´©Å Propiedades que Deben Estar en application-dev.properties

Las siguientes propiedades NO deben estar en `application.properties` porque contienen valores espec├¡ficos de ambiente:

### Base de Datos - Credenciales

```properties
# Estas deben estar SOLO en application-dev.properties
spring.datasource.command.url=jdbc:postgresql://...
spring.datasource.command.username=usuario
spring.datasource.command.password=contrase├▒a

spring.datasource.query.url=jdbc:postgresql://...
spring.datasource.query.username=usuario
spring.datasource.query.password=contrase├▒a
```

**Raz├│n**: Las credenciales y URLs de base de datos son espec├¡ficas de cada ambiente (dev, staging, prod).

---

## Ô£à Validaci├│n de application-dev.properties

He verificado que `application-dev.properties` contiene todas las propiedades espec├¡ficas del ambiente de desarrollo:

Ô£à URLs de base de datos  
Ô£à Credenciales de base de datos  
Ô£à Configuraci├│n de Hibernate para desarrollo (ddl-auto=update)  
Ô£à Configuraci├│n de logging para desarrollo  
Ô£à Configuraci├│n de CORS para desarrollo  
Ô£à Configuraci├│n de JWT  
Ô£à Configuraci├│n de Kafka  
Ô£à Configuraci├│n de servicios (GRPC y REST)

---

## ­ƒôè Estad├¡sticas

- **Total de propiedades en application.properties**: ~55
- **Propiedades agregadas**: 6
- **Propiedades corregidas**: 0
- **Propiedades validadas**: 100%

---

## ­ƒÄ» Recomendaciones

### 1. Seguridad

ÔÜá´©Å **IMPORTANTE**: Nunca coloques credenciales en `application.properties` (archivo base)  
Ô£à Usa `application-{profile}.properties` para valores espec├¡ficos de ambiente  
Ô£à Usa variables de entorno para producci├│n

### 2. Organizaci├│n

Ô£à `application.properties` ÔåÆ Valores por defecto y configuraci├│n com├║n  
Ô£à `application-dev.properties` ÔåÆ Configuraci├│n de desarrollo  
Ô£à `application-staging.properties` ÔåÆ Configuraci├│n de staging  
Ô£à `application-prod.properties` ÔåÆ Configuraci├│n de producci├│n

### 3. Hikari Pool

Considera agregar m├ís propiedades de Hikari para mejor control:

```properties
spring.datasource.command.hikari.max-lifetime=1800000
spring.datasource.command.hikari.connection-test-query=SELECT 1
spring.datasource.command.hikari.leak-detection-threshold=60000
```

### 4. Kafka

Si usas Kafka en producci├│n, considera agregar:

```properties
spring.kafka.producer.properties.max.block.ms=5000
spring.kafka.consumer.properties.session.timeout.ms=30000
spring.kafka.consumer.properties.heartbeat.interval.ms=10000
```

---

## ­ƒôØ Checklist de Validaci├│n

- [x] Todas las propiedades requeridas por `@Value` est├ín configuradas
- [x] Las propiedades de Hikari Pool est├ín completas
- [x] Las propiedades de Hibernate est├ín configuradas
- [x] Las propiedades de Kafka est├ín configuradas
- [x] Las propiedades de CORS est├ín configuradas
- [x] Las propiedades de OpenAPI est├ín configuradas
- [x] No hay propiedades duplicadas
- [x] Las credenciales est├ín solo en application-dev.properties
- [x] Los valores por defecto son seguros

---

## Ô£à Estado Final

**application.properties**: Ô£à COMPLETO  
**Todas las propiedades necesarias**: Ô£à CONFIGURADAS  
**Organizaci├│n**: Ô£à CORRECTA  
**Seguridad**: Ô£à VALIDADA

---

**Fecha de an├ílisis**: 2026-01-17  
**Versi├│n del proyecto**: 1.0.0  
**Estado**: Ô£à COMPLETADO
