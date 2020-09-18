package ru.aleshi.scoreboards.core

import kotlinx.coroutines.channels.Channel

class EventsController : IEventsController {

    private val events = mutableListOf<String>()

    override val channel = Channel<List<String>>()

    override fun addEvent(event: Event) {
        events.add(event.toFormattedString())
        notifyDataChanged()
    }

    override fun reset() {
        events.clear()
        notifyDataChanged()
    }

    private fun notifyDataChanged() {
        channel.offer(events)
    }
}