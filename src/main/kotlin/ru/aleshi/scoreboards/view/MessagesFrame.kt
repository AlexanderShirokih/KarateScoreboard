package ru.aleshi.scoreboards.view

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import ru.aleshi.scoreboards.core.IEventsController
import java.awt.Dimension
import javax.swing.*

class MessagesFrame(private val eventsController: IEventsController) : JFrame() {

    private val scope = MainScope()

    private val content = JPanel().apply { layout = BoxLayout(this, BoxLayout.Y_AXIS) }

    init {
        add(
            JScrollPane(
                content, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
            ).apply { alignmentY = -1f }
        )
        size = Dimension(250, 480)

        scope.launch {
            eventsController.channel.consumeAsFlow().collect { list ->
                setMessages(list)
            }
        }
    }

    fun setMessages(messages: List<String>) {
        content.removeAll()
        messages
            .map { JLabel(it) }
            .forEach { content.add(it) }
        this.revalidate()
        this.repaint()
    }

    /**
     * Detaches subscriptions of view-model events.
     */
    fun clearSubscriptions() {
        scope.cancel()
    }
}