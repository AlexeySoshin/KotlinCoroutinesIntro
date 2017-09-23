package me.soshin

import kotlinx.coroutines.experimental.*


fun main(args: Array<String>) = runBlocking<Unit> {


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

    result1.join()
    result2.join()
}

suspend fun cdnCall(name: String): String {
    val retries = (Math.random() * 10).toInt() + 2

    try {
        repeat(retries) { i ->
            delay(((Math.random() * 500) + 500).toLong())
            println("$name still working ($i-$retries)...")
        }
    } catch (ce: CancellationException) {
        println("$name was cancelled")
    }

    return name
}