package ru.aleshi.scoreboards.view

import kotlinx.coroutines.channels.Channel
import ru.aleshi.scoreboards.core.Resources
import ru.aleshi.scoreboards.data.SettingsItem
import java.awt.Point
import java.awt.event.ItemEvent
import javax.swing.*

/**
 * Settings dialog window. Contains all settings.
 */
class SettingsDialog(frame: ScoreboardFrame) : JDialog(frame) {

    private val mTypeCheckedChannel = Channel<Pair<SettingsItem, Boolean>>()

    /**
     * A [Channel] for emitting changes in setting items.
     */
    fun getSettingsValue(): Channel<Pair<SettingsItem, Boolean>> = mTypeCheckedChannel

    private val cbUseMType = JCheckBox(Resources.getString("settings.use_m_type"))
        .apply {
            addItemListener { itemEvent: ItemEvent ->
                val isSelected = itemEvent.stateChange == ItemEvent.SELECTED
                mTypeCheckedChannel.trySend(SettingsItem.UseThirdWarningLine to isSelected)
            }
        }

    private val cbRedOnLeft = JCheckBox(Resources.getString("settings.red_on_left"), true)
        .apply {
            addItemListener { itemEvent: ItemEvent ->
                val isSelected = itemEvent.stateChange == ItemEvent.SELECTED
                mTypeCheckedChannel.trySend(SettingsItem.RedOnLeft to isSelected)
            }
        }

    private val cbAddPointsOnWarnings = JCheckBox(Resources.getString("settings.pts_on_warn"))
        .apply {
            addItemListener { itemEvent: ItemEvent ->
                val isSelected = itemEvent.stateChange == ItemEvent.SELECTED
                mTypeCheckedChannel.trySend(SettingsItem.AddPointsOnWarnings to isSelected)
            }
        }

    private val cbMirrorTeams = JCheckBox(Resources.getString("settings.mirror_teams"))
        .apply {
            addItemListener { itemEvent: ItemEvent ->
                val isSelected = itemEvent.stateChange == ItemEvent.SELECTED
                mTypeCheckedChannel.trySend(SettingsItem.MirrorTeams to isSelected)
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
            add(cbMirrorTeams)
            add(Box.createVerticalStrut(16))
            add(cbAddPointsOnWarnings)
            add(Box.createVerticalStrut(16))
            add(JLabel(Resources.getString("settings.keybindings_description")))
        })
        setSize(500, 340)
        location = Point(400, 200)
    }
}