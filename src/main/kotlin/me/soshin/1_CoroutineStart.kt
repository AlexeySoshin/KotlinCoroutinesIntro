package me.soshin;

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger


/**
 * Same example we've seen crashing with threads, now with coroutines
 */
fun main(args: Array<String>) {

    val latch = CountDownLatch(10_000)
    val c = AtomicInteger()

    val start = System.currentTimeMillis()
    for (i in 1..10_000) {
        launch(CommonPool) {
            c.incrementAndGet()
            delay(100)
            c.incrementAndGet()
            latch.countDown()
        }
    }

    latch.await(10, TimeUnit.SECONDS)

    println("Executed ${c.get() / 2} coroutines in ${System.currentTimeMillis() - start}ms")
}