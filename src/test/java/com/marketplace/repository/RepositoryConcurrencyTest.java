
package com.marketplace.repository;

import com.marketplace.model.User;
import com.marketplace.util.IdGenerator;

import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RepositoryConcurrencyTest {

    private static final int THREAD_COUNT = 100;

    public void testConcurrentUserRegistration() throws Exception {

        UserRepository repository = new InMemoryUserRepository();

        ExecutorService executor =
                Executors.newFixedThreadPool(THREAD_COUNT);

        CountDownLatch start = new CountDownLatch(1);
        AtomicInteger successfulRegistrations = new AtomicInteger(0);

        for (int i = 0; i < THREAD_COUNT; i++) {

            executor.submit(() -> {
                try {
                    start.await();

                    if (repository.add(new User("harsh"))) {
                        successfulRegistrations.incrementAndGet();
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        start.countDown();

        executor.shutdown();
        assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));

        assertEquals(1, successfulRegistrations.get());
        assertTrue(repository.exists("HARSH"));
    }

    public void testConcurrentIdGeneration() throws Exception {

        IdGenerator generator = new IdGenerator();

        ExecutorService executor =
                Executors.newFixedThreadPool(THREAD_COUNT);

        Set<Long> generatedIds =
                ConcurrentHashMap.newKeySet();

        CountDownLatch start = new CountDownLatch(1);

        for (int i = 0; i < THREAD_COUNT; i++) {

            executor.submit(() -> {
                try {
                    start.await();

                    long id = generator.nextId();
                    generatedIds.add(id);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        start.countDown();

        executor.shutdown();
        assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));

        assertEquals(THREAD_COUNT, generatedIds.size());
    }

    public void testConcurrentCategoryIncrements() throws Exception {

        CategoryStatsRepository repository =
                new InMemoryCategoryStatsRepository();

        ExecutorService executor =
                Executors.newFixedThreadPool(THREAD_COUNT);

        CountDownLatch start = new CountDownLatch(1);

        for (int i = 0; i < THREAD_COUNT; i++) {

            executor.submit(() -> {
                try {
                    start.await();
                    repository.increment("Sports");

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        start.countDown();

        executor.shutdown();
        assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));

        assertEquals("Sports", repository.topCategory());
    }

    private void assertEquals(Object expected, Object actual) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(
                    "Expected: " + expected + ", Actual: " + actual
            );
        }
    }

    private void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected condition to be true");
        }
    }
}
