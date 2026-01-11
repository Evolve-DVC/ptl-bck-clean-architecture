package com.empresa.plantilla.commons.query;

import com.empresa.plantilla.commons.exception.DomainException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * An abstract test class for query implementations.
 * This class provides a set of common test methods for validating the behavior of queries.
 *
 * @param <C> The type of the query context
 * @param <R> The type of the query result
 * @param <T> The type of the query extending QueryAbstract
 */
@ExtendWith(MockitoExtension.class)
public abstract class QueryTestAbstract<C, R, T extends QueryAbstract<C, R>> {

    /**
     * The query instance to be tested.
     */
    protected T query;

    /**
     * Sets up the test environment before each test method.
     * Initializes the query object by calling the createQuery method.
     */
    @BeforeEach
    public void setUp() {
        query = createQuery();
    }

    /**
     * Tears down the test environment after each test method.
     * Cleans up any mocks that were created during the test.
     */
    @AfterEach
    public void tearDown() {
        cleanUpMocks();
    }

    /**
     * Creates and returns an instance of the query to be tested.
     *
     * @return A new instance of the query
     */
    protected abstract T createQuery();

    /**
     * Creates and returns a valid context for the query.
     *
     * @return A valid context object
     */
    protected abstract C createValidContext();

    /**
     * Creates and returns an invalid context for the query.
     *
     * @return An invalid context object
     */
    protected abstract C createInvalidContext();

    /**
     * Asserts that the result of the query execution is valid.
     *
     * @param result The result returned by the query execution
     */
    protected abstract void assertValidResult(R result);

    /**
     * Mocks any dependencies required for the query execution.
     */
    protected abstract void mockDependencies();

    /**
     * Creates and returns a partial context for the query.
     * A partial context is one that contains some but not all required fields.
     *
     * @return A partial context object
     */
    protected abstract C createPartialContext();

    /**
     * Verifies that the query execution methods are called in the correct order.
     * This should verify that preProcess, process, and postProcess are executed in sequence.
     */
    protected abstract void verifyExecutionOrder();

    /**
     * Verifies that the post-processing step was executed correctly.
     *
     * @param result The result returned by the query execution
     */
    protected abstract void verifyPostProcess(R result);

    /**
     * Cleans up any mocks created during the test.
     * Default implementation is empty, subclasses can override to provide cleanup logic.
     */
    protected void cleanUpMocks() {
        // Implementación por defecto vacía
    }

    /**
     * Tests the execution of the query with a valid context.
     * Mocks dependencies, creates a valid context, executes the query,
     * and asserts that the result is valid.
     */
    @Test
    public void testExecuteWithValidContext() {
        mockDependencies();
        C context = createValidContext();
        R result = query.execute(context);
        assertValidResult(result);
    }

    /**
     * Tests the execution of the query with an invalid context.
     * Expects a DomainException to be thrown.
     */
    @Test
    public void testExecuteWithInvalidContext() {
        C context = createInvalidContext();
        assertThrows(DomainException.class, () -> query.execute(context));
    }

    /**
     * Tests the pre-processing step of the query with a null context.
     * Expects a DomainException to be thrown.
     */
    @Test
    public void testPreProcessWithNullContext() {
        assertThrows(DomainException.class, () -> query.execute(null));
    }

    /**
     * Tests that the post-processing step is executed after query execution.
     * Verifies that postProcess is called and the result is valid.
     */
    @Test
    public void testPostProcessIsExecuted() {
        mockDependencies();
        C context = createValidContext();
        R result = query.execute(context);
        assertValidResult(result);
        verifyPostProcess(result);
    }

    /**
     * Tests that the query execution methods are called in the correct order.
     * Verifies that preProcess, process, and postProcess are executed in sequence.
     */
    @Test
    public void testExecutionOrder() {
        mockDependencies();
        C context = createValidContext();
        query.execute(context);
        verifyExecutionOrder();
    }

    /**
     * Tests the execution of the query with a partial context.
     * A partial context contains some but not all required fields.
     * Expects a DomainException to be thrown due to incomplete context.
     */
    @Test
    public void testExecuteWithPartialContext() {
        C context = createPartialContext();
        assertThrows(DomainException.class, () -> query.execute(context));
    }
}