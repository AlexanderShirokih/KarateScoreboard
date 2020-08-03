package ru.aleshi.scoreboards.view

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.awt.*
import javax.swing.*
import kotlin.math.roundToInt

/**
 * A warning line.
 */
class WarningCategoryView(
    categoryName: String,
    spacing: Int = 30,
    flagsCount: Int = 3,
    flagsColor: Color = Color.white,
    keyStroke: KeyStroke
) : JPanel(FlowLayout()) {

    companion object {
        private const val KEY_ADD = "add"
    }

    private val flags: MutableList<CircularLabel> = MutableList(flagsCount) {
        CircularLabel(
            flagsColor
        )
    }

    private val categoryChannel = PublishSubject.create<Int>()

    /**
     * Returns an [Observable] that emit changes of warning points when control buttons pressed.
     */
    fun getCategoryChannel(): Observable<Int> = categoryChannel

    private val subButton = JButton("-").apply {
        isFocusable = false
        addActionListener(PublishSubjectActionListener(categoryChannel, -1))
    }

    private val addButton = JButton("+").apply {
        isFocusable = false
        addActionListener(PublishSubjectActionListener(categoryChannel, 1))
        actionMap.put(KEY_ADD, DefaultActionListenerCaller(this))
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, KEY_ADD)
    }

    init {
        val size = 50
        val scaledPoints = (size * 0.8f).roundToInt()

        isOpaque = false

        add(subButton)

        add(JLabel(categoryName).apply {
            foreground = flagsColor
            preferredSize = Dimension(size, size)
            horizontalAlignment = JLabel.CENTER
            verticalAlignment = JLabel.CENTER
            font = Font("Serif", Font.PLAIN, 24)
            border = RoundedBorder(flagsColor, 24, 1.75f)
        })

        add(JPanel(FlowLayout(FlowLayout.CENTER, 20, 5)).apply {
            isOpaque = false
            border = RoundedBorder(flagsColor, 24, 1.7f)
            preferredSize = Dimension((size + spacing) * flagsCount, size)

            for (i in 0 until flagsCount) {
                add(flags[i].apply {
                    preferredSize = Dimension(scaledPoints, scaledPoints)
                })
            }
        })

        add(addButton)
    }

    /**
     * Sets current amount of enabled warning point labels.
     */
    fun setPoints(points: Int) {
        if (points < 0 || points > flags.size)
            error("Invalid element id $points")

        for ((i, flag) in flags.withIndex()) {
            val currentState = flag.isActive
            flag.isActive = i < points
            if (currentState != flag.isActive) {
                flag.repaint()
            }
        }
    }

    /**
     * Shows or hides control buttons
     */
    fun setControlButtonsVisible(isVisible: Boolean) {
        subButton.isVisible = isVisible
        addButton.isVisible = isVisible
    }
}