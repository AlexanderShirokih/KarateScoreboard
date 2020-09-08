package ru.aleshi.scoreboards.viewmodel

import kotlinx.coroutines.flow.Flow
import ru.aleshi.scoreboards.core.IScoreboardController
import ru.aleshi.scoreboards.data.ScoreboardState
import ru.aleshi.scoreboards.data.SettingsItem
import ru.aleshi.scoreboards.data.Team
import ru.aleshi.scoreboards.data.WarningCategory

/**
 * A view-model bridge connecting scoreboard view and its controller.
 */
class ScoreboardFrameViewModel(
    private val scoreboardController: IScoreboardController
) {

    /**
     * Returns current state
     */
    fun getState(): Flow<ScoreboardState> = scoreboardController.getState()

    /**
     * Called when user hits add score button
     */
    fun onAddScore(team: Team, score: Int) {
        scoreboardController.addPoints(team, score)
    }

    /**
     * Called when user hits add time button
     */
    fun onAddTime(time: Int) {
        scoreboardController.addTime(time)
    }

    /**
     * Called when user hots add warning button
     */
    fun onAddWarning(team: Team, cat: WarningCategory, point: Int) {
        scoreboardController.addWarning(team, cat, point)
    }

    /**
     * Called when user sets pause state
     */
    fun onSetPaused(isPaused: Boolean) {
        scoreboardController.setPaused(isPaused)
    }

    /**
     * Called when user hits reset button
     */
    fun onReset() {
        scoreboardController.reset()
    }

    /**
     * Called when user changes settings checkboxes
     */
    fun onSetSettingsItem(settingsItem: SettingsItem, isChecked: Boolean) {
        when (settingsItem) {
            SettingsItem.UseThirdWarningLine -> scoreboardController.setMCatVisibility(isChecked)
            SettingsItem.RedOnLeft -> scoreboardController.setRedOnLeft(isChecked)
            SettingsItem.AddPointsOnWarnings -> scoreboardController.setAddPointsOnWarnings(isChecked)
        }
    }
}