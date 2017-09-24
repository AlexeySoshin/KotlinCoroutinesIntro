package me.soshin

import kotlinx.coroutines.experimental.*
import java.util.*


/**
 * You can control sets of coroutines by providing a parent job
 */
fun main(args: Array<String>) = runBlocking<Unit> {

    val parentJob = Job()

    List(10) { i ->
        async(CommonPool + parentJob) {
            produceBeautifulUuid()
        }
    }

    delay(100)
    parentJob.cancel()
    delay(1000)
}

suspend fun produceBeautifulUuid(): String {
    try {
        val uuids = List(1000) {
            yield()
            UUID.randomUUID()
        }

        println("Coroutine done")
        return uuids.sorted().first().toString()
    } catch (t: CancellationException) {
        println("Got cancelled")
    }

    return ""
}