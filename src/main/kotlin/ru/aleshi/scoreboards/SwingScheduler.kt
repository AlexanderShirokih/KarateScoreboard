package ru.aleshi.scoreboards

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit
import javax.swing.SwingUtilities

/**
 * The RxJava scheduler that schedules events on Swing event thread
 */
object SwingScheduler : Scheduler() {

    private val SWING = Executor { runnable: Runnable ->
        if (SwingUtilities.isEventDispatchThread()) runnable.run() else SwingUtilities.invokeLater(runnable)
    }

    private val swingScheduler = Schedulers.from(SWING)

    override fun createWorker(): Worker = swingScheduler.createWorker()

    override fun schedulePeriodicallyDirect(
        run: Runnable?,
        initialDelay: Long,
        period: Long,
        unit: TimeUnit?
    ): Disposable = swingScheduler.schedulePeriodicallyDirect(run, initialDelay, period, unit)

    override fun scheduleDirect(run: Runnable?): Disposable = swingScheduler.scheduleDirect(run)

    override fun scheduleDirect(run: Runnable?, delay: Long, unit: TimeUnit?): Disposable =
        swingScheduler.scheduleDirect(run, delay, unit)

}