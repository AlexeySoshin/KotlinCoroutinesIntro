package me.soshin

import kotlinx.coroutines.experimental.*
import java.util.*


fun main(args: Array<String>) {


    val result1 = async<String>(CommonPool) {
        cdnCall("A")
    }

    val result2 = async(CommonPool) {
        cdnCall("B")
    }

    result1.invokeOnCompletion {
        println("${result1.getCompleted()} won")
        result2.cancel()
    }

    result2.invokeOnCompletion {
        println("${result2.getCompleted()} won")
        result1.cancel()
    }

    // Since main is not async by itself, we need to wrap this in block
    runBlocking {
        result1.join()
        result2.join()
    }
}

suspend fun cdnCall(name: String): String {
    val retries = Random().nextInt(10) + 2

    try {
        repeat(retries) { i ->
            delay((Random().nextInt(500) + 500).toLong())
            println("$name still working ($i-$retries)...")
        }
    } catch (ce: CancellationException) {
        println("$name was cancelled")
    } finally {
        println("I can still do finally")
    }

    return name
}