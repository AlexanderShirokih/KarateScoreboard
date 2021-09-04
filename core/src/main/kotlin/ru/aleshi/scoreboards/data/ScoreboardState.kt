package ru.aleshi.scoreboards.data

/**
 * Describes scoreboard states
 */
sealed class ScoreboardState {

    /**
     * Initial state. Uses when scoreboard application starts and not setup yet
     */
    object SplashState : ScoreboardState()

    /**
     * Data state. Main state, that used in game
     */
    data class DataState(
        val battleInfo: BattleInfo,
        val isGroupMVisible: Boolean,
        val isRedOnLeft: Boolean = true,
        val isMirrored: Boolean = false
    ) : ScoreboardState()
}