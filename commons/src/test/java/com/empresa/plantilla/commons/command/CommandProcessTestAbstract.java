package com.empresa.plantilla.commons.command;

import com.empresa.plantilla.commons.exception.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * An abstract test class for command process implementations.
 * This class provides a set of common test methods for validating the behavior of command processes.
 * It includes tests for synchronous and asynchronous execution, context handling, result validation,
 * and flag management.
 *
 * <p>This class is designed to be extended by concrete test classes that provide specific
 * implementations for command creation, context creation, and result validation.</p>
 *
 * <p>Usage example:</p>
 * <pre>
 * public class MyCommandProcessTest extends CommandProcessTestAbstract&lt;MyContext, MyResult, MyCommandProcess&gt; {
 *     &#64;Override
 *     protected MyCommandProcess createCommand() {
 *         return new MyCommandProcess();
 *     }
 *
 *     &#64;Override
 *     protected MyContext createValidContext() {
 *         return new MyContext(...);
 *     }
 *
 *     // Implement other abstract methods...
 * }
 * </pre>
 *
 * @param <C> The type of the command context
 * @param <R> The type of the command result
 * @param <T> The type of the command process extending CommandProcessAbstract
 */
@ExtendWith(MockitoExtension.class)
public abstract class CommandProcessTestAbstract<C, R, T extends CommandProcessAbstract<C, R>> {

    /**
     * Mock executor for testing multitasking mode.
     * This mock is injected by Mockito and can be used for advanced testing scenarios.
     */
    @Mock
    protected ThreadPoolExecutor executor;

    /**
     * The command process instance being tested.
     * This instance is created in the setUp method and used across all test methods.
     */
    protected T command;

    /**
     * Sets up the test environment before each test method.
     * Creates a new command instance and sets its executor to a real single-threaded executor
     * for testing purposes.
     *
     * <p>This method is annotated with @BeforeEach, meaning it runs before each test method
     * to ensure a clean test environment.</p>
     */
    @BeforeEach
    public void setUp() {
        command = createCommand();
        // Usar un executor real para pruebas
        Executor testExecutor = Executors.newSingleThreadExecutor();
        command.setExecutor(testExecutor);
    }


    /**
     * Creates an instance of the command process to be tested.
     * This method must be implemented by concrete test classes to provide
     * the specific command implementation to test.
     *
     * @return A new instance of the command process
     */
    protected abstract T createCommand();

    /**
     * Creates a valid context for testing the command process.
     * This method must be implemented by concrete test classes to provide
     * a context that should pass validation and allow successful command execution.
     *
     * @return A valid context instance
     */
    protected abstract C createValidContext();

    /**
     * Creates an invalid context for testing error scenarios.
     * This method must be implemented by concrete test classes to provide
     * a context that should fail validation and cause the command execution to throw an exception.
     *
     * @return An invalid context instance
     */
    protected abstract C createInvalidContext();

    /**
     * Asserts that the result of the command execution is valid.
     * This method must be implemented by concrete test classes to validate
     * the specific properties of the result returned by the command.
     *
     * @param result The result returned by the command execution
     */
    protected abstract void assertValidResult(R result);

    /**
     * Sets the specific method to be tested in the command process.
     * This method should be implemented by subclasses to define
     * which method of the command to invoke during tests.
     *
     * <p>This allows test classes to configure the command before execution,
     * such as setting which internal method should be called during the execute phase.</p>
     */
    protected abstract void setMethod();

    /**
     * Tests the execution of the command with a valid context.
     * Verifies that:
     * <ul>
     *   <li>The command executes successfully</li>
     *   <li>A valid result is returned</li>
     *   <li>The executed flag is set to true</li>
     * </ul>
     *
     * @throws DomainException if the command execution fails
     */
    @Test
    public void testExecuteWithValidContext() throws DomainException {
        C context = createValidContext();
        command.setContext(context);
        setMethod();
        command.execute();
        R result = command.getResult();
        assertValidResult(result);
        assertTrue(command.isExecuted());
    }

    /**
     * Tests the execution of the command with an invalid context.
     * Verifies that:
     * <ul>
     *   <li>A DomainException is thrown</li>
     *   <li>The executed flag remains false</li>
     * </ul>
     *
     * <p>This test ensures proper error handling when invalid data is provided.</p>
     */
    @Test
    public void testExecuteWithInvalidContext() {
        C context = createInvalidContext();
        command.setContext(context);
        assertThrows(DomainException.class, () -> command.execute());
        assertFalse(command.isExecuted());
    }

    /**
     * Tests setting the context of the command.
     * Verifies that the context is properly stored and can be retrieved.
     *
     * <p>This test validates the basic context management functionality.</p>
     */
    @Test
    public void testSetContext() {
        C context = createValidContext();
        command.setContext(context);
        assertEquals(context, command.getContext());
    }

    /**
     * Tests retrieving the result of the command execution.
     * Verifies that:
     * <ul>
     *   <li>The command executes successfully</li>
     *   <li>The result can be retrieved</li>
     *   <li>Multiple calls to getResult return the same result</li>
     * </ul>
     *
     * @throws DomainException if the command execution fails
     */
    @Test
    public void testGetResult() throws DomainException {
        C context = createValidContext();
        command.setContext(context);
        setMethod();
        command.execute();
        R result = command.getResult();
        assertEquals(result, command.getResult());
    }

    /**
     * Tests the execution of the command with a context provided directly.
     * Verifies that:
     * <ul>
     *   <li>The command executes successfully</li>
     *   <li>A valid result is returned</li>
     *   <li>The executed flag is set to true</li>
     *   <li>The context is properly stored</li>
     * </ul>
     *
     * <p>This test validates the direct execution pattern where context is set before execution.</p>
     *
     * @throws DomainException if the command execution fails
     */
    @Test
    public void testExecuteWithProvidedContext() throws DomainException {
        C context = createValidContext();
        command.setContext(context);
        setMethod();
        R result = command.execute();
        assertValidResult(result);
        assertTrue(command.isExecuted());
        assertEquals(context, command.getContext());
    }

    /**
     * Tests the asynchronous execution of the command with a valid context.
     * Verifies that:
     * <ul>
     *   <li>The command executes successfully in async mode</li>
     *   <li>A valid result is returned</li>
     *   <li>The executed flag is set to true</li>
     * </ul>
     *
     * <p>This test ensures that the command can be executed asynchronously when the async flag is set.</p>
     *
     * @throws DomainException if the command execution fails
     */
    @Test
    public void testExecuteAsync() throws DomainException {
        C context = createValidContext();
        command.setContext(context);
        command.setAsync(true);
        setMethod();
        R result = command.execute();
        assertValidResult(result);
        assertTrue(command.isExecuted());
    }

    /**
     * Tests the asynchronous execution of the command with an invalid context.
     * Verifies that:
     * <ul>
     *   <li>A DomainException is thrown even in async mode</li>
     *   <li>The executed flag remains false</li>
     * </ul>
     *
     * <p>This test ensures proper error handling in asynchronous execution mode.</p>
     */
    @Test
    public void testExecuteAsyncWithInvalidContext() {
        C context = createInvalidContext();
        command.setContext(context);
        command.setAsync(true);
        assertThrows(DomainException.class, () -> command.execute());
        assertFalse(command.isExecuted());
    }

    /**
     * Tests the valid flag of the command.
     * Verifies that:
     * <ul>
     *   <li>The valid flag is initially false</li>
     *   <li>The flag can be set to true</li>
     *   <li>The flag value is properly returned</li>
     * </ul>
     *
     * <p>This test validates the basic flag management for the valid state.</p>
     */
    @Test
    public void testIsValidFlag() {
        assertFalse(command.isValid());
        command.setValid(true);
        assertTrue(command.isValid());
    }

    /**
     * Tests the async flag of the command.
     * Verifies that:
     * <ul>
     *   <li>The async flag is initially false</li>
     *   <li>The flag can be set to true</li>
     *   <li>The flag value is properly returned</li>
     * </ul>
     *
     * <p>This test validates the basic flag management for the async state.</p>
     */
    @Test
    public void testIsAsyncFlag() {
        assertFalse(command.isAsync());
        command.setAsync(true);
        assertTrue(command.isAsync());
    }

    /**
     * Tests the executed flag of the command.
     * Verifies that:
     * <ul>
     *   <li>The executed flag is initially false</li>
     *   <li>The flag can be set to true</li>
     *   <li>The flag value is properly returned</li>
     * </ul>
     *
     * <p>This test validates the basic flag management for the executed state.</p>
     */
    @Test
    public void testIsExecutedFlag() {
        assertFalse(command.isExecuted());
        command.setExecuted(true);
        assertTrue(command.isExecuted());
    }

    /**
     * Tests the preProcess method of the command.
     * This method should be overridden by subclasses to test the specific
     * preProcess behavior of their commands.
     *
     * <p>The preProcess method is typically called before the main execution
     * and can be used for setup, validation, or preparation tasks.</p>
     *
     * <p>Subclasses should implement this method to verify that:</p>
     * <ul>
     *   <li>PreProcess logic executes correctly</li>
     *   <li>Any necessary setup is performed</li>
     *   <li>Validation logic works as expected</li>
     * </ul>
     *
     * @throws DomainException if the preProcess execution fails
     */
    public abstract void testPreProcess() throws DomainException;

    /**
     * Tests the postProcess method of the command.
     * This method should be overridden by subclasses to test the specific
     * postProcess behavior of their commands.
     *
     * <p>The postProcess method is typically called after the main execution
     * and can be used for cleanup, result transformation, or finalization tasks.</p>
     *
     * <p>Subclasses should implement this method to verify that:</p>
     * <ul>
     *   <li>PostProcess logic executes correctly</li>
     *   <li>Any necessary cleanup is performed</li>
     *   <li>Result transformation works as expected</li>
     * </ul>
     *
     * @throws DomainException if the postProcess execution fails
     */
    public abstract void testPostProcess() throws DomainException;
}