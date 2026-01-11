# Guía de Uso de @Transactional en el Proyecto

## Índice
1. [Introducción](#introducción)
2. [Configuración Básica](#configuración-básica)
3. [Uso en Commands](#uso-en-commands)
4. [Propagación de Transacciones](#propagación-de-transacciones)
5. [Manejo de Excepciones y Rollback](#manejo-de-excepciones-y-rollback)
6. [Testing de Transacciones](#testing-de-transacciones)
7. [Mejores Prácticas](#mejores-prácticas)

## Introducción

Las transacciones garantizan que un conjunto de operaciones se ejecuten de forma atómica: o todas se completan exitosamente o ninguna se aplica. En este proyecto, utilizamos `@Transactional` de Spring Boot para gestionar transacciones de forma declarativa.

## Configuración Básica

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

### 3. Configuración en application.yml

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

### 4. Configuración Avanzada (Opcional)

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

### Ejemplo Básico: CreateUserCommand

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
        // Validaciones sin transacción
        if (context == null || context.getEmail() == null) {
            throw new DomainException("Contexto inválido");
        }
        
        if (userRepository.existsByEmail(context.getEmail())) {
            throw new DomainException("El email ya está registrado");
        }
        
        setValid(true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    protected void process() throws DomainException {
        // Operación transaccional: crear usuario
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
        // Post-procesamiento sin transacción
        if (result != null) {
            emailService.sendWelcomeEmail(result.getEmail());
        }
    }
}
```

### Ejemplo con Múltiples Operaciones: CreateOrderCommand

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
            throw new DomainException("Contexto de orden inválido");
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
            
            // Crear línea de orden
            OrderLine orderLine = OrderLine.builder()
                    .order(savedOrder)
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .price(item.getPrice())
                    .build();
            
            savedOrder.addOrderLine(orderLine);
        }
        
        // 3. Guardar orden completa
        // Si algo falla aquí, TODO se revierte
        orderRepository.save(savedOrder);
        
        setResult(OrderResult.from(savedOrder));
        setExecuted(true);
    }

    @Override
    protected void postProcess() throws DomainException {
        // Enviar notificaciones (fuera de la transacción)
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

## Propagación de Transacciones

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
            throw new DomainException("Contexto inválido");
        }
        setValid(true);
    }

    @Override
    @Transactional(
        propagation = Propagation.REQUIRED,
        rollbackFor = Exception.class
    )
    protected void process() throws DomainException {
        // 1. Crear usuario (se une a esta transacción)
        CreateUserContext userContext = CreateUserContext.builder()
                .name(context.getName())
                .email(context.getEmail())
                .password(context.getPassword())
                .build();
        
        createUserCommand.setContext(userContext);
        UserResult userResult = createUserCommand.execute();
        
        // 2. Crear perfil (se une a esta transacción)
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

### Tipos de Propagación

```java
// REQUIRED (por defecto): Usa transacción existente o crea una nueva
@Transactional(propagation = Propagation.REQUIRED)

// REQUIRES_NEW: Siempre crea una nueva transacción (suspende la existente)
@Transactional(propagation = Propagation.REQUIRES_NEW)

// SUPPORTS: Usa transacción si existe, sino ejecuta sin transacción
@Transactional(propagation = Propagation.SUPPORTS)

// MANDATORY: Requiere transacción existente, lanza excepción si no hay
@Transactional(propagation = Propagation.MANDATORY)

// NOT_SUPPORTED: Se ejecuta sin transacción (suspende la existente)
@Transactional(propagation = Propagation.NOT_SUPPORTED)

// NEVER: Lanza excepción si hay una transacción activa
@Transactional(propagation = Propagation.NEVER)

// NESTED: Crea transacción anidada si hay una existente
@Transactional(propagation = Propagation.NESTED)
```

## Manejo de Excepciones y Rollback

### Ejemplo: Rollback Explícito

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
            throw new DomainException("Contexto de pago inválido");
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
            NotificationException.class  // No revertir por errores de notificación
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
            // La transacción se revertirá automáticamente
            log.error("Error al procesar pago", e);
            throw new PaymentException("Error en el gateway de pago", e);
        }
    }

    @Override
    protected void postProcess() throws DomainException {
        // Enviar confirmación de pago
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
            throw new DomainException("Contexto inválido");
        }
        setValid(true);
    }

    @Override
    protected void process() throws DomainException {
        List<User> successfulImports = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        
        // Cada usuario se importa en su propia transacción
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
                errors.add("Error en transacción: " + e.getMessage());
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
        // Generar reporte de importación
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
        entityManager.clear();  // Limpiar caché
        
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
        
        // Verificar que no se guardó nada
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
                .name("")  // Nombre inválido
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
        // Configuración específica si es necesaria
    }
}
```

## Mejores Prácticas

### 1. Ubicación del @Transactional

✅ **CORRECTO**: Solo en el método `process()`
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

❌ **INCORRECTO**: En toda la clase
```java
@Transactional  // NO hacer esto
public class CreateUserCommand extends CommandProcessAbstract<...> {
    // Toda la clase queda transaccional (incluso preProcess y postProcess)
}
```

### 2. Especificar Rollback Explícitamente

✅ **CORRECTO**:
```java
@Transactional(rollbackFor = Exception.class)
protected void process() throws DomainException {
    // Hace rollback para cualquier excepción
}
```

❌ **INCORRECTO**: Confiar en el comportamiento por defecto
```java
@Transactional  // Solo hace rollback para RuntimeException
protected void process() throws DomainException {
    // DomainException no causará rollback si es checked
}
```

### 3. Usar Timeout Apropiado

✅ **CORRECTO**:
```java
@Transactional(
    rollbackFor = Exception.class,
    timeout = 30  // 30 segundos para operaciones normales
)
protected void process() throws DomainException {
    // Operaciones con límite de tiempo
}
```

⚠️ **PARA OPERACIONES LARGAS**:
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

✅ **CORRECTO**:
```java
@Override
protected void preProcess() throws DomainException {
    // Validaciones (sin transacción)
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
    // Notificaciones (sin transacción)
    emailService.sendWelcomeEmail(result.getEmail());
}
```

### 5. Usar readOnly en Queries

❌ **INCORRECTO**:
```java
@Transactional  // No especifica readOnly
protected UserResult process() {
    return userRepository.findById(id);
}
```

✅ **CORRECTO**:
```java
@Transactional(readOnly = true)
protected UserResult process() {
    // Optimizado para solo lectura
    return userRepository.findById(id);
}
```

### 6. Manejo de Excepciones en Transacciones

✅ **CORRECTO**:
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

### 7. Propagación en Commands Anidados

✅ **CORRECTO**:
```java
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
protected Result process() throws DomainException {
    // Reutiliza transacción existente
    UserResult user = createUserCommand.execute();
    ProfileResult profile = createProfileCommand.execute();
    // Si alguno falla, ambos se revierten
    return combineResults(user, profile);
}
```

### 8. Logging en Transacciones

✅ **CORRECTO**:
```java
@Transactional(rollbackFor = Exception.class)
protected Result process() throws DomainException {
    log.debug("Iniciando transacción para {}", context);
    
    try {
        Result result = performOperation();
        log.info("Transacción completada exitosamente");
        setResult(result);
        setExecuted(true);
    } catch (Exception e) {
        log.error("Error en transacción, ejecutando rollback", e);
        throw e;
    }
}
```

### 9. Evitar Operaciones Largas en Transacciones

❌ **INCORRECTO**:
```java
@Transactional(rollbackFor = Exception.class)
protected Result process() throws DomainException {
    User user = userRepository.save(entity);
    
    // ¡NO! Operación larga dentro de transacción
    Thread.sleep(10000);
    emailService.sendEmail(user.getEmail());
    
    return UserResult.from(user);
}
```

✅ **CORRECTO**:
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
    // Operaciones largas fuera de transacción
    emailService.sendEmail(result.getEmail());
}
```

### 10. Testing con Transacciones

✅ **CORRECTO**:
```java
@Test
@Transactional
@Rollback  // Asegura que no afecta otros tests
void testCreateUser() throws DomainException {
    // Test con rollback automático
    UserResult result = command.execute();
    entityManager.flush();
    assertNotNull(result);
}
```

## Resumen de Configuración

| Aspecto | Recomendación | Ejemplo |
|---------|---------------|---------|
| **Ubicación** | Solo en método `process()` | `@Transactional protected void process()` |
| **Propagación** | `REQUIRED` para escritura | `propagation = Propagation.REQUIRED` |
| **Rollback** | Todas las excepciones | `rollbackFor = Exception.class` |
| **Timeout** | 30s normal, más para batch | `timeout = 30` |
| **Isolation** | `READ_COMMITTED` | `isolation = Isolation.READ_COMMITTED` |
| **ReadOnly** | `true` para queries | `readOnly = true` |

## Niveles de Aislamiento

```java
// READ_UNCOMMITTED: Menor aislamiento, permite dirty reads
@Transactional(isolation = Isolation.READ_UNCOMMITTED)

// READ_COMMITTED (recomendado): Previene dirty reads
@Transactional(isolation = Isolation.READ_COMMITTED)

// REPEATABLE_READ: Previene dirty y non-repeatable reads
@Transactional(isolation = Isolation.REPEATABLE_READ)

// SERIALIZABLE: Mayor aislamiento, más lento
@Transactional(isolation = Isolation.SERIALIZABLE)
```

## Escenarios Comunes

### Escenario 1: Crear entidad con relaciones

```java
@Transactional(rollbackFor = Exception.class)
protected OrderResult process() throws DomainException {
    // Crear orden principal
    Order order = orderRepository.save(new Order());
    
    // Agregar líneas de orden (cascada)
    for (OrderItem item : context.getItems()) {
        OrderLine line = new OrderLine(order, item);
        order.addLine(line);
    }
    
    // Una sola transacción para todo
    orderRepository.save(order);
    
    setResult(OrderResult.from(order));
    setExecuted(true);
}
```

### Escenario 2: Actualizar múltiples entidades

```java
@Transactional(rollbackFor = Exception.class)
protected TransferResult process() throws DomainException {
    // Operaciones atómicas
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

### Escenario 3: Operación con servicio externo

```java
@Override
protected void preProcess() throws DomainException {
    // Validar antes de iniciar transacción
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
        // Rollback automático
        throw new DomainException("Error en servicio externo", e);
    }
}
```

## Conclusión

El uso correcto de `@Transactional` en este proyecto garantiza:

- ✅ **Consistencia de datos**: Las operaciones se completan o se revierten completamente
- ✅ **Integridad**: Los datos permanecen en un estado válido
- ✅ **Rendimiento**: Transacciones optimizadas y controladas
- ✅ **Mantenibilidad**: Código claro y predecible
- ✅ **Testabilidad**: Facilidad para escribir tests confiables

Siguiendo estas guías, tus Commands manejarán transacciones de forma robusta y confiable en el contexto de la arquitectura `CommandProcessAbstract`.

## Referencias

- [Spring Transaction Management](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#transaction)
- [Spring Boot JPA](https://spring.io/guides/gs/accessing-data-jpa/)
- [Transaction Propagation](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/annotation/Propagation.html)

