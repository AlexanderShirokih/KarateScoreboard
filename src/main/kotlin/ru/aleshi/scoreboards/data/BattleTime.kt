package ru.aleshi.scoreboards.data

import java.lang.Long.max
import java.util.concurrent.TimeUnit

/**
 * A battle time. Contains current battle time and manages it.
 */
class BattleTime {

    private var timeInMs: Long = 0

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
    fun asString(): String {
        val totalSeconds = TimeUnit.MILLISECONDS.toSeconds(timeInMs)

        val minutes = totalSeconds / 60
        val seconds = totalSeconds - minutes * 60

        return String.format("%d:%02d", minutes, seconds)
    }

}