package ru.aleshi.scoreboards.view

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.aleshi.scoreboards.data.BattleTime
import java.awt.*
import javax.swing.*
import javax.swing.border.EmptyBorder

/**
 * Time table showing battle time.
 */
class TimeLabel : JPanel() {

    companion object {
        private val timerFont =
            Font.createFont(
                Font.TRUETYPE_FONT,
                ScoreboardFrame::class.java.getResourceAsStream("/font/DigitalFont-Regular.otf")
            ).deriveFont(160f)
    }

    private val timeChannel = PublishSubject.create<Int>()

    /**
     * Returns an [Observable] that emits seconds when user hits control button for adding or subtracting time.
     */
    fun getTimeChannel(): Observable<Int> = timeChannel

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
        font = timerFont
        isOpaque = true
        background = Color.black
        border = EmptyBorder(20, 0, 0, 0)
        preferredSize = Dimension(340, 150)
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
     * Sets current time.
     */
    fun setTime(time: BattleTime) {
        timeLabel.text = time.asString()
    }

    /**
     * Show or hides control buttons
     */
    fun setControlButtonsVisible(isVisible: Boolean) {
        subGroup.isVisible = isVisible
        addGroup.isVisible = isVisible
    }
}