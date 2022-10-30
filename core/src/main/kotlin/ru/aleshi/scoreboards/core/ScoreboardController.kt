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
class ScoreboardController(private val eventsController: IEventsController) : IScoreboardController {

    private var timerJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)
    private val battleInfo = BattleInfo(eventsController)
    private var redOnLeft = true
    private var mirrored = false
    private var addPointsOnWarnings = false
    private var stopTime: Long = 0L
    private var shootTimeOut = false
    private var showMillis = true

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
        battleInfo.addWarning(team, category, amount, addPointsOnWarnings).apply {
            updateData()
        }

    override fun addPoints(team: Team, amount: Int): Unit =
        battleInfo.addPoints(team, amount).apply {
            updateData()
        }

    override fun addTime(timeInSeconds: Int): Unit =
        battleInfo.addTime(timeInSeconds).apply {
            shootTimeOut = false
            updateData()
        }

    override fun reset() {
        shootTimeOut = false
        battleInfo.reset()
        battleInfo.getTime().setTime(battleTime, TimeUnit.SECONDS)

        updateData()
    }

    override fun setPaused(paused: Boolean) {
        if (battleInfo.isRunning == paused) {
            battleInfo.isRunning = !paused
            updateData()
        }

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

                if (!shootTimeOut && battleInfo.getTime().isTimeOut) {
                    shootTimeOut = true
                    eventsController.addEvent(Event.TimeOutEvent)
                }

                updateData()
            }
        }
    }

    override fun setRedOnLeft(isRedOnLeft: Boolean) {
        redOnLeft = isRedOnLeft
        updateData()
    }

    override fun setMirrorTeams(isMirrored: Boolean) {
        mirrored = isMirrored
        updateData()
    }

    override fun setAddPointsOnWarnings(addPointsOnWarnings: Boolean) {
        this.addPointsOnWarnings = addPointsOnWarnings
        updateData()
    }

    override fun setShowMilliseconds(showMillis: Boolean) {
        this.showMillis = showMillis
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
            isMirrored = mirrored,
            isGroupMVisible = isGroupMVisible,
            showMillis = showMillis,
        )
        state.trySend(currentState)
    }

}