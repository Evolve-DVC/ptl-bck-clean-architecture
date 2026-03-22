# Gu├¡a de Uso de @Transactional en el Proyecto

## ├ìndice

1. [Introducci├│n](#introducci├│n)
2. [Configuraci├│n B├ísica](#configuraci├│n-b├ísica)
3. [Uso en Commands](#uso-en-commands)
4. [Propagaci├│n de Transacciones](#propagaci├│n-de-transacciones)
5. [Manejo de Excepciones y Rollback](#manejo-de-excepciones-y-rollback)
6. [Testing de Transacciones](#testing-de-transacciones)
7. [Mejores Pr├ícticas](#mejores-pr├ícticas)

## Introducci├│n

Las transacciones garantizan que un conjunto de operaciones se ejecuten de forma at├│mica: o todas se completan
exitosamente o ninguna se aplica. En este proyecto, utilizamos `@Transactional` de Spring Boot para gestionar
transacciones de forma declarativa.

## Configuraci├│n B├ísica

### 1. MainApplication

La clase principal ya tiene `@EnableTransactionManagement` habilitado:

```java
@EnableAsync
@SpringBootApplication
@ComponentScan(basePackages = "com.empresa.plantilla")
@EnableTransactionManagement
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
```

### 2. Dependencias en build.gradle

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.postgresql:postgresql'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
}
```

### 3. Configuraci├│n en application.yml

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/mi_base_datos
    username: usuario
    password: password
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        enable_lazy_load_no_trans: false
        jdbc:
          batch_size: 20
        format_sql: true
    show-sql: false
    open-in-view: false
  
  transaction:
    default-timeout: 30
```

### 4. Configuraci├│n Avanzada (Opcional)

```java
package com.empresa.plantilla.commons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
public class TransactionConfig {

    @Bean
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
```

## Uso en Commands

### Ejemplo B├ísico: CreateUserCommand

```java
package com.empresa.plantilla.domain.user.command;

import com.empresa.plantilla.commons.command.CommandProcessAbstract;
import com.empresa.plantilla.commons.exception.DomainException;
import com.empresa.plantilla.domain.user.model.User;
import com.empresa.plantilla.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateUserCommand 
        extends CommandProcessAbstract<CreateUserContext, UserResult> {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void preProcess() throws DomainException {
        // Validaciones sin transacci├│n
        if (context == null || context.getEmail() == null) {
            throw new DomainException("Contexto inv├ílido");
        }
        
        if (userRepository.existsByEmail(context.getEmail())) {
            throw new DomainException("El email ya est├í registrado");
        }
        
        setValid(true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    protected void process() throws DomainException {
        // Operaci├│n transaccional: crear usuario
        User user = User.builder()
                .name(context.getName())
                .email(context.getEmail())
                .password(passwordEncoder.encode(context.getPassword()))
                .createdAt(LocalDateTime.now())
                .build();
        
        User savedUser = userRepository.save(user);
        
        setResult(UserResult.builder()
                .userId(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .build());
        
        setExecuted(true);
    }

    @Override
    protected void postProcess() throws DomainException {
        // Post-procesamiento sin transacci├│n
        if (result != null) {
            emailService.sendWelcomeEmail(result.getEmail());
        }
    }
}
```

### Ejemplo con M├║ltiples Operaciones: CreateOrderCommand

```java
package com.empresa.plantilla.domain.order.command;

import com.empresa.plantilla.commons.command.CommandProcessAbstract;
import com.empresa.plantilla.commons.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CreateOrderCommand 
        extends CommandProcessAbstract<CreateOrderContext, OrderResult> {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final InventoryService inventoryService;
    private final NotificationService notificationService;

    @Override
    protected void preProcess() throws DomainException {
        // Validar disponibilidad de productos
        if (context == null || context.getItems() == null || context.getItems().isEmpty()) {
            throw new DomainException("Contexto de orden inv├ílido");
        }
        
        for (OrderItem item : context.getItems()) {
            if (!productRepository.existsById(item.getProductId())) {
                throw new DomainException("Producto no encontrado: " + item.getProductId());
            }
            
            if (!inventoryService.hasStock(item.getProductId(), item.getQuantity())) {
                throw new DomainException("Stock insuficiente para: " + item.getProductId());
            }
        }
        
        setValid(true);
    }

    @Override
    @Transactional(
        propagation = Propagation.REQUIRED,
        isolation = Isolation.READ_COMMITTED,
        timeout = 30,
        rollbackFor = Exception.class
    )
    protected void process() throws DomainException {
        // 1. Crear la orden
        Order order = Order.builder()
                .userId(context.getUserId())
                .status(OrderStatus.PENDING)
                .totalAmount(calculateTotal())
                .createdAt(LocalDateTime.now())
                .build();
        
        Order savedOrder = orderRepository.save(order);
        
        // 2. Procesar cada item
        for (OrderItem item : context.getItems()) {
            // Reducir inventario
            inventoryService.reduceStock(
                item.getProductId(), 
                item.getQuantity()
            );
            
            // Crear l├¡nea de orden
            OrderLine orderLine = OrderLine.builder()
                    .order(savedOrder)
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .price(item.getPrice())
                    .build();
            
            savedOrder.addOrderLine(orderLine);
        }
        
        // 3. Guardar orden completa
        // Si algo falla aqu├¡, TODO se revierte
        orderRepository.save(savedOrder);
        
        setResult(OrderResult.from(savedOrder));
        setExecuted(true);
    }

    @Override
    protected void postProcess() throws DomainException {
        // Enviar notificaciones (fuera de la transacci├│n)
        if (result != null) {
            notificationService.notifyOrderCreated(result);
        }
    }

    private BigDecimal calculateTotal() {
        return context.getItems().stream()
                .map(item -> item.getPrice().multiply(
                    BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
```

## Propagaci├│n de Transacciones

### Ejemplo: Command que llama a otros Commands

```java
package com.empresa.plantilla.domain.registration.command;

import com.empresa.plantilla.commons.command.CommandProcessAbstract;
import com.empresa.plantilla.commons.exception.DomainException;
import com.empresa.plantilla.domain.user.command.CreateUserCommand;
import com.empresa.plantilla.domain.profile.command.CreateProfileCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RegisterUserCommand 
        extends CommandProcessAbstract<RegistrationContext, RegistrationResult> {

    private final CreateUserCommand createUserCommand;
    private final CreateProfileCommand createProfileCommand;

    @Override
    protected void preProcess() throws DomainException {
        if (context == null) {
            throw new DomainException("Contexto inv├ílido");
        }
        setValid(true);
    }

    @Override
    @Transactional(
        propagation = Propagation.REQUIRED,
        rollbackFor = Exception.class
    )
    protected void process() throws DomainException {
        // 1. Crear usuario (se une a esta transacci├│n)
        CreateUserContext userContext = CreateUserContext.builder()
                .name(context.getName())
                .email(context.getEmail())
                .password(context.getPassword())
                .build();
        
        createUserCommand.setContext(userContext);
        UserResult userResult = createUserCommand.execute();
        
        // 2. Crear perfil (se une a esta transacci├│n)
        CreateProfileContext profileContext = CreateProfileContext.builder()
                .userId(userResult.getUserId())
                .bio(context.getBio())
                .phone(context.getPhone())
                .build();
        
        createProfileCommand.setContext(profileContext);
        ProfileResult profileResult = createProfileCommand.execute();
        
        // Si cualquiera de los dos falla, TODO se revierte
        
        setResult(RegistrationResult.builder()
                .userId(userResult.getUserId())
                .profileId(profileResult.getProfileId())
                .build());
        
        setExecuted(true);
    }

    @Override
    protected void postProcess() throws DomainException {
        // Notificaciones post-registro
    }
}
```

### Tipos de Propagaci├│n

```java
// REQUIRED (por defecto): Usa transacci├│n existente o crea una nueva
@Transactional(propagation = Propagation.REQUIRED)

// REQUIRES_NEW: Siempre crea una nueva transacci├│n (suspende la existente)
@Transactional(propagation = Propagation.REQUIRES_NEW)

// SUPPORTS: Usa transacci├│n si existe, sino ejecuta sin transacci├│n
@Transactional(propagation = Propagation.SUPPORTS)

// MANDATORY: Requiere transacci├│n existente, lanza excepci├│n si no hay
@Transactional(propagation = Propagation.MANDATORY)

// NOT_SUPPORTED: Se ejecuta sin transacci├│n (suspende la existente)
@Transactional(propagation = Propagation.NOT_SUPPORTED)

// NEVER: Lanza excepci├│n si hay una transacci├│n activa
@Transactional(propagation = Propagation.NEVER)

// NESTED: Crea transacci├│n anidada si hay una existente
@Transactional(propagation = Propagation.NESTED)
```

## Manejo de Excepciones y Rollback

### Ejemplo: Rollback Expl├¡cito

```java
package com.empresa.plantilla.domain.payment.command;

import com.empresa.plantilla.commons.command.CommandProcessAbstract;
import com.empresa.plantilla.commons.exception.DomainException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessPaymentCommand 
        extends CommandProcessAbstract<PaymentContext, PaymentResult> {

    private final PaymentGateway paymentGateway;
    private final OrderRepository orderRepository;

    @Override
    protected void preProcess() throws DomainException {
        if (context == null || context.getAmount() == null) {
            throw new DomainException("Contexto de pago inv├ílido");
        }
        setValid(true);
    }

    @Override
    @Transactional(
        rollbackFor = {
            DomainException.class,
            PaymentException.class,
            RuntimeException.class
        },
        noRollbackFor = {
            NotificationException.class  // No revertir por errores de notificaci├│n
        }
    )
    protected void process() throws DomainException {
        try {
            // 1. Procesar pago en gateway externo
            PaymentResponse response = paymentGateway.charge(
                context.getAmount(),
                context.getCardToken()
            );
            
            if (!response.isSuccessful()) {
                throw new PaymentException("Pago rechazado: " + response.getError());
            }
            
            // 2. Actualizar orden
            Order order = orderRepository.findById(context.getOrderId())
                    .orElseThrow(() -> new DomainException("Orden no encontrada"));
            
            order.setStatus(OrderStatus.PAID);
            order.setPaymentId(response.getTransactionId());
            order.setPaidAt(LocalDateTime.now());
            orderRepository.save(order);
            
            setResult(PaymentResult.builder()
                    .transactionId(response.getTransactionId())
                    .status(PaymentStatus.APPROVED)
                    .build());
            
            setExecuted(true);
                    
        } catch (PaymentGatewayException e) {
            // La transacci├│n se revertir├í autom├íticamente
            log.error("Error al procesar pago", e);
            throw new PaymentException("Error en el gateway de pago", e);
        }
    }

    @Override
    protected void postProcess() throws DomainException {
        // Enviar confirmaci├│n de pago
    }
}
```

### Ejemplo: Manejo de Rollback con TransactionTemplate

```java
package com.empresa.plantilla.domain.bulk.command;

import com.empresa.plantilla.commons.command.CommandProcessAbstract;
import com.empresa.plantilla.commons.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BulkImportCommand 
        extends CommandProcessAbstract<BulkImportContext, BulkImportResult> {

    private final TransactionTemplate transactionTemplate;
    private final UserRepository userRepository;

    @Override
    protected void preProcess() throws DomainException {
        if (context == null || context.getUsersData() == null) {
            throw new DomainException("Contexto inv├ílido");
        }
        setValid(true);
    }

    @Override
    protected void process() throws DomainException {
        List<User> successfulImports = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        
        // Cada usuario se importa en su propia transacci├│n
        for (UserData userData : context.getUsersData()) {
            try {
                transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(TransactionStatus status) {
                        try {
                            User user = createUserFromData(userData);
                            userRepository.save(user);
                            successfulImports.add(user);
                        } catch (Exception e) {
                            status.setRollbackOnly();
                            errors.add("Error importando usuario " + userData.getEmail() 
                                + ": " + e.getMessage());
                        }
                    }
                });
            } catch (Exception e) {
                errors.add("Error en transacci├│n: " + e.getMessage());
            }
        }
        
        setResult(BulkImportResult.builder()
                .successCount(successfulImports.size())
                .failureCount(errors.size())
                .errors(errors)
                .build());
        
        setExecuted(true);
    }

    @Override
    protected void postProcess() throws DomainException {
        // Generar reporte de importaci├│n
    }

    private User createUserFromData(UserData data) {
        return User.builder()
                .name(data.getName())
                .email(data.getEmail())
                .build();
    }
}
```

## Testing de Transacciones

### Ejemplo: Test con @Transactional

```java
package com.empresa.plantilla.domain.user.command;

import com.empresa.plantilla.commons.command.CommandProcessTestAbstract;
import com.empresa.plantilla.commons.exception.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CreateUserCommandTest 
        extends CommandProcessTestAbstract<CreateUserContext, UserResult> {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private CreateUserCommand command;

    @BeforeEach
    public void setUp() {
        command = createCommand();
    }

    @Override
    protected CreateUserCommand createCommand() {
        return new CreateUserCommand(userRepository, emailService, passwordEncoder);
    }

    @Test
    @Transactional
    @Rollback
    void testTransactionCommit() throws DomainException {
        // Arrange
        CreateUserContext context = createValidContext();
        command.setContext(context);
        
        // Act
        UserResult result = command.execute();
        entityManager.flush();  // Forzar escritura a BD
        entityManager.clear();  // Limpiar cach├®
        
        // Assert
        User savedUser = userRepository.findById(result.getUserId())
                .orElseThrow();
        assertNotNull(savedUser);
        assertEquals(context.getEmail(), savedUser.getEmail());
        assertTrue(command.isExecuted());
    }

    @Test
    @Transactional
    void testTransactionRollback() {
        // Arrange
        CreateUserContext context = createInvalidContext();
        command.setContext(context);
        
        // Act & Assert
        assertThrows(DomainException.class, () -> command.execute());
        
        // Verificar que no se guard├│ nada
        entityManager.clear();
        long count = userRepository.count();
        assertEquals(0, count);
        assertFalse(command.isExecuted());
    }

    @Test
    @Transactional
    void testTransactionIsolation() throws DomainException {
        // Arrange
        CreateUserContext context1 = createValidContext();
        CreateUserContext context2 = createValidContext();
        context2.setEmail("other@example.com");
        
        command.setContext(context1);
        CreateUserCommand command2 = createCommand();
        command2.setContext(context2);
        
        // Act
        UserResult result1 = command.execute();
        UserResult result2 = command2.execute();
        
        entityManager.flush();
        
        // Assert
        assertEquals(2, userRepository.count());
        assertNotEquals(result1.getUserId(), result2.getUserId());
    }

    @Test
    @Transactional
    void testAsyncTransactionExecution() throws DomainException {
        // Arrange
        CreateUserContext context = createValidContext();
        command.setContext(context);
        command.setAsync(true);
        
        // Act
        UserResult result = command.execute();
        
        // Assert
        assertNotNull(result);
        assertTrue(command.isExecuted());
    }

    @Override
    protected CreateUserContext createValidContext() {
        return CreateUserContext.builder()
                .name("John Doe")
                .email("john@example.com")
                .password("SecurePass123!")
                .build();
    }

    @Override
    protected CreateUserContext createInvalidContext() {
        return CreateUserContext.builder()
                .name("")  // Nombre inv├ílido
                .email("invalid-email")
                .password("123")
                .build();
    }

    @Override
    protected void mockDependencies() {
        // No se necesitan mocks en @DataJpaTest
    }

    @Override
    protected void assertValidResult(UserResult result) {
        assertNotNull(result);
        assertNotNull(result.getUserId());
        assertEquals("john@example.com", result.getEmail());
    }

    @Override
    protected void setMethod() {
        // Configuraci├│n espec├¡fica si es necesaria
    }
}
```

## Mejores Pr├ícticas

### 1. Ubicaci├│n del @Transactional

Ô£à **CORRECTO**: Solo en el m├®todo `process()`

```java
@Override
@Transactional(rollbackFor = Exception.class)
protected void process() throws DomainException {
    // Operaciones transaccionales
    User user = userRepository.save(context.toEntity());
    setResult(UserResult.from(user));
    setExecuted(true);
}
```

ÔØî **INCORRECTO**: En toda la clase

```java
@Transactional  // NO hacer esto
public class CreateUserCommand extends CommandProcessAbstract<...> {
    // Toda la clase queda transaccional (incluso preProcess y postProcess)
}
```

### 2. Especificar Rollback Expl├¡citamente

Ô£à **CORRECTO**:

```java
@Transactional(rollbackFor = Exception.class)
protected void process() throws DomainException {
    // Hace rollback para cualquier excepci├│n
}
```

ÔØî **INCORRECTO**: Confiar en el comportamiento por defecto

```java
@Transactional  // Solo hace rollback para RuntimeException
protected void process() throws DomainException {
    // DomainException no causar├í rollback si es checked
}
```

### 3. Usar Timeout Apropiado

Ô£à **CORRECTO**:

```java
@Transactional(
    rollbackFor = Exception.class,
    timeout = 30  // 30 segundos para operaciones normales
)
protected void process() throws DomainException {
    // Operaciones con l├¡mite de tiempo
}
```

ÔÜá´©Å **PARA OPERACIONES LARGAS**:

```java
@Transactional(
    rollbackFor = Exception.class,
    timeout = 300  // 5 minutos para importaciones masivas
)
protected void process() throws DomainException {
    // Procesamiento batch
}
```

### 4. Separar Operaciones Transaccionales

Ô£à **CORRECTO**:

```java
@Override
protected void preProcess() throws DomainException {
    // Validaciones (sin transacci├│n)
    validateContext();
    setValid(true);
}

@Override
@Transactional(rollbackFor = Exception.class)
protected void process() throws DomainException {
    // Solo operaciones de BD
    User user = userRepository.save(context.toEntity());
    setResult(UserResult.from(user));
    setExecuted(true);
}

@Override
protected void postProcess() throws DomainException {
    // Notificaciones (sin transacci├│n)
    emailService.sendWelcomeEmail(result.getEmail());
}
```

### 5. Usar readOnly en Queries

ÔØî **INCORRECTO**:

```java
@Transactional  // No especifica readOnly
protected UserResult process() {
    return userRepository.findById(id);
}
```

Ô£à **CORRECTO**:

```java
@Transactional(readOnly = true)
protected UserResult process() {
    // Optimizado para solo lectura
    return userRepository.findById(id);
}
```

### 6. Manejo de Excepciones en Transacciones

Ô£à **CORRECTO**:

```java
@Transactional(rollbackFor = Exception.class)
protected Result process() throws DomainException {
    try {
        // Operaciones
        User user = userRepository.save(entity);
        setResult(UserResult.from(user));
        setExecuted(true);
    } catch (DataAccessException e) {
        log.error("Error de BD", e);
        throw new DomainException("Error al guardar", e);
    }
}
```

### 7. Propagaci├│n en Commands Anidados

Ô£à **CORRECTO**:

```java
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
protected Result process() throws DomainException {
    // Reutiliza transacci├│n existente
    UserResult user = createUserCommand.execute();
    ProfileResult profile = createProfileCommand.execute();
    // Si alguno falla, ambos se revierten
    return combineResults(user, profile);
}
```

### 8. Logging en Transacciones

Ô£à **CORRECTO**:

```java
@Transactional(rollbackFor = Exception.class)
protected Result process() throws DomainException {
    log.debug("Iniciando transacci├│n para {}", context);
    
    try {
        Result result = performOperation();
        log.info("Transacci├│n completada exitosamente");
        setResult(result);
        setExecuted(true);
    } catch (Exception e) {
        log.error("Error en transacci├│n, ejecutando rollback", e);
        throw e;
    }
}
```

### 9. Evitar Operaciones Largas en Transacciones

ÔØî **INCORRECTO**:

```java
@Transactional(rollbackFor = Exception.class)
protected Result process() throws DomainException {
    User user = userRepository.save(entity);
    
    // ┬íNO! Operaci├│n larga dentro de transacci├│n
    Thread.sleep(10000);
    emailService.sendEmail(user.getEmail());
    
    return UserResult.from(user);
}
```

Ô£à **CORRECTO**:

```java
@Transactional(rollbackFor = Exception.class)
protected Result process() throws DomainException {
    // Solo operaciones de BD
    User user = userRepository.save(entity);
    setResult(UserResult.from(user));
    setExecuted(true);
}

@Override
protected void postProcess() throws DomainException {
    // Operaciones largas fuera de transacci├│n
    emailService.sendEmail(result.getEmail());
}
```

### 10. Testing con Transacciones

Ô£à **CORRECTO**:

```java
@Test
@Transactional
@Rollback  // Asegura que no afecta otros tests
void testCreateUser() throws DomainException {
    // Test con rollback autom├ítico
    UserResult result = command.execute();
    entityManager.flush();
    assertNotNull(result);
}
```

## Resumen de Configuraci├│n

| Aspecto         | Recomendaci├│n              | Ejemplo                                   |
|-----------------|----------------------------|-------------------------------------------|
| **Ubicaci├│n**   | Solo en m├®todo `process()` | `@Transactional protected void process()` |
| **Propagaci├│n** | `REQUIRED` para escritura  | `propagation = Propagation.REQUIRED`      |
| **Rollback**    | Todas las excepciones      | `rollbackFor = Exception.class`           |
| **Timeout**     | 30s normal, m├ís para batch | `timeout = 30`                            |
| **Isolation**   | `READ_COMMITTED`           | `isolation = Isolation.READ_COMMITTED`    |
| **ReadOnly**    | `true` para queries        | `readOnly = true`                         |

## Niveles de Aislamiento

```java
// READ_UNCOMMITTED: Menor aislamiento, permite dirty reads
@Transactional(isolation = Isolation.READ_UNCOMMITTED)

// READ_COMMITTED (recomendado): Previene dirty reads
@Transactional(isolation = Isolation.READ_COMMITTED)

// REPEATABLE_READ: Previene dirty y non-repeatable reads
@Transactional(isolation = Isolation.REPEATABLE_READ)

// SERIALIZABLE: Mayor aislamiento, m├ís lento
@Transactional(isolation = Isolation.SERIALIZABLE)
```

## Escenarios Comunes

### Escenario 1: Crear entidad con relaciones

```java
@Transactional(rollbackFor = Exception.class)
protected OrderResult process() throws DomainException {
    // Crear orden principal
    Order order = orderRepository.save(new Order());
    
    // Agregar l├¡neas de orden (cascada)
    for (OrderItem item : context.getItems()) {
        OrderLine line = new OrderLine(order, item);
        order.addLine(line);
    }
    
    // Una sola transacci├│n para todo
    orderRepository.save(order);
    
    setResult(OrderResult.from(order));
    setExecuted(true);
}
```

### Escenario 2: Actualizar m├║ltiples entidades

```java
@Transactional(rollbackFor = Exception.class)
protected TransferResult process() throws DomainException {
    // Operaciones at├│micas
    Account from = accountRepository.findById(context.getFromId())
        .orElseThrow(() -> new DomainException("Cuenta origen no existe"));
    
    Account to = accountRepository.findById(context.getToId())
        .orElseThrow(() -> new DomainException("Cuenta destino no existe"));
    
    // Transferencia
    from.withdraw(context.getAmount());
    to.deposit(context.getAmount());
    
    // Ambos se guardan o ninguno
    accountRepository.save(from);
    accountRepository.save(to);
    
    setResult(new TransferResult(from, to));
    setExecuted(true);
}
```

### Escenario 3: Operaci├│n con servicio externo

```java
@Override
protected void preProcess() throws DomainException {
    // Validar antes de iniciar transacci├│n
    validatePaymentDetails();
    setValid(true);
}

@Transactional(rollbackFor = Exception.class)
protected PaymentResult process() throws DomainException {
    // Primero: actualizar BD
    Payment payment = paymentRepository.save(new Payment());
    
    try {
        // Segundo: llamar servicio externo
        ExternalResponse response = externalPaymentService.charge(payment);
        
        // Tercero: actualizar con respuesta
        payment.setExternalId(response.getId());
        paymentRepository.save(payment);
        
        setResult(PaymentResult.from(payment));
        setExecuted(true);
        
    } catch (ExternalServiceException e) {
        // Rollback autom├ítico
        throw new DomainException("Error en servicio externo", e);
    }
}
```

## Conclusi├│n

El uso correcto de `@Transactional` en este proyecto garantiza:

- Ô£à **Consistencia de datos**: Las operaciones se completan o se revierten completamente
- Ô£à **Integridad**: Los datos permanecen en un estado v├ílido
- Ô£à **Rendimiento**: Transacciones optimizadas y controladas
- Ô£à **Mantenibilidad**: C├│digo claro y predecible
- Ô£à **Testabilidad**: Facilidad para escribir tests confiables

Siguiendo estas gu├¡as, tus Commands manejar├ín transacciones de forma robusta y confiable en el contexto de la
arquitectura `CommandProcessAbstract`.

## Referencias

- [Spring Transaction Management](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#transaction)
- [Spring Boot JPA](https://spring.io/guides/gs/accessing-data-jpa/)
- [Transaction Propagation](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/annotation/Propagation.html)

