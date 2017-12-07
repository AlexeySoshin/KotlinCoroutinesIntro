package me.soshin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Introducing thread pool protects from OutOfMemory exceptions,
 * but slows the entire execution
 */
public class ThrottleThreads {

    public static void main(final String[] args) throws InterruptedException {
        // Try setting this to 1, number of cores, 100, 2000, 3000 and see what happens
        final ExecutorService pool = Executors.newFixedThreadPool(100);

        final AtomicInteger counter = new AtomicInteger(0);

        final long start = System.currentTimeMillis();
        for (int i = 0; i < 10_000; i++) {
            pool.submit(() -> {
                try {
                    // Do something
                    counter.incrementAndGet();

                    // Simulate wait on IO
                    Thread.sleep(100);

                    // Do something again
                    counter.incrementAndGet();
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        pool.awaitTermination(20, TimeUnit.SECONDS);
        pool.shutdown();

        System.out.println(String.format("Took me %s millis to complete %s tasks",
                System.currentTimeMillis() - start, counter.get() / 2));
    }
}
