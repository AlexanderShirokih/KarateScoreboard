package ru.aleshi.scoreboards.view

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.awt.Color
import java.awt.Font
import javax.swing.*

/**
 * A label containing digits for displaying score points.
 * @param keys keys for binding to [+1, +2, +3] actions.
 */
class ScoreLabel(
    keys: Array<KeyStroke>
) : JPanel() {

    companion object {
        private const val KEY_ADD_ONE = "add_one"
        private const val KEY_ADD_TWO = "add_two"
        private const val KEY_ADD_THREE = "add_three"
        private val labelFont = Font("Serif", Font.PLAIN, 200)
    }

    private val pressedButtons = PublishSubject.create<Int>()

    /**
     * Returns [Observable] that emits events when pressed button for adding or subtraction points.
     */
    fun getPressedButtons(): Observable<Int> = pressedButtons

    private val subScoreGroup = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        isOpaque = false
        add(JButton("-1").apply {
            isFocusable = false
            addActionListener(PublishSubjectActionListener(pressedButtons, -1))
        })
        add(Box.createVerticalStrut(20))
        add(JButton("-2").apply {
            isFocusable = false
            addActionListener(PublishSubjectActionListener(pressedButtons, -2))
        })
        add(Box.createVerticalStrut(20))
        add(JButton("-3").apply {
            isFocusable = false
            addActionListener(PublishSubjectActionListener(pressedButtons, -3))
        })
    }

    private val addScoreGroup = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.PAGE_AXIS)
        isOpaque = false
        add(JButton("+1").apply {
            isFocusable = false
            addActionListener(PublishSubjectActionListener(pressedButtons, 1))
            actionMap.put(KEY_ADD_ONE, DefaultActionListenerCaller(this))
            getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keys[0], KEY_ADD_ONE)
        })
        add(Box.createVerticalStrut(20))
        add(JButton("+2").apply {
            isFocusable = false
            addActionListener(PublishSubjectActionListener(pressedButtons, 2))
            actionMap.put(KEY_ADD_TWO, DefaultActionListenerCaller(this))
            getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keys[1], KEY_ADD_TWO)
        })
        add(Box.createVerticalStrut(20))
        add(JButton("+3").apply {
            isFocusable = false
            addActionListener(PublishSubjectActionListener(pressedButtons, 3))
            actionMap.put(KEY_ADD_THREE, DefaultActionListenerCaller(this))
            getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keys[2], KEY_ADD_THREE)
        })
    }

    private val scoreLabel = JLabel("0").apply {
        foreground = Color.white
        font = labelFont
    }

    init {
        layout = BoxLayout(this, BoxLayout.X_AXIS)
        isOpaque = false

        add(subScoreGroup)
        add(scoreLabel)
        add(addScoreGroup)
    }

    /**
     * Sets or gets current value of score points.
     */
    var score: Int
        get() = scoreLabel.text.toIntOrNull() ?: 0
        set(value) {
            scoreLabel.text = value.toString()
        }

    /**
     * Shows or hides control buttons.
     */
    fun setControlButtonsVisible(isVisible: Boolean) {
        subScoreGroup.isVisible = isVisible
        addScoreGroup.isVisible = isVisible
    }

}