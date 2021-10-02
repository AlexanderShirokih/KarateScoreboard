package ru.aleshi.scoreboards.core

import kotlinx.coroutines.channels.Channel
import java.util.*

class EventsController : IEventsController {

    private val events = Collections.synchronizedList(mutableListOf<String>())

    override val messagesChannel = Channel<List<String>>()

    override val eventsChannel = Channel<Event>()

    override fun addEvent(event: Event) {
        synchronized(events) {
            events.add(event.toFormattedString())
            eventsChannel.trySend(event)
        }

        notifyDataChanged()
    }

    override fun reset() {
        synchronized(events) {
            events.clear()
        }
        notifyDataChanged()
    }

    private fun notifyDataChanged() {
        synchronized(events) {
            messagesChannel.trySend(events.toList())
        }
    }
}