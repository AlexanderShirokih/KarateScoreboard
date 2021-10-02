package ru.aleshi.scoreboards.view

import kotlinx.coroutines.channels.Channel
import ru.aleshi.scoreboards.data.BattleTime
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.GridBagLayout
import javax.swing.*
import javax.swing.border.EmptyBorder


/**
 * Timetable showing battle time.
 */
class TimeLabel(private val fullSize: Boolean) : JPanel() {

    companion object {
        private val timerFont =
            Font.createFont(
                Font.TRUETYPE_FONT,
                ScoreboardFrame::class.java.getResourceAsStream("/font/DigitalFont-Regular.otf")
            ).deriveFont(160f)
        private val bigTimerFont = timerFont.deriveFont(240f)
    }

    private val timeChannel = Channel<Int>()

    private val smallTimeAnimator = Timer(300) {
        timeLabel.apply {
            foreground = when (timeLabel.foreground) {
                Color.white -> Color.red
                else -> Color.white
            }

            repaint()
        }
    }

    /**
     * Returns a [Channel] that emits seconds when user hits control button for adding or subtracting time.
     */
    fun getTimeChannel(): Channel<Int> = timeChannel

    private val subGroup = JPanel().apply {
        isOpaque = false
        isVisible = false
        layout = BoxLayout(this, BoxLayout.PAGE_AXIS)
        add(JButton("-00:30").apply {
            isFocusable = false
            addActionListener(PublishSubjectActionListener(timeChannel, -30))
        })
        add(Box.createVerticalStrut(20))
        add(JButton("-01:00").apply {
            isFocusable = false
            addActionListener(PublishSubjectActionListener(timeChannel, -60))
        })
        add(Box.createVerticalStrut(20))
        add(JButton("-02:00").apply {
            isFocusable = false
            addActionListener(PublishSubjectActionListener(timeChannel, -120))
        })
    }

    private val addGroup = JPanel().apply {
        isOpaque = false
        isVisible = false
        layout = BoxLayout(this, BoxLayout.PAGE_AXIS)
        add(JButton("+00:30").apply {
            isFocusable = false
            addActionListener(PublishSubjectActionListener(timeChannel, 30))
        })
        add(Box.createVerticalStrut(20))
        add(JButton("+01:00").apply {
            isFocusable = false
            addActionListener(PublishSubjectActionListener(timeChannel, 60))
        })
        add(Box.createVerticalStrut(20))
        add(JButton("+02:00").apply {
            isFocusable = false
            addActionListener(PublishSubjectActionListener(timeChannel, 120))
        })
    }

    private val timeLabel = JLabel("0:00").apply {
        foreground = Color.white
        font = if (fullSize) bigTimerFont else timerFont
        isOpaque = true
        background = Color.black
        border = EmptyBorder(20, 0, 0, 0)
        preferredSize = if (fullSize) Dimension(500, 200) else Dimension(340, 150)
        horizontalAlignment = SwingConstants.CENTER
        verticalAlignment = SwingConstants.CENTER
    }

    init {
        background = Color.black
        layout = GridBagLayout()

        add(subGroup)
        add(timeLabel)
        add(addGroup)
    }

    /**
     * Sets the current time.
     */
    fun setTime(time: BattleTime, isRunning: Boolean) {
        timeLabel.text = time.asString()

        val shouldStart = time.isSmallTime && isRunning && !fullSize

        if (shouldStart != smallTimeAnimator.isRunning) {
            if (shouldStart) {
                smallTimeAnimator.start()
            } else {
                timeLabel.foreground = Color.white
                smallTimeAnimator.stop()
            }
        }
    }

    /**
     * Show or hides control buttons
     */
    fun setControlButtonsVisible(isVisible: Boolean) {
        subGroup.isVisible = isVisible
        addGroup.isVisible = isVisible
    }
}