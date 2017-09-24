package me.soshin

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking

/**
 * Actors are coroutines bound to a SendChannel
 */
fun main(args: Array<String>) = runBlocking {

    val actor = repeatActor(2)

    for (c in 'a'..'z') {
        actor.channel.send(c.toString())
        delay(10)
    }
    // Note that sending is done indeed asynchronously, before actor manages to complete it's task
    println("Done sending")

    // Let the actor know there's no more work to do
    actor.channel.close()

    // Wait for actor to clear the queue
    actor.join()
}

fun repeatActor(repeats: Int) = actor<String>(CommonPool, capacity = 100) {

    // Actors are stateful
    var messageCount = 0

    try {
        // Will execute until channel is closed
        for (msg in channel) {
            println("${++messageCount}) ${msg.repeat(repeats)}")
            delay(50)
        }
    } finally {
        println("I'm done")
    }
}