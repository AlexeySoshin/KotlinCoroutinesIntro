package me.soshin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class ThrottleThreads {

    public static void main(final String[] args) throws InterruptedException {
        final ExecutorService pool = Executors.newFixedThreadPool(100);

        final AtomicInteger counter = new AtomicInteger(0);

        final long start = System.currentTimeMillis();
        for (int i = 0; i < 10_000; i++) {
            pool.submit(() -> {
                try {
                    counter.incrementAndGet();
                    Thread.sleep(100);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        pool.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println(String.format("Took me %s millis to complete %s tasks",
                System.currentTimeMillis() - start, counter.get()));
    }
}
