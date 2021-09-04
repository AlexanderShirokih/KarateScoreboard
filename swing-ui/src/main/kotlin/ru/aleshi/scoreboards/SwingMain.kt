package ru.aleshi.scoreboards

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.aleshi.scoreboards.core.EventsController
import ru.aleshi.scoreboards.core.ScoreboardController
import ru.aleshi.scoreboards.data.ScreenInfo
import ru.aleshi.scoreboards.view.MessagesFrame
import ru.aleshi.scoreboards.view.ScoreboardFrame
import ru.aleshi.scoreboards.viewmodel.ScoreboardFrameViewModel
import java.awt.GraphicsEnvironment
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.UIManager

@ExperimentalCoroutinesApi
object SwingMain {

    fun setup() {
        setLookAndFeel()
        setupUI()
    }

    @ExperimentalCoroutinesApi
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

        val eventsController = EventsController()
        val controller = ScoreboardController(eventsController)
        val scoreboardViewModel = ScoreboardFrameViewModel(controller)

        val frame = ScoreboardFrame(true).apply {
            setViewModel(scoreboardViewModel, eventsController)
            addWindowStateListener(object : WindowAdapter() {
                override fun windowClosing(event: WindowEvent?) = clearSubscriptions()
            })
        }

        val mainFrame = ScoreboardFrame(false).apply {
            title = "Karate Scoreboard"
            setSize(1050, 700)
            setViewModel(scoreboardViewModel, eventsController)
            addWindowStateListener(object : WindowAdapter() {
                override fun windowClosing(event: WindowEvent?) = clearSubscriptions()
            })
        }

        val messagesFrame = MessagesFrame(eventsController).apply {
            addWindowStateListener(object : WindowAdapter() {
                override fun windowClosing(event: WindowEvent?) = clearSubscriptions()
            })
        }

        val scoreboardScreen =
            if (screens.size == 1) default.defaultConfiguration.bounds else screens.first { !it.isDefault }.bounds
        frame.location = scoreboardScreen.location
        frame.isVisible = true
        mainFrame.isVisible = true
        messagesFrame.isVisible = true
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
}