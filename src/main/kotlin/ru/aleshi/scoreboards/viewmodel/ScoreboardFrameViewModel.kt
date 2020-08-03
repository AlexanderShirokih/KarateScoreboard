package ru.aleshi.scoreboards.viewmodel

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
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
    fun getState(): Observable<ScoreboardState> = scoreboardController.getState()

    /**
     * Called when user hits add score button
     */
    fun onAddScore(team: Team, score: Int): Completable =
        Completable.fromAction { scoreboardController.addPoints(team, score) }

    /**
     * Called when user hits add time button
     */
    fun onAddTime(time: Int): Completable =
        Completable.fromAction { scoreboardController.addTime(time) }

    /**
     * Called when user hots add warning button
     */
    fun onAddWarning(team: Team, cat: WarningCategory, point: Int): Completable =
        Completable.fromAction { scoreboardController.addWarning(team, cat, point) }

    /**
     * Called when user sets pause state
     */
    fun onSetPaused(isPaused: Boolean): Completable =
        Completable.fromAction { scoreboardController.setPaused(isPaused) }

    /**
     * Called when user hits reset button
     */
    fun onReset(): Completable =
        Completable.fromAction { scoreboardController.reset() }

    /**
     * Called when user changes settings checkboxes
     */
    fun onSetSettingsItem(settingsItem: SettingsItem, isChecked: Boolean): Completable =
        Completable.fromAction {
            when (settingsItem) {
                SettingsItem.UseThirdWarningLine -> scoreboardController.setMCatVisibility(isChecked)
                SettingsItem.RedOnLeft -> scoreboardController.setRedOnLeft(isChecked)
            }
        }
}