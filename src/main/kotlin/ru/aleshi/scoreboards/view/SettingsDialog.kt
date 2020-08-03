package ru.aleshi.scoreboards.view

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.aleshi.scoreboards.core.Resources
import ru.aleshi.scoreboards.data.SettingsItem
import java.awt.Point
import java.awt.event.ItemEvent
import javax.swing.*

/**
 * Settings dialog window. Contains all settings.
 */
class SettingsDialog(frame: ScoreboardFrame) : JDialog(frame) {

    private val mTypeCheckedChannel = PublishSubject.create<Pair<SettingsItem, Boolean>>()

    /**
     * An [Observable] for emitting changes in setting items.
     */
    fun getSettingsValue(): Observable<Pair<SettingsItem, Boolean>> = mTypeCheckedChannel

    private val cbUseMType = JCheckBox(Resources.getString("settings.use_m_type"))
        .apply {
            addItemListener { itemEvent: ItemEvent ->
                val isSelected = itemEvent.stateChange == ItemEvent.SELECTED
                mTypeCheckedChannel.onNext(SettingsItem.UseThirdWarningLine to isSelected)
            }
        }

    private val cbRedOnLeft = JCheckBox(Resources.getString("settings.red_on_left"), true)
        .apply {
            addItemListener { itemEvent: ItemEvent ->
                val isSelected = itemEvent.stateChange == ItemEvent.SELECTED
                mTypeCheckedChannel.onNext(SettingsItem.RedOnLeft to isSelected)
            }
        }

    init {
        title = Resources.getString("settings.title")
        add(JPanel().apply {
            border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
            layout = BoxLayout(this, BoxLayout.PAGE_AXIS)
            add(cbUseMType)
            add(Box.createVerticalStrut(16))
            add(cbRedOnLeft)
            add(Box.createVerticalStrut(16))
            add(JLabel(Resources.getString("settings.keybindings_description")))
        })
        setSize(400, 300)
        location = Point(400, 200)
    }
}