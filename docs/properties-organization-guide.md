# Guia de Organizacion de Properties

## Filosofia de Configuracion

Este proyecto sigue una estrategia de configuracion en dos niveles:

1. **application.properties** → Configuraciones **GENERALES** (para Config Server)
2. **application-{profile}.properties** → Configuraciones **ESPECIFICAS** por ambiente

---

## 📁 application.properties (Base - Config Server)

### Proposito
Contiene configuraciones **estructurales y generales** que:
- Son comunes a TODOS los ambientes (dev, staging, prod)
- Definen la estructura y comportamiento de frameworks
- Seran movidas al servidor de configuracion centralizada (Spring Cloud Config)

### Que VA en este archivo ✅

#### 1. Configuraciones Estructurales de Base de Datos
```properties
# Dialectos, schemas, estrategias JTA
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.default_schema=public
spring.jpa.properties.hibernate.transaction.jta.platform=none
spring.jpa.open-in-view=false

# DDL Strategy por defecto (seguro para produccion)
spring.hibernate.hbm2ddl.auto=none
spring.jpa.hibernate.ddl-auto=none
```

#### 2. Configuraciones Estructurales de Hikari Pool
```properties
# Driver comun
spring.datasource.command.driverClassName=org.postgresql.Driver
spring.datasource.query.driverClassName=org.postgresql.Driver

# Timeouts comunes
spring.datasource.command.hikari.idle-timeout=300000
spring.datasource.command.hikari.connection-timeout=20000

# Pool names
spring.datasource.command.hikari.pool-name=CommandHikariPool
```

#### 3. Configuraciones de Frameworks (Swagger, Spring Boot)
```properties
# SpringDoc/Swagger
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.operations-sorter=method

# Multipart
spring.servlet.multipart.max-file-size=10MB

# Logging patterns
logging.pattern.console=%clr(%d{...
```

#### 4. Configuraciones Comunes de Kafka
```properties
# Politicas comunes
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.properties.security.protocol=PLAINTEXT
spring.kafka.producer.acks=1
```

### Que NO VA en este archivo ❌

- ❌ URLs de base de datos
- ❌ Credenciales (usernames, passwords)
- ❌ Pool sizes especificos
- ❌ Server ports
- ❌ Bootstrap servers de Kafka
- ❌ URLs de servicios externos
- ❌ Niveles de logging especificos
- ❌ CORS origins especificos

---

## 📁 application-dev.properties (Desarrollo)

### Proposito
Contiene configuraciones **especificas del ambiente de desarrollo** que:
- Son unicas para este ambiente
- Incluyen credenciales de desarrollo
- Definen conexiones y recursos locales
- Se repiten en cada perfil (dev, staging, prod) con valores diferentes

### Que VA en este archivo ✅

#### 1. Configuracion del Servidor
```properties
server.port=8080
```

#### 2. URLs y Credenciales de Base de Datos
```properties
# URLs especificas de desarrollo
spring.datasource.command.url=jdbc:postgresql://dev-server:5432/dev_db
spring.datasource.command.username=dev_user
spring.datasource.command.password=dev_password

spring.datasource.query.url=jdbc:postgresql://dev-server:5432/dev_db
spring.datasource.query.username=dev_user
spring.datasource.query.password=dev_password
```

#### 3. Pool Sizes Especificos
```properties
# Tamanos de pool para desarrollo (mas pequenos)
spring.datasource.command.hikari.maximum-pool-size=5
spring.datasource.command.hikari.minimum-idle=2

spring.datasource.query.hikari.maximum-pool-size=10
spring.datasource.query.hikari.minimum-idle=5
```

#### 4. Configuraciones de Desarrollo
```properties
# Mostrar SQL en desarrollo
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# DDL auto-update en desarrollo
spring.jpa.hibernate.ddl-auto=update
spring.hibernate.hbm2ddl.auto=update
```

#### 5. Niveles de Logging Especificos
```properties
logging.level.root=INFO
logging.level.com.empresa=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

#### 6. Kafka - Valores Especificos
```properties
# Bootstrap servers de desarrollo
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=plantilla-group-dev
```

#### 7. CORS - Origenes Especificos
```properties
# Frontend de desarrollo
cors.allowedOrigins=http://localhost:4200
```

#### 8. URLs de Servicios Externos
```properties
# GRPC Services
grpc.services.example-service.address=localhost:8280

# REST Services
rest.services.example-service.url=http://localhost:8380/api
```

#### 9. Configuraciones de Seguridad (JWT, etc)
```properties
jwt.secret=mydevsecretkeyforjwtgeneration
jwt.expirationMs=3600000
```

---

## 📊 Tabla de Decision Rapida

| Configuracion | application.properties | application-{profile}.properties |
|--------------|------------------------|----------------------------------|
| **Dialect de Hibernate** | ✅ Si | ❌ No |
| **Schema por defecto** | ✅ Si | ❌ No |
| **DDL strategy default** | ✅ Si (none) | ✅ Si (override: update/validate) |
| **Driver JDBC** | ✅ Si | ❌ No |
| **Timeouts de Hikari** | ✅ Si | ❌ No |
| **Pool names** | ✅ Si | ❌ No |
| **URL de base de datos** | ❌ No | ✅ Si |
| **Credenciales de BD** | ❌ No | ✅ Si |
| **Pool sizes (max/min)** | ❌ No | ✅ Si |
| **Server port** | ❌ No | ✅ Si |
| **Swagger enabled** | ✅ Si | ❌ No |
| **Swagger path** | ✅ Si | ❌ No |
| **Swagger UI config** | ✅ Si | ❌ No |
| **Kafka auto-offset-reset** | ✅ Si | ❌ No |
| **Kafka security protocol** | ✅ Si | ❌ No |
| **Kafka bootstrap-servers** | ❌ No | ✅ Si |
| **Kafka group-id** | ❌ No | ✅ Si |
| **CORS allowed origins** | ❌ No | ✅ Si |
| **Logging patterns** | ✅ Si | ❌ No |
| **Logging levels** | ❌ No | ✅ Si |
| **URLs servicios externos** | ❌ No | ✅ Si |
| **JWT secret** | ❌ No | ✅ Si |
| **Multipart sizes** | ✅ Si | ❌ No |

---

## 🔄 Estrategia de Perfiles

### Estructura Recomendada

```
application.properties           (Base - Config Server)
├── application-dev.properties   (Desarrollo)
├── application-staging.properties (Staging/QA)
└── application-prod.properties  (Produccion)
```

### Ejemplo: Pool Sizes por Ambiente

**application.properties** (comun):
```properties
# Timeouts comunes
spring.datasource.command.hikari.idle-timeout=300000
spring.datasource.command.hikari.connection-timeout=20000
```

**application-dev.properties**:
```properties
# Pool pequeno para desarrollo
spring.datasource.command.hikari.maximum-pool-size=5
spring.datasource.command.hikari.minimum-idle=2
```

**application-staging.properties**:
```properties
# Pool mediano para staging
spring.datasource.command.hikari.maximum-pool-size=10
spring.datasource.command.hikari.minimum-idle=5
```

**application-prod.properties**:
```properties
# Pool grande para produccion
spring.datasource.command.hikari.maximum-pool-size=20
spring.datasource.command.hikari.minimum-idle=10
```

---

## 🎯 Reglas de Oro

### 1. Preguntate: "¿Este valor cambia entre ambientes?"

- **SI cambia** → `application-{profile}.properties`
- **NO cambia** → `application.properties`

### 2. Preguntate: "¿Es una credencial o URL?"

- **SI** → `application-{profile}.properties`
- **NO** → Evalua con regla 1

### 3. Preguntate: "¿Es una configuracion estructural del framework?"

- **SI** → `application.properties`
- **NO** → Evalua con regla 1

---

## 📝 Ejemplos Practicos

### ✅ CORRECTO

**application.properties**:
```properties
# Driver comun a todos los ambientes
spring.datasource.command.driverClassName=org.postgresql.Driver

# Timeout comun a todos los ambientes
spring.datasource.command.hikari.connection-timeout=20000
```

**application-dev.properties**:
```properties
# URL especifica de desarrollo
spring.datasource.command.url=jdbc:postgresql://localhost:5432/dev_db

# Pool size especifico de desarrollo
spring.datasource.command.hikari.maximum-pool-size=5
```

### ❌ INCORRECTO

**application.properties**:
```properties
# ❌ NO: URL especifica (cambia por ambiente)
spring.datasource.command.url=jdbc:postgresql://localhost:5432/dev_db

# ❌ NO: Credenciales (especificas por ambiente)
spring.datasource.command.username=dev_user
spring.datasource.command.password=dev_password

# ❌ NO: Pool size (varia por ambiente)
spring.datasource.command.hikari.maximum-pool-size=5

# ❌ NO: Server port (especifico por ambiente)
server.port=8080
```

---

## 🚀 Migracion a Config Server

Cuando migres a Spring Cloud Config Server:

1. **application.properties** → Se mueve completo al repositorio de configuracion
2. **application-{profile}.properties** → Se crean en el repositorio con valores especificos
3. Los archivos locales se pueden eliminar o mantener como fallback

### Ejemplo de estructura en Config Server:

```
config-repo/
├── bck-plantilla.properties           (de application.properties)
├── bck-plantilla-dev.properties       (de application-dev.properties)
├── bck-plantilla-staging.properties   (de application-staging.properties)
└── bck-plantilla-prod.properties      (de application-prod.properties)
```

---

## ✅ Checklist de Validacion

Antes de agregar una propiedad, verifica:

- [ ] ¿Es una configuracion estructural de framework? → application.properties
- [ ] ¿Es un valor que cambia entre ambientes? → application-{profile}.properties
- [ ] ¿Es una credencial o secret? → application-{profile}.properties (NUNCA en base)
- [ ] ¿Es una URL de servicio externo? → application-{profile}.properties
- [ ] ¿Es un nivel de logging? → application-{profile}.properties
- [ ] ¿Es un pool size? → application-{profile}.properties
- [ ] ¿Es un timeout o pattern? → application.properties
- [ ] ¿Es una configuracion de Swagger? → application.properties

---

## 📚 Referencias

- [Spring Boot Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
- [Spring Cloud Config](https://spring.io/projects/spring-cloud-config)

---

**Fecha**: 2026-01-17  
**Version**: 1.0.0
