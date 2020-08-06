package ru.aleshi.scoreboards.view

import kotlinx.coroutines.channels.SendChannel
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * The listener that emits [value] to [targetSubject] when the action performed.
 */
class PublishSubjectActionListener(
    private val targetSubject: SendChannel<Int>, private val value: Int
) :
    ActionListener {

    override fun actionPerformed(event: ActionEvent?) {
        targetSubject.offer(value)
    }

}