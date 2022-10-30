package ru.aleshi.scoreboards.data

import java.lang.Long.max
import java.util.concurrent.TimeUnit

/**
 * A battle time. Contains current battle time and manages it.
 */
class BattleTime {

    private var timeInMs: Long = 0

    /**
     * `true` if remaining time is nearing the end
     */
    val isSmallTime: Boolean
        get() = timeInMs < SMALL_TIME_THRESHOLD

    /**
     * `true` if remaining time is becomes zero
     * */
    val isTimeOut: Boolean
        get() = timeInMs <= 0L

    /**
     * Sets current battle time
     */
    fun setTime(t: Long, unit: TimeUnit) {
        timeInMs = unit.toMillis(t)
        timeInMs = max(timeInMs, 0)
    }

    /**
     * Adds certain amount of time to battle timer
     */
    fun addTime(t: Int, unit: TimeUnit) {
        timeInMs += unit.toMillis(t.toLong())
        timeInMs = max(timeInMs, 0)
    }

    /**
     * Decreases certain amount of time
     */
    fun decrease(deltaInMs: Long) {
        timeInMs -= deltaInMs
        timeInMs = max(timeInMs, 0)
    }

    /**
     * Returns formatted string representation of current battle time
     */
    fun seconds(): String {
        val totalSeconds = TimeUnit.MILLISECONDS.toSeconds(timeInMs)

        val minutes = totalSeconds / 60
        val seconds = totalSeconds - minutes * 60

        return String.format("%d:%02d", minutes, seconds)
    }

    /**
     * Returns formatted string representation of the currently remaining battle time milliseconds
     */
    fun remainingMillis(): String {
        val remainingMillis = timeInMs % 1000L
        return String.format("%03d", remainingMillis)
    }

    companion object {
        private const val SMALL_TIME_THRESHOLD = 5000L
    }

}