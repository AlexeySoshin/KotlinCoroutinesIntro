package me.soshin

import kotlinx.coroutines.experimental.*
import java.util.*


/**
 * Let's have to concurrent "network" calls
 * When the first arrives, we cancel the other one, because we don't care about its results anymore
 */
fun main(args: Array<String>) {

    fun onComplete(winner: Deferred<String>, loser: Deferred<String>) {
        println("${winner.getCompleted()} won")
        loser.cancel()
    }

    val result1 = async(CommonPool) {
        cdnCall("A")
    }

    val result2 = async(CommonPool) {
        cdnCall("B")
    }

    result1.invokeOnCompletion {
        onComplete(result1, result2)
    }

    result2.invokeOnCompletion {
        onComplete(result2, result1)
    }

    // Since main is not async by itself, we need to wrap this in block
    runBlocking {
        result1.join()
        result2.join()
    }
}

/**
 * Will return result after some retries and delays
 */
suspend fun cdnCall(name: String): String {
    // Between 2-10 retries
    val retries = Random().nextInt(8) + 2

    try {
        repeat(retries) { i ->
            // Delay between 0.5-1second
            delay((Random().nextInt(500) + 500).toLong())
            println("$name still working ($i-$retries)...")
        }
    } catch (ce: CancellationException) {
        println("$name was cancelled")
    } finally {
        println("$name can still do finally")
    }

    return name
}