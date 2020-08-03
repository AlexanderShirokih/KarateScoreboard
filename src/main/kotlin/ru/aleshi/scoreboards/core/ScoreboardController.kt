package ru.aleshi.scoreboards.core

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ru.aleshi.scoreboards.data.BattleInfo
import ru.aleshi.scoreboards.data.ScoreboardState
import ru.aleshi.scoreboards.data.Team
import ru.aleshi.scoreboards.data.WarningCategory
import java.util.concurrent.TimeUnit

/**
 * Default [IScoreboardController] implementation.
 * Contains main business-logic.
 */
class ScoreboardController : IScoreboardController {

    private var timerDisposable: Disposable? = null
    private val battleInfo = BattleInfo()
    private var redOnLeft = true
    private var stopTime: Long = 0L

    /**
     * `true` if M-type warning visible
     */
    var isGroupMVisible = false

    /**
     * Standard battle time in seconds when timer resets
     */
    var battleTime: Long = 120L

    private var currentState: ScoreboardState = ScoreboardState.SplashState
    private val state = BehaviorSubject.create<ScoreboardState>().apply {
        onNext(currentState)
    }

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
        if ((timerDisposable == null || timerDisposable!!.isDisposed) == paused)
            return

        if (paused) {
            timerDisposable?.dispose()
            return
        }

        stopTime = System.currentTimeMillis()
        timerDisposable = Observable
            .interval(100, TimeUnit.MILLISECONDS)
            .subscribe {
                val current = System.currentTimeMillis()
                val delta = current - stopTime
                stopTime = current
                battleInfo.getTime().decrease(delta)
                updateData()
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

    override fun getState(): Observable<ScoreboardState> = state

    private fun updateData() {
        currentState = ScoreboardState.DataState(
            battleInfo = battleInfo,
            isRedOnLeft = redOnLeft,
            isGroupMVisible = isGroupMVisible
        )
        state.onNext(currentState)
    }

}