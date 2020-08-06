package ru.aleshi.scoreboards.core

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import ru.aleshi.scoreboards.data.BattleInfo
import ru.aleshi.scoreboards.data.ScoreboardState
import ru.aleshi.scoreboards.data.Team
import ru.aleshi.scoreboards.data.WarningCategory
import java.util.concurrent.TimeUnit

/**
 * Default [IScoreboardController] implementation.
 * Contains main business-logic.
 */

@ExperimentalCoroutinesApi
class ScoreboardController : IScoreboardController {

    private var timerJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)
    private val battleInfo = BattleInfo()
    private var redOnLeft = true
    private var stopTime: Long = 0L

    /**
     * `true` if M-type warning is visible
     */
    private var isGroupMVisible = false

    /**
     * Standard battle time in seconds when timer resets
     */
    var battleTime: Long = 120L

    private var currentState: ScoreboardState = ScoreboardState.SplashState

    private val state = ConflatedBroadcastChannel<ScoreboardState>()

    init {
        reset()
    }

    override fun addWarning(
        team: Team,
        category: WarningCategory,
        amount: Int
    ): Boolean =
        battleInfo.addWarning(team, category, amount).apply {
            updateData()
        }

    override fun addPoints(team: Team, amount: Int): Unit =
        battleInfo.addPoints(team, amount).apply {
            updateData()
        }

    override fun addTime(timeInSeconds: Int): Unit =
        battleInfo.addTime(timeInSeconds).apply {
            updateData()
        }

    override fun reset() {
        battleInfo.reset()
        battleInfo.getTime().setTime(battleTime, TimeUnit.SECONDS)

        updateData()
    }

    override fun setPaused(paused: Boolean) {
        if ((timerJob == null || !timerJob!!.isActive) == paused)
            return

        if (paused) {
            timerJob?.cancel()
            return
        }

        timerJob = scope.launch {
            stopTime = System.currentTimeMillis()

            while (isActive) {
                delay(100)

                val current = System.currentTimeMillis()
                val delta = current - stopTime
                stopTime = current
                battleInfo.getTime().decrease(delta)
                updateData()
            }
        }
    }

    override fun setRedOnLeft(isRedOnLeft: Boolean) {
        redOnLeft = isRedOnLeft
        updateData()
    }

    override fun setMCatVisibility(isVisible: Boolean) {
        isGroupMVisible = isVisible
        updateData()
    }

    @FlowPreview
    override fun getState(): Flow<ScoreboardState> = state.asFlow()

    private fun updateData() {
        currentState = ScoreboardState.DataState(
            battleInfo = battleInfo,
            isRedOnLeft = redOnLeft,
            isGroupMVisible = isGroupMVisible
        )
        state.offer(currentState)
    }

}