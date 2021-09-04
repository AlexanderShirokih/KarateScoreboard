package ru.aleshi.scoreboards.view

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import ru.aleshi.scoreboards.data.WarningCategory
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.KeyStroke

/**
 * A set of [WarningCategoryView]. Contains A, D, M -types of warning categories.
 */
class PointsGroup(keys: Array<KeyStroke>) : JPanel() {

    private val groupA = WarningCategoryView("A", keyStroke = keys[0])
    private val groupD = WarningCategoryView("D", keyStroke = keys[1])
    private val groupM = WarningCategoryView("M", keyStroke = keys[2])

    init {
        isOpaque = false
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(groupA)
        add(groupD)
        add(groupM)
    }

    /**
     * Returns [kotlinx.coroutines.channels.ReceiveChannel] which emits items when control buttons(+1, -1) were pressed on certain warning category
     */
    @ExperimentalCoroutinesApi
    suspend fun getWarningsChannel(scope: CoroutineScope) = scope.produce {
        launch {
            for (a in groupA.getCategoryChannel()) {
                this@produce.send(WarningCategory.TypeA to a)
            }
        }
        launch {
            for (d in groupD.getCategoryChannel()) {
                this@produce.send(WarningCategory.TypeD to d)
            }
        }

        launch {
            for (m in groupM.getCategoryChannel()) {
                this@produce.send(WarningCategory.TypeM to m)
            }
        }
    }

    /**
     * Sets the current amount of A-type warnings
     */
    fun setGroupAPoints(points: Int) = groupA.setPoints(points)

    /**
     * Sets the current amount of D-type warnings
     */
    fun setGroupDPoints(points: Int) = groupD.setPoints(points)

    /**
     * Sets the current amount of M-type warnings
     */
    fun setGroupMPoints(points: Int) = groupM.setPoints(points)

    /**
     * Shows or hides control buttons (-1, +1).
     */
    fun setControlButtonsVisible(isVisible: Boolean) {
        groupA.setControlButtonsVisible(isVisible)
        groupD.setControlButtonsVisible(isVisible)
        groupM.setControlButtonsVisible(isVisible)
    }

    /**
     * Shows or hides M-type warning line.
     */
    fun setGroupMVisibility(isVisible: Boolean) {
        groupM.isVisible = isVisible
    }
}