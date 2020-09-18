package ru.aleshi.scoreboards.view

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import ru.aleshi.scoreboards.core.IEventsController
import ru.aleshi.scoreboards.data.*
import ru.aleshi.scoreboards.data.ScoreboardState.DataState
import ru.aleshi.scoreboards.viewmodel.ScoreboardFrameViewModel
import java.awt.Color
import java.awt.event.KeyEvent
import javax.swing.*
import javax.swing.SpringLayout.*

/**
 * The main frame that contains all user interface components.
 */
class ScoreboardFrame(
    isFullscreen: Boolean
) : JFrame() {

    private val scope = MainScope()

    private val leftPanel = JPanel()
    private val rightPanel = JPanel()

    private val leftScoreLabel = ScoreLabel(
        arrayOf(
            KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD7, 0),
            KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, 0),
            KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD1, 0)
        ), bigSize = isFullscreen
    )

    private val rightScoreLabel = ScoreLabel(
        arrayOf(
            KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD9, 0),
            KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD6, 0),
            KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD3, 0)
        ), bigSize = isFullscreen
    )

    private val leftPointsGroup = PointsGroup(
        arrayOf(
            KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0),
            KeyStroke.getKeyStroke(KeyEvent.VK_A, 0),
            KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0)
        )
    )
    private val rightPointsGroup = PointsGroup(
        arrayOf(
            KeyStroke.getKeyStroke(KeyEvent.VK_E, 0),
            KeyStroke.getKeyStroke(KeyEvent.VK_D, 0),
            KeyStroke.getKeyStroke(KeyEvent.VK_C, 0)
        )
    )

    private val timeLabel = TimeLabel(bigSize = isFullscreen)
    private val settingsPanel = SettingsPanel()
    private val settingsDialog = SettingsDialog(this)

    init {
        defaultCloseOperation = EXIT_ON_CLOSE

        isFullscreen.not().apply {
            settingsPanel.isVisible = this
            leftScoreLabel.setControlButtonsVisible(this)
            rightScoreLabel.setControlButtonsVisible(this)
            timeLabel.setControlButtonsVisible(this)
            leftPointsGroup.setControlButtonsVisible(this)
            rightPointsGroup.setControlButtonsVisible(this)
        }

        if (isFullscreen) {
            extendedState = MAXIMIZED_BOTH
            isUndecorated = true
        }

        val springLayout = SpringLayout()
        layout = springLayout

        add(settingsPanel)
        add(leftScoreLabel)
        add(rightScoreLabel)
        add(leftPointsGroup)
        add(rightPointsGroup)
        add(timeLabel)
        add(leftPanel)
        add(rightPanel)

        buildLayout(springLayout)
    }

    private fun buildLayout(layout: SpringLayout) {
        val content = contentPane
        layout.apply {
            putConstraint(NORTH, leftPanel, 0, NORTH, content)
            putConstraint(SOUTH, leftPanel, 0, SOUTH, content)
            putConstraint(WEST, leftPanel, 0, WEST, content)
            putConstraint(EAST, leftPanel, 0, HORIZONTAL_CENTER, content)
            putConstraint(NORTH, rightPanel, 0, NORTH, content)
            putConstraint(SOUTH, rightPanel, 0, SOUTH, content)
            putConstraint(EAST, rightPanel, 0, EAST, content)
            putConstraint(WEST, rightPanel, 0, HORIZONTAL_CENTER, content)

            putConstraint(HORIZONTAL_CENTER, timeLabel, 0, HORIZONTAL_CENTER, content)
            putConstraint(VERTICAL_CENTER, timeLabel, 0, VERTICAL_CENTER, content)

            val contentSouth = getConstraint(SOUTH, content)
            val contentEast = getConstraint(EAST, content)

            getConstraints(timeLabel).apply {
                width = Spring.max(Spring.scale(contentEast, 0.28f), Spring.constant(460))
                height = Spring.scale(contentSouth, 0.28f)
            }

            getConstraints(leftScoreLabel).apply {
                x = Spring.scale(contentEast, 0.06f)
                y = Spring.scale(contentSouth, 0.06f)
            }

            getConstraints(rightScoreLabel).apply {
                x = Spring.sum(Spring.scale(contentEast, 0.94f), Spring.minus(width))
                y = Spring.scale(contentSouth, 0.06f)
            }

            getConstraints(leftPointsGroup).apply {
                x = Spring.scale(contentEast, 0.08f)
                setConstraint(SOUTH, Spring.sum(contentSouth, Spring.minus(Spring.scale(contentSouth, 0.08f))))
            }

            getConstraints(rightPointsGroup).apply {
                x = Spring.sum(Spring.scale(contentEast, 0.92f), Spring.minus(width))
                setConstraint(SOUTH, Spring.sum(contentSouth, Spring.minus(Spring.scale(contentSouth, 0.08f))))
            }

            putConstraint(NORTH, settingsPanel, 20, NORTH, content)
            putConstraint(HORIZONTAL_CENTER, settingsPanel, 0, HORIZONTAL_CENTER, content)
        }
    }

    /**
     * Attaches the [viewModel] to this view and subscribes to view-model events.
     */
    @ExperimentalCoroutinesApi
    fun setViewModel(viewModel: ScoreboardFrameViewModel, eventController: IEventsController) {

        scope.apply {
            launch(Dispatchers.Main) {
                launch {
                    viewModel.getState().collect {
                        when (it) {
                            ScoreboardState.SplashState -> updateData(
                                DataState(
                                    BattleInfo(eventController),
                                    isGroupMVisible = false
                                )
                            )
                            is DataState -> updateData(it)
                        }
                    }
                }

                launch {
                    for (score in leftScoreLabel.getPressedButtons()) {
                        viewModel.onAddScore(Team.LEFT, score)
                    }
                }

                launch {
                    for (score in rightScoreLabel.getPressedButtons()) {
                        viewModel.onAddScore(Team.RIGHT, score)
                    }
                }

                launch {
                    for ((cat, point) in leftPointsGroup.getWarningsChannel(this)) {
                        viewModel.onAddWarning(Team.LEFT, cat, point)
                    }
                }

                launch {
                    for ((cat, point) in rightPointsGroup.getWarningsChannel(this)) {
                        viewModel.onAddWarning(Team.RIGHT, cat, point)
                    }
                }

                launch {
                    for (time in timeLabel.getTimeChannel()) {
                        viewModel.onAddTime(time)
                    }
                }

                launch {
                    for (action in settingsPanel.getButtonsChannel()) {
                        when (action) {
                            UserAction.Reset -> viewModel.onReset()
                            UserAction.Pause -> viewModel.onSetPaused(true)
                            UserAction.Unpause -> viewModel.onSetPaused(false)
                            UserAction.OpenSettings -> settingsDialog.isVisible = true
                        }
                    }
                }

                launch {
                    for ((item, isChecked) in settingsDialog.getSettingsValue()) {
                        viewModel.onSetSettingsItem(item, isChecked)
                    }
                }
            }
        }
    }

    private fun updateData(data: DataState) {
        setColors(data.isRedOnLeft)
        setGroupMVisibility(data.isGroupMVisible)
        SwingUtilities.updateComponentTreeUI(this)

        val battleInfo = data.battleInfo

        setTime(battleInfo.getTime())

        for (team in Team.values()) {
            setScore(team, battleInfo.getPoints(team))

            for (cat in WarningCategory.values())
                setGroupPoints(team, cat, battleInfo.getWarnings(team, cat))
        }

    }

    private fun setScore(team: Team, score: Int) {
        when (team) {
            Team.LEFT -> leftScoreLabel.score = score
            Team.RIGHT -> rightScoreLabel.score = score
        }
    }

    private fun setGroupPoints(team: Team, category: WarningCategory, points: Int) {
        val setter = when (category) {
            WarningCategory.TypeA -> PointsGroup::setGroupAPoints
            WarningCategory.TypeM -> PointsGroup::setGroupMPoints
            WarningCategory.TypeD -> PointsGroup::setGroupDPoints
        }

        when (team) {
            Team.LEFT -> setter.invoke(leftPointsGroup, points)
            Team.RIGHT -> setter.invoke(rightPointsGroup, points)
        }
    }

    private fun setGroupMVisibility(isVisible: Boolean) {
        leftPointsGroup.setGroupMVisibility(isVisible)
        rightPointsGroup.setGroupMVisibility(isVisible)
    }

    private fun setTime(time: BattleTime) {
        timeLabel.setTime(time)
    }

    private fun setColors(leftIsRed: Boolean) {
        if (leftIsRed) {
            leftPanel.background = Color.red
            rightPanel.background = Color.blue
        } else {
            leftPanel.background = Color.blue
            rightPanel.background = Color.red
        }
    }

    /**
     * Detaches subscriptions of view-model events.
     */
    fun clearSubscriptions() {
        scope.cancel()
    }

}