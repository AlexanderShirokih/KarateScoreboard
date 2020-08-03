package ru.aleshi.scoreboards

import ru.aleshi.scoreboards.core.Resources
import ru.aleshi.scoreboards.core.ScoreboardController
import ru.aleshi.scoreboards.data.ScreenInfo
import ru.aleshi.scoreboards.view.ScoreboardFrame
import ru.aleshi.scoreboards.viewmodel.ScoreboardFrameViewModel
import java.awt.GraphicsEnvironment
import javax.swing.UIManager

/**
 * Application entry point
 */
fun main() {
    setLookAndFeel()

    Resources.loadFromResources("/i18n/ru-RU.prop")
        .subscribeOn(SwingScheduler)
        .observeOn(SwingScheduler)
        .subscribe {
            setupUI()
        }
}

private fun setupUI() {
    val environment = GraphicsEnvironment.getLocalGraphicsEnvironment()
    val default = environment.defaultScreenDevice
    val screens = environment.screenDevices.mapIndexed { index, graphicsDevice ->
        ScreenInfo(
            index = index + 1,
            bounds = graphicsDevice.defaultConfiguration.bounds,
            isFullscreen = graphicsDevice.isFullScreenSupported,
            isDefault = graphicsDevice == default
        )
    }
    screens.forEach { println(it) }

    val controller = ScoreboardController()
    val scoreboardViewModel = ScoreboardFrameViewModel(controller)

    val frame = ScoreboardFrame(true).apply {
        setViewModel(scoreboardViewModel)
    }

    val mainFrame = ScoreboardFrame(false).apply {
        title = "Karate Scoreboard"
        setSize(1050, 700)
        setViewModel(scoreboardViewModel)
    }

    val scoreboardScreen =
        if (screens.size == 1) default.defaultConfiguration.bounds else screens.first { !it.isDefault }.bounds
    frame.location = scoreboardScreen.location
    frame.isVisible = true
    mainFrame.isVisible = true
}

private fun setLookAndFeel() {
    try {
        for (info in UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus" == info.name) {
                UIManager.setLookAndFeel(info.className)
                break
            }
        }
    } catch (_: Exception) {
    }
}