package me.soshin

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.selects.select
import java.util.*


fun main(args: Array<String>) = runBlocking {

    val cdn1 = produce(CommonPool) {
        delay(Random().nextInt(1000).toLong())

        send("A")
    }

    val cdn2 = produce(CommonPool) {
        delay(Random().nextInt(1000).toLong())

        send("B")
    }

    val firstResult = select<String> {
        cdn1.onReceive { result ->
            result
        }
        cdn2.onReceive { result ->
            result
        }
    }

    println("First result was $firstResult")

}