package ru.aleshi.scoreboards.view

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.aleshi.scoreboards.data.UserAction
import java.awt.Color
import java.awt.Insets
import java.awt.event.KeyEvent
import javax.swing.*

/**
 * A panel for controlling battle state and opening settings dialog.
 */
class SettingsPanel : JPanel() {

    private val iconPause = ImageIcon(javaClass.getResource("/icons/ic_pause.png"))
    private val iconResume = ImageIcon(javaClass.getResource("/icons/ic_play.png"))

    private val buttonsChannel = PublishSubject.create<UserAction>()

    /**
     * Returns an [Observable] that emits [UserAction] when user hits the button.
     */
    fun getButtonsChannel(): Observable<UserAction> = buttonsChannel

    private val buttonReset = JButton(ImageIcon(javaClass.getResource("/icons/ic_clear.png"))).apply {
        isFocusable = false
        margin = Insets(0, 0, 0, 0)
        addActionListener { buttonsChannel.onNext(UserAction.Reset) }
        actionMap.put("Reset", DefaultActionListenerCaller(this))
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "Reset")
    }

    private val buttonPause = JButton(iconResume).apply {
        isFocusable = false
        addActionListener {
            buttonsChannel.onNext(if (paused) UserAction.Unpause else UserAction.Pause)
            paused = !paused
        }

        actionMap.put("Paused", DefaultActionListenerCaller(this))
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(' '), "Paused")
    }

    private val buttonSettings = JButton(ImageIcon(javaClass.getResource("/icons/ic_settings.png"))).apply {
        isFocusable = false
        margin = Insets(0, 0, 0, 0)
        addActionListener { buttonsChannel.onNext(UserAction.OpenSettings) }
    }


    init {
        isOpaque = false
        background = Color(0xBFBFBF)
        layout = BoxLayout(this, BoxLayout.LINE_AXIS)

        add(buttonReset)
        add(Box.createHorizontalStrut(20))
        add(buttonPause)
        add(Box.createHorizontalStrut(20))
        add(buttonSettings)
    }

    /**
     * Current pause button state.
     */
    var paused: Boolean = true
        set(value) {
            field = value
            if (value) {
                buttonPause.icon = iconResume
                buttonReset.isVisible = true
                buttonSettings.isVisible = true
            } else {
                buttonPause.icon = iconPause
                buttonReset.isVisible = false
                buttonSettings.isVisible = false
            }
        }

}