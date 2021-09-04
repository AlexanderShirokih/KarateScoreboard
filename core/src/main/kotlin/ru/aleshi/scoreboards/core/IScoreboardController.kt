package ru.aleshi.scoreboards.core

import kotlinx.coroutines.flow.Flow
import ru.aleshi.scoreboards.data.ScoreboardState
import ru.aleshi.scoreboards.data.Team
import ru.aleshi.scoreboards.data.WarningCategory

/**
 * An interface for the scoreboard controller
 */
interface IScoreboardController {

    /**
     * Returns [Flow] that emits [ScoreboardState]s
     */
    fun getState(): Flow<ScoreboardState>

    /**
     * Adds warning for team [team] on category [category] with amount [amount]
     * @return `true` is warning was added (not filled yet), `false` oterwise
     */
    fun addWarning(team: Team, category: WarningCategory, amount: Int): Boolean

    /**
     * Add [amount] of points to [team]
     */
    fun addPoints(team: Team, amount: Int)

    /**
     * Add certain time(in seconds) to game timer
     */
    fun addTime(timeInSeconds: Int)

    /**
     * Resets timer, points counter, warnings to default state
     */
    fun reset()

    /**
     * Stop the game timer if [paused] is `true`, otherwise starts the timer
     */
    fun setPaused(paused: Boolean)

    /**
     * If `true` then left panel will colored red and right is blue and vice versa
     */
    fun setRedOnLeft(isRedOnLeft: Boolean)

    /**
     * If `true` then team location on external and internal frames will be opposite
     */
    fun setMirrorTeams(isMirrored: Boolean)

    /**
     * If `true` then third warning time become visible
     */
    fun setMCatVisibility(isVisible: Boolean)

    /**
     * If `true` than when warnings added opponent will give points
     */
    fun setAddPointsOnWarnings(addPointsOnWarnings: Boolean)

}