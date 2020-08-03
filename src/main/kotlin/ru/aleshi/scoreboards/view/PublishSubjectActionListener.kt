package ru.aleshi.scoreboards.view

import io.reactivex.rxjava3.subjects.PublishSubject
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * The listener that emits [value] to [targetSubject] when the action performed.
 */
class PublishSubjectActionListener(private val targetSubject: PublishSubject<Int>, private val value: Int) :
    ActionListener {

    override fun actionPerformed(event: ActionEvent?) {
        targetSubject.onNext(value)
    }

}