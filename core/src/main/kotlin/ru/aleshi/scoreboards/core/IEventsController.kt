package ru.aleshi.scoreboards.core

import kotlinx.coroutines.channels.Channel

/**
 * Controls events
 */
interface IEventsController {

    fun addEvent(event: Event)

    fun reset()

    val channel: Channel<List<String>>

}
