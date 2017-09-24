package me.soshin

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking


/**
 * You can control sets of coroutines by providing a parent job
 */
fun main(args: Array<String>) = runBlocking {

    val parentJob = Job()

    List(10) { i ->
        async(CommonPool + parentJob) {
            produceBeautifulUuid()
        }
    }

    parentJob.join()
}
