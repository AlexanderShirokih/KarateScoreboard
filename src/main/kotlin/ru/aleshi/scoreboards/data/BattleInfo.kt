package ru.aleshi.scoreboards.data

import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.min

/**
 * A class that contains info about battle state.
 */
class BattleInfo {

    private val leftPlayer = PlayerInfo()
    private val rightPlayer = PlayerInfo()
    private val time = BattleTime()

    /**
     * Resets player setting
     */
    fun reset() {
        leftPlayer.reset()
        rightPlayer.reset()
    }

    /**
     * Adds a warning for [team] in [category] with [amount].
     */
    fun addWarning(
        team: Team,
        category: WarningCategory,
        amount: Int
    ): Boolean {
        val player = getPlayer(team)
        val field =
            when (category) {
                WarningCategory.TypeA -> player::aWarnings
                WarningCategory.TypeM -> player::mWarnings
                WarningCategory.TypeD -> player::dWarnings
            }

        val maxWarnings = 3
        val previous = field.get()
        val current = previous + amount
        val next = max(min(current, maxWarnings), 0)
        val isMaxWarningsReached = current == maxWarnings + 1 || current == -1

        if (!isMaxWarningsReached) {
            addPoints(getOppositeTeam(team), if (amount < 0) -previous else next)
            field.set(next)
        }

        return isMaxWarningsReached
    }

    /**
     * Returns current warnings count for [team] in [category]
     */
    fun getWarnings(team: Team, category: WarningCategory): Int {
        val player = getPlayer(team)
        val field =
            when (category) {
                WarningCategory.TypeA -> player::aWarnings
                WarningCategory.TypeM -> player::mWarnings
                WarningCategory.TypeD -> player::dWarnings
            }
        return field.get()
    }

    /**
     * Adds points to [team] in [amount]
     */
    fun addPoints(team: Team, amount: Int) {
        val player = getPlayer(team)
        player.score += amount
        player.score = player.score.coerceAtLeast(0)
    }

    /**
     * Gets current point count for [team]
     */
    fun getPoints(team: Team): Int = getPlayer(team).score

    /**
     * Adds time to battle timer in seconds.
     */
    fun addTime(timeInSeconds: Int) {
        time.addTime(timeInSeconds, TimeUnit.SECONDS)
    }

    /**
     * Gets current timer value
     */
    fun getTime(): BattleTime = time

    private fun getPlayer(team: Team): PlayerInfo =
        when (team) {
            Team.LEFT -> leftPlayer
            Team.RIGHT -> rightPlayer
        }

    private fun getOppositeTeam(team: Team) =
        when (team) {
            Team.LEFT -> Team.RIGHT
            Team.RIGHT -> Team.LEFT
        }

}