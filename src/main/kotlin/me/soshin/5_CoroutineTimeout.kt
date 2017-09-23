package me.soshin

import kotlinx.coroutines.experimental.*
import java.util.*
import java.util.concurrent.TimeUnit


fun main(args: Array<String>) = runBlocking<Unit> {

    var alwaysCompletesJob: Deferred<String>? = null
    try {

        // This job will timeout most of the time
        val timingOutJob = async(CommonPool) {
            withTimeout(5, TimeUnit.SECONDS) {
                longOperation("A")
            }
        }

        alwaysCompletesJob = async(CommonPool) {
            withTimeout(20, TimeUnit.SECONDS) {
                longOperation("B")
            }
        }

        println("Result was ${timingOutJob.await()} + ${alwaysCompletesJob.await()}")

    } catch (t: TimeoutException) {
        println("Timeout $t")
        println("Result was ${alwaysCompletesJob?.await()}")
    }
}

suspend fun longOperation(name: String): String {

    val repeats = Random().nextInt(10) + 3

    repeat(repeats) { i ->
        println("$i) $name still working: $i-$repeats...")
        delay(1000)
    }
    return "Done " + name
}