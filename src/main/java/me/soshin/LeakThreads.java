package me.soshin;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * Even if threads are idle, we cannot create thousands of them,
 * unless we have a lot of memory
 */
public class LeakThreads {

    public static void main(final String[] args) {
        System.out.println("Heap is " + Runtime.getRuntime().totalMemory());

        final AtomicInteger counter = new AtomicInteger();
        try {
            for (int i = 0; i < 10_000; i++) {
                new Thread(() -> {
                    try {
                        counter.incrementAndGet();
                        Thread.sleep(10_000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (final OutOfMemoryError oome) {
            System.out.println(String.format("Spawned %s threads before crashing", counter.get()));
            System.exit(-42);
        }
    }
}
