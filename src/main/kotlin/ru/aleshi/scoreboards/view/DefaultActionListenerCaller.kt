package ru.aleshi.scoreboards.view

import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JButton

/**
 * Helper class that calls first action listener of the target button.
 */
class DefaultActionListenerCaller(target: JButton) : AbstractAction() {

    private val defaultAction = target.actionListeners[0]

    override fun actionPerformed(e: ActionEvent?) = defaultAction.actionPerformed(e)

}